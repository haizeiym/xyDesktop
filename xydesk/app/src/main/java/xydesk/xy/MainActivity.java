package xydesk.xy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
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
import xydesk.xy.fragmentf.FragmentViewAdapter;
import xydesk.xy.fragmentf.AppFragment;
import xydesk.xy.i.ViewI;
import xydesk.xy.i.VoiceI;
import xydesk.xy.model.XYAppInfoInDesk;
import xydesk.xy.set.LoveAppAdapter;
import xydesk.xy.set.VoiceSetUI;
import xydesk.xy.utils.AppUtils;
import xydesk.xy.utils.Utils;
import xydesk.xy.view.ItemView;
import xydesk.xy.view.NoPreloadViewPager;
import xydesk.xy.voice.VoiceData;
import xydesk.xy.voice.VoiceUtils;
import xydesk.xy.xydesk.R;

public class MainActivity extends XYBaseActivity {

    @Bind(R.id.add_app)
    NoPreloadViewPager addApp;
    @Bind(R.id.love_app)
    GridView loveApp;
    LoveAppAdapter loveAppAdapter;
    public List<XYBaseFragment> fragments = new ArrayList<>();
    public AppFragment oneAppFragment, twoAppFragment, threeAppFragment, fourAppFragment;
    private VoiceUtils voiceUtils;
    private DeskDB deskDB;
    private FragmentViewAdapter fragmentAdapter;
    public static MainActivity instance;
    public List<XYAppInfoInDesk> bottomList = new ArrayList<>();
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case XYContant.REFRESH_BOTTOM_APP:
                    bottomList = deskDB.bottomAllApp();
                    loveAppAdapter.refresh(bottomList);
                    break;
                case XYContant.REFRESH_FRAGMENT:
                    String whatWhere = (String) msg.obj;
                    removeFragment(whatWhere);
                    break;
            }
        }
    };

    //移除fragment
    private void removeFragment(String whatWhere) {
        switch (whatWhere) {
            case XYContant.ONE_FRAGMENT:
                if (AppUtils.getInstance().getAllApp(instance, XYContant.ONE_FRAGMENT).size() == 0) {
                    fragments.remove(oneAppFragment);
                }
                break;
            case XYContant.TWO_FRAGMENT:
                if (AppUtils.getInstance().getAllApp(instance, XYContant.TWO_FRAGMENT).size() == 0) {
                    fragments.remove(twoAppFragment);
                }
                break;

            case XYContant.THREE_FRAGMENT:
                if (AppUtils.getInstance().getAllApp(instance, XYContant.THREE_FRAGMENT).size() == 0) {
                    fragments.remove(threeAppFragment);
                }
                break;

            case XYContant.FOUR_FRAGMENT:
                if (AppUtils.getInstance().getAllApp(instance, XYContant.FOUR_FRAGMENT).size() == 0) {
                    fragments.remove(fourAppFragment);
                }
                break;
        }
        fragmentAdapter.refreshFragment(fragments);
    }

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
        loveApp.setOnItemClickListener(bottomItemClick);
        addFragment();
    }

    //初始化时添加fragment
    private void addFragment() {
        List<XYAppInfoInDesk> xyAppInfoInDeskList_one = AppUtils.getInstance().getAllApp(MainActivity.instance, XYContant.ONE_FRAGMENT);
        List<XYAppInfoInDesk> xyAppInfoInDeskList_two = AppUtils.getInstance().getAllApp(MainActivity.instance, XYContant.TWO_FRAGMENT);
        List<XYAppInfoInDesk> xyAppInfoInDeskList_three = AppUtils.getInstance().getAllApp(MainActivity.instance, XYContant.THREE_FRAGMENT);
        List<XYAppInfoInDesk> xyAppInfoInDeskList_four = AppUtils.getInstance().getAllApp(MainActivity.instance, XYContant.FOUR_FRAGMENT);
        oneAppFragment = new AppFragment(xyAppInfoInDeskList_one, 1, XYContant.ONE_FRAGMENT);
        twoAppFragment = new AppFragment(xyAppInfoInDeskList_two, 2, XYContant.TWO_FRAGMENT);
        threeAppFragment = new AppFragment(xyAppInfoInDeskList_three, 3, XYContant.THREE_FRAGMENT);
        fourAppFragment = new AppFragment(xyAppInfoInDeskList_four, 4, XYContant.FOUR_FRAGMENT);
        if (xyAppInfoInDeskList_one.size() > 0) {
            fragments.add(oneAppFragment);
        }
        if (xyAppInfoInDeskList_two.size() > 0) {
            fragments.add(twoAppFragment);
        }
        if (xyAppInfoInDeskList_three.size() > 0) {
            fragments.add(threeAppFragment);
        }
        if (xyAppInfoInDeskList_four.size() > 0) {
            fragments.add(fourAppFragment);
        }
    }

    @Override
    public void setAdapter() {
        bottomList = deskDB.bottomAllApp();
        loveAppAdapter = new LoveAppAdapter(instance, bottomList);
        loveApp.setAdapter(loveAppAdapter);
        fragmentAdapter = new FragmentViewAdapter(getSupportFragmentManager(), fragments);
        addApp.setAdapter(fragmentAdapter);
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return KeyEvent.KEYCODE_BACK != keyCode && super.onKeyDown(keyCode, event);
    }

    @OnClick({R.id.all_app, R.id.up_ping, R.id.down_ping})
    public void onClick(View view) {
        int allFragment = fragments.size();
        switch (view.getId()) {
            case R.id.up_ping:
                int cup = addApp.getCurrentItem();
                if (cup >= 0 && cup < allFragment) {
                    addApp.setCurrentItem(cup - 1);
                } else {
                    addApp.setCurrentItem(0);
                }
                break;
            case R.id.down_ping:
                int cdown = addApp.getCurrentItem();
                if (cdown >= 0 && cdown < allFragment) {
                    addApp.setCurrentItem(cdown + 1);
                } else {
                    addApp.setCurrentItem(allFragment - 1);
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
                            Utils.getInstance().toast(instance, "暂无联系人");
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
                            Utils.getInstance().toast(instance, "无此联系人");
                        }
                    } else {
                        Utils.getInstance().toast(instance, "无法识别开头语请说打开或拨打");
                    }
                } else {
                    Utils.getInstance().toast(instance, "语句太短");
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
