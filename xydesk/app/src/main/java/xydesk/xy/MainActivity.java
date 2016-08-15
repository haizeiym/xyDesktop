package xydesk.xy;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;


import xydesk.xy.appAll.ui.AllAppShowUI;
import xydesk.xy.contant.XYContant;
import xydesk.xy.db.DeskDB;
import xydesk.xy.utils.AppUtils;
import xydesk.xy.utils.Utils;
import xydesk.xy.viewFragment.OneAppFragment;
import xydesk.xy.xydesk.R;

public class MainActivity extends FragmentActivity implements View.OnClickListener {
    TextView all_app, xyCall;
    OneAppFragment oneAppFragment;
    public static MainActivity instance;
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case XYContant.ADD_APP:
                    OneAppFragment.instance.handler.sendEmptyMessage(XYContant.ADD_APP);
                    Utils.getInstance().toast(instance, "已应用到添加桌面");
                    break;
                case XYContant.DELETER_APP:
                    OneAppFragment.instance.handler.sendEmptyMessage(XYContant.DELETER_APP);
                    Utils.getInstance().toast(instance, "应用已删除");
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
        xyCall.setOnClickListener(this);
        all_app.setOnClickListener(this);
        AppUtils.getInstance().getAllAppList(instance);
        AppUtils.getInstance().getAppU(instance);
        setDefaultFargment();
    }

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
                DeskDB deskDB = new DeskDB(instance);
                if (deskDB.isExits(AppUtils.getInstance().xyPakcageName)) {
                    AppUtils.getInstance().openApp(instance, AppUtils.getInstance().xyPakcageName);
                } else {
                    Intent systemPhoneNum = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"));
                    systemPhoneNum.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(systemPhoneNum);
                    Utils.getInstance().toast(instance, "暂未安装该应用，为您跳转至系统拨号界面");
                }
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
}
