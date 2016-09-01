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
import xydesk.xy.i.ViewI;
import xydesk.xy.model.XYAllAppModel;
import xydesk.xy.model.XYAppInfoInDesk;
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
                XYAllAppModel xyAllAppModel = xyAllAppModelList.get(position);
                switch ((String) view.getTag()) {
                    case XYContant.OPEN_APP:
                        AppUtils.getInstance().openApp(instance, xyAllAppModel.appPackageName);
                        break;
                    case XYContant.ADD_DESK_ONE:
                        if (deskDB.isExits(xyAllAppModel.appPackageName)) {
                            Utils.getInstance().toast("桌面已添加");
                        } else {
                            XYAppInfoInDesk xyAppInfoInDesk = new XYAppInfoInDesk();
                            xyAppInfoInDesk.appPackageName = xyAllAppModel.appPackageName;
                            xyAppInfoInDesk.appName = xyAllAppModel.appName;
                            xyAppInfoInDesk.appPonitParents = XYContant.ONE_FRAGMENT;
                            deskDB.addAppInfo(xyAppInfoInDesk);
                            MainActivity.instance.handler.sendEmptyMessage(XYContant.ADD_APP);
                        }
                        break;
                    case XYContant.ADD_DESK_TWO:

                        break;
                    case XYContant.ADD_DESK_THREE:

                        break;
                    case XYContant.ADD_DESK_FOUR:

                        break;
                    case XYContant.DELE_APP:
                        delePackageName = xyAllAppModel.appPackageName;
                        AppUtils.getInstance().delApp(instance, xyAllAppModel.appPackageName);
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
