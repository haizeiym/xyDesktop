package xydesk.xy.appAll.ui;

import android.view.View;
import android.widget.TextView;

import xydesk.xy.base.XYBaseActivity;
import xydesk.xy.contant.XYContant;
import xydesk.xy.db.DeskDB;
import xydesk.xy.i.VoiceI;
import xydesk.xy.model.XYXFNameSetModel;
import xydesk.xy.utils.Utils;
import xydesk.xy.voice.VoiceUtils;
import xydesk.xy.xydesk.R;

/**
 * Created by haizeiym
 * on 2016/8/31
 */
public class AppXFNameSetUI extends XYBaseActivity {
    TextView app_xfName_set, sure, cancle;
    VoiceUtils voiceUtils;
    DeskDB deskDB;
    String packageName = XYContant.F;

    @Override
    public void initView() {
        setContentView(R.layout.app_xyname_set);
        app_xfName_set = (TextView) findViewById(R.id.app_xfname_set);
        sure = (TextView) findViewById(R.id.sure);
        cancle = (TextView) findViewById(R.id.cancle);
        app_xfName_set.setOnClickListener(this);
        sure.setOnClickListener(this);
        cancle.setOnClickListener(this);
        voiceUtils = new VoiceUtils(instance);
        deskDB = new DeskDB(instance);
        packageName = getIntent().getStringExtra(XYContant.ADD_APP_NAME);
    }

    @Override
    public void setOnclick(View v) {
        switch (v.getId()) {
            case R.id.app_xfname_set:
                voiceUtils.startVoice(new VoiceI() {
                    @Override
                    public void findApp(String lastRec) {
                        app_xfName_set.setText(lastRec);
                    }

                    @Override
                    public void findNum(String lastRec) {

                    }
                }, true);
                break;
            case R.id.sure:
                String setName = app_xfName_set.getText().toString();
                if (!setName.equals("点击添加语音名称")) {
                    XYXFNameSetModel xyxfNameSetModel = new XYXFNameSetModel();
                    xyxfNameSetModel.set_app_name = "打开" + setName;
                    xyxfNameSetModel.set_app_package_name = packageName;
                    deskDB.addSetAppName(xyxfNameSetModel);
                    Utils.getInstance().toast("已添加成功");
                    finish();
                } else {
                    Utils.getInstance().toast("名称为空");
                }
                break;
            case R.id.cancle:
                finish();
                break;
        }
    }
}
