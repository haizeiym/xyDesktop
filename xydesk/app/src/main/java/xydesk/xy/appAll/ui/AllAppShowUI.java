package xydesk.xy.appAll.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ListView;

import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import xydesk.xy.MainActivity;
import xydesk.xy.appAll.adapter.AllAppAdapter;
import xydesk.xy.base.XYBaseActivity;
import xydesk.xy.contant.XYContant;
import xydesk.xy.db.DeskDB;
import xydesk.xy.i.ViewI;
import xydesk.xy.model.XYAllAppModel;
import xydesk.xy.model.XYAppInfoInDesk;
import xydesk.xy.set.SetUI;
import xydesk.xy.utils.AppUtils;
import xydesk.xy.utils.CharacterParser;
import xydesk.xy.utils.PinyinComparator;
import xydesk.xy.utils.Utils;
import xydesk.xy.view.ItemView;
import xydesk.xy.xydesk.R;

/**
 * Created by haizeiym
 * on 2016/7/27
 */
public class AllAppShowUI extends XYBaseActivity {
    @Bind(R.id.all_app)
    ListView allApp;
    private List<XYAllAppModel> xyAllAppModelList;
    //根据拼音来排列ListView里面的数据类
    private PinyinComparator pinyinComparator;
    //汉字转拼音的类
    private CharacterParser characterParser;
    private AllAppAdapter adapter;
    private DeskDB deskDB;
    //要删除app的包名
    private String delePackageName;

    @Override
    public void initView() {
        setContentView(R.layout.allsome_show_ui);
        ButterKnife.bind(this);
        pinyinComparator = new PinyinComparator();
        characterParser = CharacterParser.getInstance();
        deskDB = new DeskDB(instance);
    }

    @Override
    public void initData() {
        setAppData();
        adapter = new AllAppAdapter(instance, xyAllAppModelList);
    }

    private void setAppData() {
        xyAllAppModelList = Utils.getInstance().filledChar(AppUtils.getInstance().getAllAppList(instance), characterParser);
        Collections.sort(xyAllAppModelList, pinyinComparator);
    }

    @Override
    public void setAdapter() {
        allApp.setAdapter(adapter);
    }

