package xydesk.xy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import xydesk.xy.appAll.ui.AllAppShowUI;
import xydesk.xy.base.XYBaseActivity;
import xydesk.xy.contact.AddContactNameUI;
import xydesk.xy.contact.ContactManUtils;
import xydesk.xy.contant.XYContant;
import xydesk.xy.db.DeskDB;
import xydesk.xy.fragmentf.XYAPPAdapter;
import xydesk.xy.i.GridViewScroolI;
import xydesk.xy.i.ResponseHandler;
import xydesk.xy.i.ViewI;
import xydesk.xy.i.VoiceI;
import xydesk.xy.model.XYAppInfoInDesk;
import xydesk.xy.set.LoveAppAdapter;
import xydesk.xy.set.VoiceSetUI;
import xydesk.xy.utils.AppUtils;
import xydesk.xy.utils.AsyncHttpUtils;
import xydesk.xy.utils.Utils;
import xydesk.xy.view.ItemView;
import xydesk.xy.view.MYGestureListener;
import xydesk.xy.voice.VoiceData;
import xydesk.xy.voice.VoiceUtils;
import xydesk.xy.xydesk.R;

public class MainActivity extends XYBaseActivity {

    @Bind(R.id.love_app)
    GridView loveApp;
    @Bind(R.id.main_app_list)
    GridView mainApp;
    //手势监听
    private MYGestureListener myGestureListener;
    private LoveAppAdapter loveAppAdapter;
    private VoiceUtils voiceUtils;
    private XYAPPAdapter xyappAdapter;
    private DeskDB deskDB;
    private static final int FLING_MIN_DISTANCE = 199;//移动最小距离
    private static final int FLING_MIN_VELOCITY = 199;//移动最大速度
    public static MainActivity instance;
    private List<XYAppInfoInDesk> bottomList = new ArrayList<>();
    private List<XYAppInfoInDesk> xyAppInfoInDeskList;
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case XYContant.XYContants.REFRESH_BOTTOM_APP:
                    bottomList = deskDB.bottomAllApp();
                    loveAppAdapter.refresh(bottomList);
                    break;
                case XYContant.XYContants.REFRESH_FRAGMENT:
                    downPing(false);
                    break;
                case XYContant.XYContants.DELETER_APP:
                    upPing(false);
                    break;
            }
        }
    };

    @Override
    public void initView() {
        setContentView(R.layout.activity_main);
        instance = this;
        ButterKnife.bind(this);
        /**Home键监听*/
        initHomeListen();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //更新联系人
        ContactManUtils.getPeopleInPhone(instance);
        getVersion();
    }

    //初始化数据
    @Override
    public void initData() {
        deskDB = new DeskDB(instance);
        AppUtils.getInstance().getAllAppList(instance);
        AppUtils.getInstance().getAppU(instance);
        VoiceData.getInstance().addSysApp(instance);
        voiceUtils = new VoiceUtils(instance);
        deskDB.addAupdateBottomApp(instance);
        myGestureListener = new MYGestureListener(instance, null, gridViewScroolI);
    }

    //获取版本号
    private void getVersion() {
        //请求数据
        AsyncHttpUtils.get(AsyncHttpUtils.URL, new ResponseHandler() {
            @Override
            public void onSuccess(byte[] result) {
                try {
                    JSONObject jsonObject = new JSONObject(new String(result));
                    JSONObject jsonObjectdesk = new JSONObject(jsonObject.get("desktop").toString());
                    if (!jsonObjectdesk.get("version").toString().equals(Utils.getInstance().getVersionCode(instance))) {
                        Utils.getInstance().toast(instance, "版本已过期，请升级至最新版本");
                        Timer timer = new Timer();
                        TimerTask t = new TimerTask() {
                            @Override
                            public void run() {
                                System.exit(0);
                            }
                        };
                        timer.schedule(t, 3000);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //手势监听
    private GridViewScroolI gridViewScroolI = new GridViewScroolI() {
        @Override
        public void scrool(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            // velocityX：X轴上的移动速度（像素/秒）
            // velocityY：Y轴上的移动速度（像素/秒）

            // X轴的坐标位移大于FLING_MIN_DISTANCE，且移动速度大于FLING_MIN_VELOCITY个像素/秒
            //上一屏
            if (e2.getX() - e1.getX() > FLING_MIN_DISTANCE
                    && Math.abs(velocityX) > FLING_MIN_VELOCITY) {
                upPing(true);
                //下一屏
            } else if (e1.getX() - e2.getX() > FLING_MIN_DISTANCE
                    && Math.abs(velocityX) > FLING_MIN_VELOCITY) {
                downPing(true);
            }
        }
    };

    //上一屏
    private void upPing(boolean isRefresh) {
        AppUtils.getInstance().upApp(instance, isRefresh);
        refreshData();
        Utils.getInstance().toast(instance, whatPing(AppUtils.nowPage));
    }

    //下一屏
    private void downPing(boolean isToast) {
        AppUtils.getInstance().downApp(instance);
        refreshData();
        if (isToast) {
            Utils.getInstance().toast(instance, whatPing(AppUtils.nowPage));
        }
    }

    //数据刷新
    private void refreshData() {
        xyAppInfoInDeskList = AppUtils.getInstance().getAllApp(instance, AppUtils.nowPage);
        xyappAdapter.refresh(xyAppInfoInDeskList);
    }

    @Override
    public void setAdapter() {
        //底部添加的APP
        bottomList = deskDB.bottomAllApp();
        loveAppAdapter = new LoveAppAdapter(instance, bottomList);
        loveApp.setAdapter(loveAppAdapter);
        loveApp.setOnItemClickListener(bottomItemClick);

        //屏幕中的APP
        xyAppInfoInDeskList = AppUtils.getInstance().getAllApp(MainActivity.instance, XYContant.WharFragment.ONE_FRAGMENT);
        xyappAdapter = new XYAPPAdapter(MainActivity.instance, xyAppInfoInDeskList);
        mainApp.setAdapter(xyappAdapter);
        mainApp.setOnItemClickListener(mainAppClick);
        mainApp.setOnItemLongClickListener(onItemLongClickListener);
        mainApp.setOnTouchListener(myGestureListener);
        Utils.getInstance().toast(instance, whatPing(AppUtils.nowPage));
    }

    //底部APP点击事件
    private AdapterView.OnItemClickListener bottomItemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            boolean isVoice = false;
            String packName = "";
            for (XYAppInfoInDesk xyAppInfoInDesk : bottomList) {
                if ((position + 1 + "").equals("3") && xyAppInfoInDesk.appName.equals("语音")) {
                    isVoice = true;
                    break;
                } else if ((position + 1 + "").equals(xyAppInfoInDesk.appBottomPosition)) {
                    packName = xyAppInfoInDesk.appPackageName;
                    isVoice = false;
                }
            }
            if (isVoice) {
                initVoice();
            } else {
                AppUtils.getInstance().openApp(instance, packName);
            }
        }
    };

    //屏幕APP点击事件
    private AdapterView.OnItemClickListener mainAppClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            try {
                AppUtils.getInstance().openApp(instance, xyAppInfoInDeskList.get(position).appPackageName);
            } catch (Exception e) {
                e.printStackTrace();
                Utils.getInstance().toast(instance, "异常退出");
                System.exit(0);
            }
        }
    };

    //屏幕APP长按事件
    private AdapterView.OnItemLongClickListener onItemLongClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
            ItemView.getInstance().showLongView(instance, ItemView.getInstance().itemLong, new ViewI() {
                @Override
                public void click(View view, int itemPosition) {
                    try {
                        XYAppInfoInDesk xyAllAppModel = xyAppInfoInDeskList.get(position);
                        switch ((String) view.getTag()) {
                            case XYContant.LongPressItem.DELE_APP_IN_FRAGMENT:
                                AppUtils.getInstance().deleAtFragment(instance, xyAllAppModel.appPackageName);
                                Message m = handler.obtainMessage();
                                m.what = XYContant.XYContants.DELETER_APP;
                                handler.sendMessage(m);
                                Utils.getInstance().toast(instance, "删除成功");
                                break;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Utils.getInstance().toast(instance, "异常退出");
                        System.exit(0);
                    }
                }
            });
            return true;
        }
    };

    //判断第几屏幕
    private String whatPing(String whatF) {
        String ping;
        switch (whatF) {
            case XYContant.WharFragment.ONE_FRAGMENT:
                ping = "第一屏";
                break;
            case XYContant.WharFragment.TWO_FRAGMENT:
                ping = "第二屏";
                break;
            case XYContant.WharFragment.THREE_FRAGMENT:
                ping = "第三屏";
                break;
            case XYContant.WharFragment.FOUR_FRAGMENT:
                ping = "第四屏";
                break;
            default:
                ping = XYContant.XYContants.F;
                break;
        }
        ping = ping + ",当前屏幕共" + xyAppInfoInDeskList.size() + "项";
        return ping;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return KeyEvent.KEYCODE_BACK != keyCode && super.onKeyDown(keyCode, event);
    }

    @OnClick({R.id.all_app, R.id.up_ping, R.id.down_ping})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.up_ping:
                upPing(true);
                break;
            case R.id.down_ping:
                downPing(true);
                break;
            case R.id.all_app:
                ItemView.getInstance().showLongView(instance, ItemView.getInstance().menu_click, new ViewI() {
                    @Override
                    public void click(View view, int itemPosition) {
                        switch ((String) view.getTag()) {
                            case XYContant.ClickMenu.ALL_APP_IN_MENU:
                                Intent intentAllAppInMenu = new Intent();
                                intentAllAppInMenu.setClass(instance, AllAppShowUI.class);
                                startActivity(intentAllAppInMenu);
                                break;
                            case XYContant.ClickMenu.APP_SET_IN_MENU:
                                Intent intentAppSetInMenu = new Intent();
                                intentAppSetInMenu.setClass(instance, VoiceSetUI.class);
                                startActivity(intentAppSetInMenu);
                                break;
                            case XYContant.ClickMenu.CONTACT_NAME_SET:
                                Intent intentContactNameSet = new Intent();
                                intentContactNameSet.setClass(instance, AddContactNameUI.class);
                                startActivity(intentContactNameSet);
                                break;
                            case XYContant.ClickMenu.REFRESH_FRAGMENT_IN_MENU:
                                break;
                        }
                    }
                });
                break;
        }
    }

    //语音初始化
    private void initVoice() {
        voiceUtils.startVoice(new VoiceI() {
            @Override
            public void voiceResult(String lastRec) {
                try {
                    if (lastRec.length() > 2) {
                        if (lastRec.contains("打开") && !lastRec.contains("拨打")) {
                            VoiceData.getInstance().openApp(instance, lastRec);
                        } else if (lastRec.contains("拨打") && !lastRec.contains("打开")) {
                            //有无联系人
                            if (ContactManUtils.allContact.isEmpty()) {
                                Utils.getInstance().toast(instance, "暂无联系人");
                                return;
                            }
                            String number = deskDB.getContactNum(lastRec);
                            //直接拨打
                            if (!number.equals(XYContant.XYContants.F)) {
                                ContactManUtils.callPhone(instance, number);
                                return;
                            }
                            //是否为别名，拨打别名
                            boolean isHave = false;
                            String num = "";
                            for (String name : ContactManUtils.allContact.keySet()) {
                                if (lastRec.contains(name)) {
                                    isHave = true;
                                    num = name;
                                    break;
                                } else {
                                    isHave = false;
                                }
                            }
                            if (isHave) {
                                ContactManUtils.callPhone(instance, ContactManUtils.allContact.get(num));
                            } else {
                                Utils.getInstance().toast(instance, "没有找到您要的联系人");
                            }
                        } else {
                            Utils.getInstance().toast(instance, "没听懂您说的请说打开或拨打");
                        }
                    } else {
                        Utils.getInstance().toast(instance, "说的太少了，我听不懂");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /************************************
     * 桌面默认
     ************************************************/
    private void initHomeListen() {
        //注册广播
        IntentFilter homeFilter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        registerReceiver(mHomeKeyEventReceiver, homeFilter);
    }

    /*//小米默认桌面自弹出
    private void xiaomi() {
        Intent paramIntent = new Intent("android.intent.action.MAIN");
        paramIntent.setComponent(new ComponentName("android", "com.android.internal.app.ResolverActivity"));
        paramIntent.addCategory("android.intent.category.DEFAULT");
        paramIntent.addCategory("android.intent.category.HOME");
        startActivity(paramIntent);
    }*/

    /**
     * 监听是否点击了home键将客户端推到后台
     */
    private BroadcastReceiver mHomeKeyEventReceiver = new BroadcastReceiver() {
        final String SYS_KEY = "reason"; //标注下这里必须是这么一个字符串值
        final String SYS_HOME_KEY = "homekey";//标注下这里必须是这么一个字符串值

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                String reason = intent.getStringExtra(SYS_KEY);
                if (reason != null && reason.equals(SYS_HOME_KEY)) {
                    //表示按了home键,程序到了后台
                    xyAppInfoInDeskList = AppUtils.getInstance().getAllApp(MainActivity.instance, XYContant.WharFragment.ONE_FRAGMENT);
                    xyappAdapter.refresh(xyAppInfoInDeskList);
                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(instance);
    }
}
