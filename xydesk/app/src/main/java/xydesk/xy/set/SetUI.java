package xydesk.xy.set;

import android.os.Bundle;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import xydesk.xy.base.XYBaseActivity;
import xydesk.xy.xydesk.R;

/**
 * Created by haizeiym
 * on 2016/9/29
 */
public class SetUI extends XYBaseActivity {
    Bundle appBundle;
    @Bind(R.id.app_info)
    TextView appInfo;

    @Override
    public void initView() {
        setContentView(R.layout.set_ui);
        ButterKnife.bind(this);
    }

    @Override
    public void initData() {
        appBundle = getIntent().getBundleExtra("appInfo");
        appInfo.setText(appBundle.getString("appName") + "\n" + "版本号:" + appBundle.getString("appVersion"));
    }
}
