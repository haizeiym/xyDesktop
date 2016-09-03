package xydesk.xy.viewFragment;

import android.content.Intent;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import xydesk.xy.MainActivity;
import xydesk.xy.base.XYBaseFragment;
import xydesk.xy.contant.XYContant;
import xydesk.xy.db.DeskDB;
import xydesk.xy.i.ViewI;
import xydesk.xy.model.XYAppInfoInDesk;
import xydesk.xy.utils.AppUtils;
import xydesk.xy.utils.Utils;
import xydesk.xy.view.ItemView;
import xydesk.xy.xydesk.R;

/**
 * Created by haizeiym
 * on 2016/8/8
 */
public class FourAppFragment extends XYBaseFragment {
    GridView fragmentApp;
    //显示第几屏
    TextView what_ottf;
    public static FourAppFragment instance;
    XYFragmentAdapter xyFragmentAdapter;

    @Override
    public void createInit() {
        if (instance == null) {
            instance = this;
        }
        xyFragmentAdapter = new XYFragmentAdapter(MainActivity.instance, AppUtils.four_xyAppInfoInDesks);
    }

    @Override
    public View initCreateView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.base_fragment, container, false);
        fragmentApp = (GridView) view.findViewById(R.id.app_list);
        what_ottf = (TextView) view.findViewById(R.id.what_ottf);
        what_ottf.setText("第四屏");
        setAdapter();
        return view;
    }

    private void setAdapter() {
        fragmentApp.setAdapter(xyFragmentAdapter);
        fragmentApp.setOnItemClickListener(this);
        fragmentApp.setOnItemLongClickListener(this);
    }

    @Override
    public void itemClick(View view, int position) {
        AppUtils.getInstance().openApp(getActivity(), AppUtils.four_xyAppInfoInDesks.get(position).appPackageName);
    }

    @Override
    public void longpressItem(View view, final int position) {
        ItemView.getInstance().showLongView(getActivity(), ItemView.getInstance().itemLong, new ViewI() {
            @Override
            public void click(View view, int itemPosition) {
                XYAppInfoInDesk xyAllAppModel = AppUtils.four_xyAppInfoInDesks.get(position);
                switch ((String) view.getTag()) {
                    case XYContant.DELE_APP_IN_FRAGMENT:
                        AppUtils.getInstance().deleAtFragment(getActivity(), xyAllAppModel.appPackageName);
                        handler.sendEmptyMessage(XYContant.DELETER_APP);
                        Utils.getInstance().toast("删除成功");
                        break;
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == XYContant.DELETER_APP) {
            handler.sendEmptyMessage(XYContant.DELETER_APP);
        }
    }

    @Override
    public void setHandler(Message msg) {
        switch (msg.what) {
            case XYContant.DELETER_APP:
            case XYContant.ADD_APP:
                if (!AppUtils.getInstance().delePackageName.equals("")) {
                    DeskDB deskDB = new DeskDB(getActivity());
                    deskDB.deleApp(AppUtils.getInstance().delePackageName);
                    AppUtils.getInstance().delePackageName = "";
                }
                AppUtils.four_xyAppInfoInDesks = AppUtils.getInstance().getAllApp(getActivity(), XYContant.FOUR_FRAGMENT);
                xyFragmentAdapter.refresh(AppUtils.four_xyAppInfoInDesks);
                break;
        }
    }
}
