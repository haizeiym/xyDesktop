package xydesk.xy.appAll.ui;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
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
public class NameSetUI extends XYBaseActivity {
    @Bind(R.id.app_xfname_set)
    TextView appXfnameSet;
    @Bind(R.id.sure)
    TextView sure;
    @Bind(R.id.cancle)
    TextView cancle;
    VoiceUtils voiceUtils;
    DeskDB deskDB;
    String name_set = XYContant.F;
    boolean isVoice = false;
    @Bind(R.id.name_set)
    EditText nameSet;

    @Override
    public void initView() {
        setContentView(R.layout.app_xyname_set);
        ButterKnife.bind(this);
        isVoice = getIntent().getBooleanExtra(XYContant.IS_VOICE, false);
        voiceUtils = new VoiceUtils(instance);
        deskDB = new DeskDB(instance);
        name_set = getIntent().getStringExtra(XYContant.NAME_SET);
        nameSet.addTextChangedListener(textWatcher);
    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.toString().isEmpty()) {
                appXfnameSet.setText("点击添加语音名称");
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            
        }
    };

    @OnClick({R.id.app_xfname_set, R.id.sure, R.id.cancle})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.app_xfname_set:
                voiceUtils.startVoice(new VoiceI() {
                    @Override
                    public void voiceResult(String lastRec) {
                        appXfnameSet.setText(lastRec);
                    }
                });
                break;
            case R.id.sure:
                String btn = appXfnameSet.getText().toString();
                String edt = nameSet.getText().toString();
                String name = null;
                if (edt.isEmpty() && !btn.equals("点击添加语音名称")) {
                    name = btn;
                } else if (!edt.isEmpty() && btn.equals("点击添加语音名称")) {
                    name = edt;
                }
                if (!name.equals("点击添加语音名称") || !edt.isEmpty()) {
                    XYXFNameSetModel xyxfNameSetModel = new XYXFNameSetModel();
                    if (isVoice) {
                        xyxfNameSetModel.set_app_name = "打开" + name;
                        xyxfNameSetModel.set_app_package_name = name_set;
                    } else {
                        xyxfNameSetModel.set_contact_name = "拨打" + name;
                        xyxfNameSetModel.set_contact_number = name_set;
                    }
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
