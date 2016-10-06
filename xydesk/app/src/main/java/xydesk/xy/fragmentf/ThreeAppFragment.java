package xydesk.xy.fragmentf;

import android.content.Intent;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

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
public class ThreeAppFragment extends XYBaseFragment {
    GridView fragmentApp;
    public static ThreeAppFragment instance;
    XYFragmentAdapter xyFragmentAdapter;

    public ThreeAppFragment() {
        if (instance == null) {
            instance = ThreeAppFragment.this;
        }
        xyFragmentAdapter = new XYFragmentAdapter(MainActivity.instance, AppUtils.three_xyAppInfoInDesks);
        initHandler();
    }

    @Override
    public View initCreateView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.base_fragment, container, false);
        fragmentApp = (GridView) view.findViewById(R.id.app_list);
        setAdapter();
        Utils.getInstance().toast(getActivity(), "第三屏，当前屏幕共" + AppUtils.three_xyAppInfoInDesks.size() + "项");
        return view;
    }

    private void setAdapter() {
        xyFragmentAdapter = new XYFragmentAdapter(MainActivity.instance, AppUtils.three_xyAppInfoInDesks);
        fragmentApp.setAdapter(xyFragmentAdapter);
        fragmentApp.setOnItemClickListener(this);
        fragmentApp.setOnItemLongClickListener(this);
    }

    @Override
    public void itemClick(View view, int position) {
        AppUtils.getInstance().openApp(getActivity(), AppUtils.three_xyAppInfoInDesks.get(position).appPackageName);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == XYContant.DELETER_APP) {
            handler.sendEmptyMessage(XYContant.DELETER_APP);
        }
    }

    @Override
    public void longpressItem(View view, final int position) {
        ItemView.getInstance().showLongView(getActivity(), ItemView.getInstance().itemLong, new ViewI() {
            @Override
            public void click(View view, int itemPosition) {
                XYAppInfoInDesk xyAllAppModel = AppUtils.three_xyAppInfoInDesks.get(position);
                switch ((String) view.getTag()) {
                    case XYContant.DELE_APP_IN_FRAGMENT:
                        AppUtils.getInstance().deleAtFragment(getActivity(), xyAllAppModel.appPackageName);
                        handler.sendEmptyMessage(XYContant.DELETER_APP);
                        Utils.getInstance().toast(getActivity(), "删除成功");
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
                if (!AppUtils.getInstance().delePackageName.equals("")) {
                    DeskDB deskDB = new DeskDB(getActivity());
                    deskDB.deleApp(AppUtils.getInstance().delePackageName);
                    AppUtils.getInstance().delePackageName = "";
                }
                AppUtils.three_xyAppInfoInDesks = AppUtils.getInstance().getAllApp(getActivity(), XYContant.THREE_FRAGMENT);
                xyFragmentAdapter.refresh(AppUtils.three_xyAppInfoInDesks);
                MainActivity.instance.handler.sendEmptyMessage(XYContant.REFRESH_FRAGMENT);
                break;
        }
    }
}
