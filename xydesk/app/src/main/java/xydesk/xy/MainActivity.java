package xydesk.xy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import xydesk.xy.appAll.ui.AllAppShowUI;
import xydesk.xy.base.XYBaseActivity;
import xydesk.xy.base.XYBaseFragment;
import xydesk.xy.contact.AddContactNameUI;
import xydesk.xy.contact.ContactManUtils;
import xydesk.xy.contant.XYContant;
import xydesk.xy.db.DeskDB;
import xydesk.xy.fragmentf.FourAppFragment;
import xydesk.xy.fragmentf.FragmentViewAdapter;
import xydesk.xy.fragmentf.OneAppFragment;
import xydesk.xy.fragmentf.ThreeAppFragment;
import xydesk.xy.fragmentf.TwoAppFragment;
import xydesk.xy.i.ViewI;
import xydesk.xy.i.VoiceI;
import xydesk.xy.model.XYAppInfoInDesk;
import xydesk.xy.set.LoveAppAdapter;
import xydesk.xy.set.VoiceSetUI;
import xydesk.xy.utils.AppUtils;
import xydesk.xy.utils.Utils;
import xydesk.xy.view.ItemView;
import xydesk.xy.voice.VoiceData;
import xydesk.xy.voice.VoiceUtils;
import xydesk.xy.xydesk.R;

public class MainActivity extends XYBaseActivity {

    @Bind(R.id.add_app)
    ViewPager addApp;
    @Bind(R.id.love_app)
    GridView loveApp;
    LoveAppAdapter loveAppAdapter;
    public List<XYBaseFragment> fragments = new ArrayList<>();
    VoiceUtils voiceUtils;
    DeskDB deskDB;
    FragmentViewAdapter adapter;
    public static MainActivity instance;
    private List<XYAppInfoInDesk> bottomList = new ArrayList<>();
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case XYContant.DELETER_APP:
                    if (OneAppFragment.instance != null) {
                        OneAppFragment.instance.handler.sendEmptyMessage(XYContant.DELETER_APP);
                    }
                    if (TwoAppFragment.instance != null) {
                        TwoAppFragment.instance.handler.sendEmptyMessage(XYContant.DELETER_APP);
                    }
                    if (ThreeAppFragment.instance != null) {
                        ThreeAppFragment.instance.handler.sendEmptyMessage(XYContant.DELETER_APP);
                    }
                    if (FourAppFragment.instance != null) {
                        FourAppFragment.instance.handler.sendEmptyMessage(XYContant.DELETER_APP);
                    }
                    Utils.getInstance().toast("应用已删除");
                    break;
                case XYContant.REFRESH_BOTTOM_APP:
                    bottomList = deskDB.bottomAllApp();
                    loveAppAdapter.refresh(bottomList);
                    break;
            }
        }
    };

    @Override
    public void initView() {
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        instance = this;
        deskDB = new DeskDB(instance);
        fragments.add(new OneAppFragment());
        fragments.add(new TwoAppFragment());
        fragments.add(new ThreeAppFragment());
        fragments.add(new FourAppFragment());
        AppUtils.getInstance().PingApp(instance);
        /**Home键监听*/
        initHomeListen();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //更新联系人
        ContactManUtils.getPeopleInPhone(instance);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    //初始化数据
    @Override
    public void initData() {
        AppUtils.getInstance().getAllAppList(instance);
        AppUtils.getInstance().getAppU(instance);
        VoiceData.getInstance().addSysApp(instance);
        voiceUtils = new VoiceUtils(instance);
        deskDB.addAupdateBottomApp(instance);
        loveApp.setOnItemClickListener(bottomItemClick);
    }

    @Override
    public void setAdapter() {
        bottomList = deskDB.bottomAllApp();
        loveAppAdapter = new LoveAppAdapter(instance, bottomList);
        adapter = new FragmentViewAdapter(getSupportFragmentManager(), fragments);
        loveApp.setAdapter(loveAppAdapter);
        addApp.setAdapter(adapter);
    }

    AdapterView.OnItemClickListener bottomItemClick = new AdapterView.OnItemClickListener() {
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == XYContant.DELETER_APP) {
            handler.sendEmptyMessage(XYContant.DELETER_APP);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return KeyEvent.KEYCODE_BACK != keyCode && super.onKeyDown(keyCode, event);
    }

    @OnClick({R.id.all_app, R.id.up_ping, R.id.down_ping})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.up_ping:
                int cup = addApp.getCurrentItem();
                if (cup > 0 && cup < 4) {
                    addApp.setCurrentItem(cup - 1);
                } else {
                    addApp.setCurrentItem(0);
                }
                break;
            case R.id.down_ping:
                int cdown = addApp.getCurrentItem();
                if (cdown >= 0 && cdown < 3) {
                    addApp.setCurrentItem(cdown + 1);
                } else {
                    addApp.setCurrentItem(3);
                }
                break;
            case R.id.all_app:
                ItemView.getInstance().showLongView(instance, ItemView.getInstance().menu_click, new ViewI() {
                    @Override
                    public void click(View view, int itemPosition) {
                        Intent intent = new Intent();
                        switch ((String) view.getTag()) {
                            case XYContant.ALL_APP_IN_MENU:
                                intent.setClass(instance, AllAppShowUI.class);
                                break;
                            case XYContant.APP_SET_IN_MENU:
                                intent.setClass(instance, VoiceSetUI.class);
                                break;
                            case XYContant.CONTACT_NAME_SET:
                                intent.setClass(instance, AddContactNameUI.class);
                                break;
                        }
                        startActivity(intent);
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
                if (lastRec.length() > 2) {
                    if (lastRec.contains("打开") && !lastRec.contains("拨打")) {
                        VoiceData.getInstance().openApp(instance, lastRec);
                    } else if (lastRec.contains("拨打") && !lastRec.contains("打开")) {
                        if (ContactManUtils.allContact.isEmpty()) {
                            Utils.getInstance().toast("暂无联系人");
                            return;
                        }
                        String number = deskDB.getContactNum(lastRec);
                        if (!number.equals(XYContant.F)) {
                            ContactManUtils.callPhone(instance, number);
                            return;
                        }
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
                            Utils.getInstance().toast("无此联系人");
                        }
                    } else {
                        Utils.getInstance().toast("无法识别开头语请说打开或拨打");
                    }
                } else {
                    Utils.getInstance().toast("语句太短");
                }
            }
        });
    }

    //桌面默认
    private void initHomeListen() {
        //注册广播
        IntentFilter homeFilter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        registerReceiver(mHomeKeyEventReceiver, homeFilter);
    }

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
                    if (addApp != null) {
                        addApp.setCurrentItem(0);
                    }
                }
            }
        }
    };
}
