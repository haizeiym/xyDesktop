package xydesk.xy;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.TextView;


import xydesk.xy.appAll.ui.AllAppShowUI;
import xydesk.xy.contant.XYContant;
import xydesk.xy.viewFragment.OneAppFragment;
import xydesk.xy.xydesk.R;

public class MainActivity extends FragmentActivity implements View.OnClickListener {
    TextView test;
    OneAppFragment oneAppFragment;
    public static MainActivity instance;
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //activity跟fragment通讯
            OneAppFragment.instance.handler.sendEmptyMessage(XYContant.ADD_APP);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        instance = this;
        test = (TextView) findViewById(R.id.test);
        test.setOnClickListener(this);
        FragmentManager fm = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = fm.beginTransaction();
        oneAppFragment = new OneAppFragment();
        transaction.replace(R.id.add_app, oneAppFragment);
        transaction.commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.test:
                Intent intent = new Intent(MainActivity.this, AllAppShowUI.class);
                startActivity(intent);
                break;
        }
    }

}
