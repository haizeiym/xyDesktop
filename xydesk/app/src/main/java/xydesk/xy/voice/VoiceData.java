package xydesk.xy.voice;

import android.app.Activity;
import android.content.Context;

import java.util.HashMap;
import java.util.Map;

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
    static VoiceData instance;
    static Context context;
    VoiceUtils voiceUtils;
    DeskDB deskDB;

    public VoiceData(Context context) {
        VoiceData.context = context;
        voiceUtils = new VoiceUtils(context);
        deskDB = new DeskDB(context);
        //添加默认名称的APP
        for (String s : initVoiceData().keySet()) {
            XYXFNameSetModel xyxfNameSetModel = new XYXFNameSetModel();
            xyxfNameSetModel.set_app_name = s;
            xyxfNameSetModel.set_app_package_name = initVoiceData().get(s);
            deskDB.addSetAppName(xyxfNameSetModel);
        }
    }

    public static VoiceData getInstance() {
        if (instance == null) {
            instance = new VoiceData(context);
        }
        return instance;
    }

    public void addAllApp() {
        //添加所有APP
        Map<String, String> appAll = AppUtils.allAppName;
        if (!appAll.isEmpty()) {
            for (String s : appAll.keySet()) {
                XYXFNameSetModel xyxfNameSetModel = new XYXFNameSetModel();
                xyxfNameSetModel.set_app_name = "打开" + s;
                xyxfNameSetModel.set_app_package_name = appAll.get(s);
                deskDB.addSetAppName(xyxfNameSetModel);
            }
        }
    }

    private HashMap<String, String> initVoiceData() {
        HashMap<String, String> setApp = new HashMap<>();
        String OPEN = "打开";
        setApp.put(OPEN + "一", "com.tencent.mobileqq");
        setApp.put(OPEN + "1", "com.tencent.mobileqq");
        setApp.put(OPEN + "二", "com.tencent.mm");
        setApp.put(OPEN + "2", "com.tencent.mm");
        setApp.put(OPEN + "三", "com.google.android.marvin.talkback8");
        setApp.put(OPEN + "3", "com.google.android.marvin.talkback8");
        return setApp;
    }

    public void openApp(Activity activity, String userString) {
        String packageName = deskDB.getAppPackageName(userString);
        if (!packageName.equals(XYContant.F)) {
            AppUtils.getInstance().openApp(activity, packageName);
        } else {
            Utils.getInstance().toast(activity, "名称未设置");
        }
    }
}
