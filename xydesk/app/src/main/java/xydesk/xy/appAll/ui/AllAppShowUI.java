package xydesk.xy.appAll.ui;

import android.content.Intent;
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
import xydesk.xy.fragmentf.TwoAppFragment;
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
    AllAppAdapter adapter;
    List<XYAllAppModel> xyModels;
    DeskDB deskDB;
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
        xyModels = AppUtils.getInstance().getAllAppList(instance);
        xyAllAppModelList = Utils.getInstance().filledChar(xyModels, characterParser);
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
                        startActivity(intentSet);
                        break;
                    case XYContant.DESK_APP:
                        ItemView.getInstance().showLongView(instance, ItemView.getInstance().deskApp, new ViewI() {
                            @Override
                            public void click(View view, int itemPosition) {
                                switch ((String) view.getTag()) {
                                    case XYContant.ADD_DESK_ONE:
                                        addAppToDesk(xyAllAppModel, XYContant.ONE_FRAGMENT);
                                        break;
                                    case XYContant.ADD_DESK_TWO:
                                        addAppToDesk(xyAllAppModel, XYContant.TWO_FRAGMENT);
                                        break;
                                    case XYContant.ADD_DESK_THREE:
                                        addAppToDesk(xyAllAppModel, XYContant.THREE_FRAGMENT);
                                        break;
                                    case XYContant.ADD_DESK_FOUR:
                                        addAppToDesk(xyAllAppModel, XYContant.FOUR_FRAGMENT);
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
    private void addAppToDesk(XYAllAppModel xyAllAppModel, String whatWhere) {
        if (deskDB.isExits(xyAllAppModel.appPackageName)) {
            Utils.getInstance().toast("桌面已添加");
        } else {
            if (AppUtils.two_xyAppInfoInDesks.size() >= 16) {
                Utils.getInstance().toast("屏幕空间不足");
            } else {
                XYAppInfoInDesk xyAppInfoInDesk = new XYAppInfoInDesk();
                xyAppInfoInDesk.appPackageName = xyAllAppModel.appPackageName;
                xyAppInfoInDesk.appName = xyAllAppModel.appName;
                xyAppInfoInDesk.appPonitParents = whatWhere;
                deskDB.addAppInfo(xyAppInfoInDesk);
                TwoAppFragment.instance.handler.sendEmptyMessage(XYContant.ADD_APP);
                Utils.getInstance().toast("应用已添加到桌面");
            }
        }
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