    @OnItemClick(R.id.all_app)
    public void onItemClick(View view, final int position) {
        ItemView.getInstance().showLongView(instance, ItemView.getInstance().itemAll, new ViewI() {
            @Override
            public void click(View view, int itemPosition) {
                final XYAllAppModel xyAllAppModel = xyAllAppModelList.get(position);
                switch ((String) view.getTag()) {
                    case XYContant.OPEN_APP:
                        AppUtils.getInstance().openApp(instance, xyAllAppModel.appPackageName);
                        break;
                    case XYContant.APP_INFO:
                        Intent intentSet = new Intent(instance, SetUI.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("appVersion", xyAllAppModel.appVersion);
                        bundle.putString("appName", xyAllAppModel.appName);
                        bundle.putString("appPackageName", xyAllAppModel.appPackageName);
                        intentSet.putExtra("appInfo", bundle);
                        startActivity(intentSet);
                        break;
                    case XYContant.DESK_APP:
                        ItemView.getInstance().showLongView(instance, ItemView.getInstance().deskApp, new ViewI() {
                            @Override
                            public void click(View view, int itemPosition) {
                                switch ((String) view.getTag()) {
                                    case XYContant.ADD_DESK_ONE:
                                        addAppToDesk(xyAllAppModel, XYContant.ONE_FRAGMENT, MainActivity.instance.oneAppFragment.handler);
                                        break;
                                    case XYContant.ADD_DESK_TWO:
                                        addAppToDesk(xyAllAppModel, XYContant.TWO_FRAGMENT, MainActivity.instance.twoAppFragment.handler);
                                        break;
                                    case XYContant.ADD_DESK_THREE:
                                        addAppToDesk(xyAllAppModel, XYContant.THREE_FRAGMENT, MainActivity.instance.threeAppFragment.handler);
                                        break;
                                    case XYContant.ADD_DESK_FOUR:
                                        addAppToDesk(xyAllAppModel, XYContant.FOUR_FRAGMENT, MainActivity.instance.fourAppFragment.handler);
                                        break;
                                }
                            }
                        });
                        break;
                    case XYContant.BOTTOM_APP:
                        ItemView.getInstance().showLongView(instance, ItemView.getInstance().bottomApp, new ViewI() {
                            @Override
                            public void click(View view, int itemPosition) {
                                switch ((String) view.getTag()) {
                                    case XYContant.BOTTOM_ONE:
                                        addAppToBottom(xyAllAppModel, "1");
                                        break;
                                    case XYContant.BOTTOM_TWO:
                                        addAppToBottom(xyAllAppModel, "2");
                                        break;
                                    case XYContant.BOTTOM_THREE:
                                        addAppToBottom(xyAllAppModel, "3");
                                        break;
                                    case XYContant.BOTTOM_FOUR:
                                        addAppToBottom(xyAllAppModel, "4");
                                        break;
                                    case XYContant.BOTTOM_FIVE:
                                        addAppToBottom(xyAllAppModel, "5");
                                        break;
                                }
                            }
                        });
                        break;
                    case XYContant.DELE_APP:
                        delePackageName = xyAllAppModel.appPackageName;
                        AppUtils.getInstance().delApp(xyAllAppModel.appPackageName);
                        break;
                    case XYContant.XFNAME:
                        Intent intent = new Intent(instance, NameSetUI.class);
                        intent.putExtra(XYContant.IS_VOICE, true);
                        intent.putExtra(XYContant.NAME_SET, xyAllAppModel.appPackageName);
                        startActivity(intent);
                        break;
                }
            }
        });
    }

    //添加到那个FRAGMENT
    private void addAppToDesk(XYAllAppModel xyAllAppModel, String whatWhere, Handler handler) {
        if (deskDB.isExits(xyAllAppModel.appPackageName)) {
            Utils.getInstance().toast(instance, "桌面已添加");
        } else {
            if (pingLeangth(whatWhere) >= 16) {
                Utils.getInstance().toast(instance, "屏幕空间不足");
            } else {
                XYAppInfoInDesk xyAppInfoInDesk = new XYAppInfoInDesk();
                xyAppInfoInDesk.appPackageName = xyAllAppModel.appPackageName;
                xyAppInfoInDesk.appName = xyAllAppModel.appName;
                xyAppInfoInDesk.appPonitParents = whatWhere;
                deskDB.addAppInfo(xyAppInfoInDesk);
                handler.sendEmptyMessage(XYContant.ADD_APP);
                Utils.getInstance().toast(instance, "应用已添加到桌面");
            }
        }
        addFragment(whatWhere);
    }

    //屏幕APP数量
    private int pingLeangth(String whatWhere) {
        int l = 0;
        switch (whatWhere) {
            case XYContant.ONE_FRAGMENT:
                l = AppUtils.getInstance().getAllApp(MainActivity.instance, XYContant.ONE_FRAGMENT).size();
                break;

            case XYContant.TWO_FRAGMENT:
                l = AppUtils.getInstance().getAllApp(MainActivity.instance, XYContant.TWO_FRAGMENT).size();
                break;

            case XYContant.THREE_FRAGMENT:
                l = AppUtils.getInstance().getAllApp(MainActivity.instance, XYContant.THREE_FRAGMENT).size();
                break;

            case XYContant.FOUR_FRAGMENT:
                l = AppUtils.getInstance().getAllApp(MainActivity.instance, XYContant.FOUR_FRAGMENT).size();
                break;
            
        }
        return l;
    }

    //添加屏幕
    private void addFragment(String whatWhere) {
        Message msg = MainActivity.instance.handler.obtainMessage();
        msg.what = XYContant.REFRESH_FRAGMENT;
        msg.obj = whatWhere;
        MainActivity.instance.handler.sendMessage(msg);
    }

    //添加到托盘位置
    private void addAppToBottom(XYAllAppModel xyAllAppModel, String position) {
        XYAppInfoInDesk xyAppInfoInDesk = new XYAppInfoInDesk();
        xyAppInfoInDesk.appPackageName = xyAllAppModel.appPackageName;
        xyAppInfoInDesk.appName = xyAllAppModel.appName;
        xyAppInfoInDesk.appBottomPosition = position;
        deskDB.updateBottomApp(xyAppInfoInDesk);
        MainActivity.instance.handler.sendEmptyMessage(XYContant.REFRESH_BOTTOM_APP);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == XYContant.DELETER_APP) {
            handler.sendEmptyMessage(XYContant.DELETER_APP);
        }
    }

    @Override
    public void handler(Message msg) {
        switch (msg.what) {
            case XYContant.DELETER_APP:
                setAppData();
                adapter.refresh(xyAllAppModelList);
                deskDB.deleApp(delePackageName);
                MainActivity.instance.handler.sendEmptyMessage(XYContant.DELETER_APP);
                break;
        }
    }
}
