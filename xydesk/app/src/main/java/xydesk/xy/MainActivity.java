package xydesk.xy;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import xydesk.xy.appAll.ui.AllAppShowUI;
import xydesk.xy.contant.XYContant;
import xydesk.xy.i.VoiceI;
import xydesk.xy.utils.AppUtils;
import xydesk.xy.utils.Utils;
import xydesk.xy.viewFragment.OneAppFragment;
import xydesk.xy.voice.VoiceData;
import xydesk.xy.voice.VoiceUtils;
import xydesk.xy.xydesk.R;

public class MainActivity extends FragmentActivity implements View.OnClickListener {
    TextView all_app, xyCall, add_three;
    OneAppFragment oneAppFragment;
    VoiceUtils voiceUtils;
    public static MainActivity instance;
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case XYContant.ADD_APP:
                    OneAppFragment.instance.handler.sendEmptyMessage(XYContant.ADD_APP);
                    Utils.getInstance().toast("已应用到添加桌面");
                    break;
                case XYContant.DELETER_APP:
                    OneAppFragment.instance.handler.sendEmptyMessage(XYContant.DELETER_APP);
                    Utils.getInstance().toast("应用已删除");
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        instance = this;
        all_app = (TextView) findViewById(R.id.all_app);
        xyCall = (TextView) findViewById(R.id.add_four);
        add_three = (TextView) findViewById(R.id.add_three);
        xyCall.setOnClickListener(this);
        all_app.setOnClickListener(this);
        add_three.setOnClickListener(this);
        initData();
    }

    /**
     * 清除默认桌面（采用先设置一个空的桌面为默认然后在将该空桌面禁用的方式来实现）
     * 默认桌面设置
     *
     * @param
     *//*
    public void clearDefaultLauncher() {
        PackageManager pm = getPackageManager();
        String pn = getPackageName();
        String hn = MainActivity.class.getName();
        ComponentName mhCN = new ComponentName(pn, hn);
        Intent homeIntent = new Intent("android.intent.action.MAIN");
        homeIntent.addCategory("android.intent.category.HOME");
        homeIntent.addCategory("android.intent.category.DEFAULT");
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        pm.setComponentEnabledSetting(mhCN, 1, 1);
        startActivity(homeIntent);
        pm.setComponentEnabledSetting(mhCN, 0, 1);
    }*/

    //初始化数据
    private void initData() {
        AppUtils.getInstance().getAllAppList(instance);
        AppUtils.getInstance().getAppU(instance);
        VoiceData.getInstance().addSysApp(instance);
        setDefaultFargment();
        voiceUtils = new VoiceUtils(instance);
    }

    //默认Fragment
    private void setDefaultFargment() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        oneAppFragment = new OneAppFragment();
        transaction.replace(R.id.add_app, oneAppFragment);
        transaction.commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.all_app:
                Intent intent = new Intent(instance, AllAppShowUI.class);
                startActivity(intent);
                break;
            case R.id.add_four:
                //语音拨打电话
                Utils.getInstance().toast("形式待定");
                break;
            case R.id.add_three:
                voiceUtils.startVoice(new VoiceI() {
                    @Override
                    public void findApp(String lastRec) {
                        add_three.setText("记录：" + lastRec);
                        VoiceData.getInstance().openApp(instance, lastRec);
                    }

                    @Override
                    public void findNum(String lastRec) {

                    }
                }, true);
                break;
        }
    }

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
}
