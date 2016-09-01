package xydesk.xy;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import xydesk.xy.appAll.ui.AllAppShowUI;
import xydesk.xy.base.XYBaseActivity;
import xydesk.xy.contact.AddContactNameUI;
import xydesk.xy.contact.ContactManUtils;
import xydesk.xy.contant.XYContant;
import xydesk.xy.i.VoiceI;
import xydesk.xy.utils.AppUtils;
import xydesk.xy.utils.Utils;
import xydesk.xy.viewFragment.OneAppFragment;
import xydesk.xy.voice.VoiceData;
import xydesk.xy.voice.VoiceUtils;
import xydesk.xy.xydesk.R;

public class MainActivity extends XYBaseActivity {
    @Bind(R.id.up_ping)
    TextView upPing;
    @Bind(R.id.down_ping)
    TextView downPing;
    @Bind(R.id.add_two)
    TextView addTwo;
    @Bind(R.id.add_one)
    TextView addOne;
    @Bind(R.id.add_four)
    TextView addFour;
    @Bind(R.id.all_app)
    TextView allApp;
    @Bind(R.id.add_three)
    TextView addThree;

    VoiceUtils voiceUtils;
    OneAppFragment oneAppFragment;
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
    public void initView() {
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        instance = this;
    }

    @Override
    protected void onResume() {
        super.onResume();
        ContactManUtils.getPeopleInPhone(instance);
    }

    //初始化数据
    @Override
    public void initData() {
        setDefaultFargment();
        AppUtils.getInstance().getAllAppList(instance);
        AppUtils.getInstance().getAppU(instance);
        VoiceData.getInstance().addSysApp(instance);
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

    @OnClick({R.id.add_four, R.id.add_three, R.id.all_app})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.all_app:
                Intent intent = new Intent(instance, AllAppShowUI.class);
                startActivity(intent);
                break;
            case R.id.add_four:
                //语音拨打电话
                Intent intent2 = new Intent(instance, AddContactNameUI.class);
                startActivity(intent2);
                break;
            case R.id.add_three:
                voiceUtils.startVoice(new VoiceI() {
                    @Override
                    public void voiceResult(String lastRec) {
                        addThree.setText("记录：" + lastRec);
                        if (lastRec.length() > 2) {
                            if (lastRec.contains("打开") && !lastRec.contains("拨打")) {
                                VoiceData.getInstance().openApp(instance, lastRec);
                            } else if (lastRec.contains("拨打") && !lastRec.contains("打开")) {
                                if (ContactManUtils.allContact.isEmpty()) {
                                    Utils.getInstance().toast("暂无联系人");
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
                break;
        }
    }
}
