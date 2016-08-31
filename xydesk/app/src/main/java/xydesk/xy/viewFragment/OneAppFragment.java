package xydesk.xy.viewFragment;

import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.List;

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
 * on 2016/7/28
 */
public class OneAppFragment extends XYBaseFragment {
    GridView fragmentApp;
    List<XYAppInfoInDesk> xyAppInfoInDesks;
    public static OneAppFragment instance;
    XYFragmentAdapter xyFragmentAdapter;

    @Override
    public void createInit() {
        if (instance == null) {
            instance = this;
        }
    }

    @Override
    public View initCreateView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.base_fragment, container, false);
        fragmentApp = (GridView) view.findViewById(R.id.app_list);
        xyAppInfoInDesks = AppUtils.getInstance().getAllApp(getActivity(), XYContant.ONE_FRAGMENT);
        xyFragmentAdapter = new XYFragmentAdapter(getActivity(), xyAppInfoInDesks);
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
        AppUtils.getInstance().openApp(getActivity(), xyAppInfoInDesks.get(position).appPackageName);
    }

    @Override
    public void longpressItem(View view, final int position) {
        ItemView.getInstance().showLongView(getActivity(), ItemView.getInstance().itemLong, new ViewI() {
            @Override
            public void click(View view, int itemPosition) {
                XYAppInfoInDesk xyAllAppModel = xyAppInfoInDesks.get(position);
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
    public void setHandler(Message msg) {
        switch (msg.what) {
            case XYContant.DELETER_APP:
            case XYContant.ADD_APP:
                if (AppUtils.getInstance().delePackageName.equals("")) {
                    DeskDB deskDB = new DeskDB(getActivity());
                    deskDB.deleApp(AppUtils.getInstance().delePackageName);
                    AppUtils.getInstance().delePackageName = "";
                }
                xyAppInfoInDesks = AppUtils.getInstance().getAllApp(getActivity(), XYContant.ONE_FRAGMENT);
                xyFragmentAdapter.refresh(xyAppInfoInDesks);
                break;
        }
    }
}
