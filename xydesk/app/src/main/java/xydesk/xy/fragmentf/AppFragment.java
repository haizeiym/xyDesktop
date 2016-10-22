package xydesk.xy.fragmentf;

import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.io.Serializable;
import java.util.List;

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
 * on 2016/7/28
 */
public class AppFragment extends XYBaseFragment {
    private GridView fragmentApp;
    private XYFragmentAdapter xyFragmentAdapter;
    private List<XYAppInfoInDesk> xyAppInfoInDeskList;
    private int position;
    private String whatFragment;

    public AppFragment() {
        initHandler();
        xyFragmentAdapter = new XYFragmentAdapter();
    }

    /*public AppFragment(List<XYAppInfoInDesk> xyAppInfoInDeskList, int position, String whatFragment) {
        this.xyAppInfoInDeskList = xyAppInfoInDeskList;
        this.position = position;
        this.whatFragment = whatFragment;
        xyFragmentAdapter = new XYFragmentAdapter(MainActivity.instance, xyAppInfoInDeskList);
    }*/

    public static AppFragment newInstance(List<XYAppInfoInDesk> xyAppInfoInDeskList, int position, String whatFragment) {
        AppFragment appFragment = new AppFragment();
        Bundle args = new Bundle();
        args.putSerializable(XYContant.ValuesToFragment.KEY_LIST, (Serializable) xyAppInfoInDeskList);
        args.putInt(XYContant.ValuesToFragment.KEY_INT, position);
        args.putString(XYContant.ValuesToFragment.KEY_WHAT, whatFragment);
        appFragment.setArguments(args);
        return appFragment;
    }

    @Override
    public void createInit() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            xyAppInfoInDeskList = (List<XYAppInfoInDesk>) bundle.getSerializable(XYContant.ValuesToFragment.KEY_LIST);
            position = bundle.getInt(XYContant.ValuesToFragment.KEY_INT);
            whatFragment = bundle.getString(XYContant.ValuesToFragment.KEY_WHAT);
            xyFragmentAdapter = null;
            xyFragmentAdapter = new XYFragmentAdapter(MainActivity.instance, xyAppInfoInDeskList);
        }
       /* xyAppInfoInDeskList = AppUtils.getInstance().getAllApp(getActivity(), whatFragment);*/
    }

    @Override
    public View initCreateView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.base_fragment, container, false);
        fragmentApp = (GridView) view.findViewById(R.id.app_list);
        setAdapter();
        Utils.getInstance().toast(getActivity(), "第" + position + "屏，当前屏幕共" + xyAppInfoInDeskList.size() + "项");
        return view;
    }

    private void setAdapter() {
        fragmentApp.setAdapter(xyFragmentAdapter);
        fragmentApp.setOnItemClickListener(this);
        fragmentApp.setOnItemLongClickListener(this);
    }

    @Override
    public void itemClick(View view, int position) {
        try {
            AppUtils.getInstance().openApp(getActivity(), xyAppInfoInDeskList.get(position).appPackageName);
        } catch (Exception e) {
            e.printStackTrace();
            Utils.getInstance().toast(getActivity(), "异常退出");
            System.exit(0);
        }
    }

    @Override
    public void longpressItem(View view, final int position) {
        ItemView.getInstance().showLongView(getActivity(), ItemView.getInstance().itemLong, new ViewI() {
            @Override
            public void click(View view, int itemPosition) {
                try {
                    XYAppInfoInDesk xyAllAppModel = xyAppInfoInDeskList.get(position);
                    switch ((String) view.getTag()) {
                        case XYContant.LongPressItem.DELE_APP_IN_FRAGMENT:
                            AppUtils.getInstance().deleAtFragment(getActivity(), xyAllAppModel.appPackageName);
                            handler.sendEmptyMessage(XYContant.XYContants.DELETER_APP);
                            Utils.getInstance().toast(getActivity(), "删除成功");
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Utils.getInstance().toast(getActivity(), "异常退出");
                    System.exit(0);
                }
            }
        });
    }

    @Override
    public void setHandler(Message msg) {
        switch (msg.what) {
            case XYContant.XYContants.DELETER_APP:
            case XYContant.XYContants.ADD_APP:
                if (!AppUtils.getInstance().delePackageName.equals("")) {
                    DeskDB deskDB = new DeskDB(getActivity());
                    deskDB.deleApp(AppUtils.getInstance().delePackageName);
                    AppUtils.getInstance().delePackageName = "";
                }
                xyAppInfoInDeskList = AppUtils.getInstance().getAllApp(getActivity(), whatFragment);
                refreshData(xyAppInfoInDeskList);
                if (xyAppInfoInDeskList.size() == 0) {
                    Message message = MainActivity.instance.handler.obtainMessage();
                    message.what = XYContant.XYContants.REFRESH_FRAGMENT;
                    message.obj = whatFragment;
                    MainActivity.instance.handler.sendMessage(message);
                }
                break;
        }
    }

    //数据刷新
    public void refreshData(List<XYAppInfoInDesk> xyAppInfoInDeskList) {
        xyFragmentAdapter.refresh(xyAppInfoInDeskList);
        if (xyAppInfoInDeskList.size() == 1 && !whatFragment.isEmpty()) {
            handler.sendEmptyMessage(XYContant.XYContants.ADD_APP);
        }
    }
}
