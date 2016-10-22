package xydesk.xy.voice;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import java.util.HashMap;
import java.util.Map;

import xydesk.xy.appAll.ui.AllAppShowUI;
import xydesk.xy.contant.XYContant;
import xydesk.xy.db.DeskDB;
import xydesk.xy.model.XYXFNameSetModel;
import xydesk.xy.utils.AppUtils;
import xydesk.xy.utils.Utils;

/**
 * Created by haizeiym
 * on 2016/8/30
 */
public class VoiceData {
    private static VoiceData instance;

    public static VoiceData getInstance() {
        if (instance == null) {
            instance = new VoiceData();
        }
        return instance;
    }

    private HashMap<String, String> initVoiceData() {
        HashMap<String, String> setApp = new HashMap<>();
        String OPEN = "打开";
        setApp.put(OPEN + "1", "com.tencent.mobileqq");
        setApp.put(OPEN + "一", "com.tencent.mobileqq");
        setApp.put(OPEN + "2", "com.tencent.mm");
        setApp.put(OPEN + "二", "com.tencent.mm");
        setApp.put(OPEN + "儿", "com.tencent.mm");
        setApp.put(OPEN + "3", "com.google.android.marvin.talkback8");
        setApp.put(OPEN + "三", "com.google.android.marvin.talkback8");
        setApp.put(OPEN + "伞", "com.google.android.marvin.talkback8");
        setApp.put(OPEN + "散", "com.google.android.marvin.talkback8");
        return setApp;
    }

    //添加默认名称的APP
    public void addSysApp(Context context) {
        DeskDB deskDB = new DeskDB(context);
        for (String s : initVoiceData().keySet()) {
            XYXFNameSetModel xyxfNameSetModel = new XYXFNameSetModel();
            xyxfNameSetModel.set_app_name = s;
            xyxfNameSetModel.set_app_package_name = initVoiceData().get(s);
            deskDB.addSetAppName(xyxfNameSetModel);
        }
    }

    public void openApp(Activity activity, String userString) {
        //打开全部应用页面
        if (userString.contains("全部应用")) {
            Intent intent = new Intent(activity, AllAppShowUI.class);
            activity.startActivity(intent);
            return;
        }
        //打开应用
        DeskDB deskDB = new DeskDB(activity);
        String packageName = deskDB.getAppPackageName(userString);
        if (!packageName.equals(XYContant.XYContants.F)) {
            AppUtils.getInstance().openApp(activity, packageName);
        } else {
            Map<String, String> appAll = AppUtils.allAppName;
            if (!appAll.isEmpty()) {
                String name = XYContant.XYContants.F;
                boolean isExist = false;
                for (String s : appAll.keySet()) {
                    if (userString.contains("打开" + s)) {
                        name = s;
                        isExist = true;
                        break;
                    } else {
                        isExist = false;
                    }
                }
                if (isExist) {
                    AppUtils.getInstance().openApp(activity, appAll.get(name));
                } else {
                    Utils.getInstance().toast(activity, "没有此应用");
                }
            }
        }
    }
}
