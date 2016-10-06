package xydesk.xy.set;

import android.view.View;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import xydesk.xy.MainActivity;
import xydesk.xy.base.XYBaseActivity;
import xydesk.xy.contant.XYContant;
import xydesk.xy.db.DeskDB;
import xydesk.xy.model.XYAppInfoInDesk;
import xydesk.xy.utils.Utils;
import xydesk.xy.xydesk.R;

/**
 * Created by haizeiym
 * on 2016/9/30
 */
public class VoiceSetUI extends XYBaseActivity {
    @Bind(R.id.show_voice)
    TextView showVoice;
    DeskDB deskDB;

    @Override
    public void initView() {
        setContentView(R.layout.voice_set_ui);
        ButterKnife.bind(this);
        deskDB = new DeskDB(instance);
        showVoice.setOnClickListener(setVoice);
    }

    View.OnClickListener setVoice = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            XYAppInfoInDesk xyAppInfoInDesk = new XYAppInfoInDesk();
            xyAppInfoInDesk.appBottomPosition = 3 + "";
            xyAppInfoInDesk.appName = "语音";
            xyAppInfoInDesk.appPackageName = XYContant.F;
            deskDB.updateBottomApp(xyAppInfoInDesk);
            MainActivity.instance.handler.sendEmptyMessage(XYContant.REFRESH_BOTTOM_APP);
            Utils.getInstance().toast(instance,"设置成功");
            finish();
        }
    };
}
