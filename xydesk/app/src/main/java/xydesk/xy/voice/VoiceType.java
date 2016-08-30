package xydesk.xy.voice;

import android.app.Activity;
import android.content.Intent;

import java.util.Map;

import xydesk.xy.appAll.ui.AllAppShowUI;
import xydesk.xy.utils.AppUtils;
import xydesk.xy.utils.Utils;

/**
 * Created by haizeiym
 * on 2016/8/26
 * 语音操作
 */

public class VoiceType {

    //打开应用
    public static void isIn(Activity activity, String userString) {
        //打开全部应用页面
        if (userString.contains("全部应用")) {
            Intent intent = new Intent(activity, AllAppShowUI.class);
            activity.startActivity(intent);
            return;
        }
        VoiceData.getInstance().openApp(activity, userString);

        //打开应用
        Map<String, String> appAll = AppUtils.allAppName;
        if (!appAll.isEmpty()) {
            boolean isHave = false;
            String name = "";
            for (String s : appAll.keySet()) {
                if (userString.contains("打开" + s)) {
                    isHave = true;
                    name = s;
                    break;
                } else {
                    isHave = false;
                }
            }
            if (isHave) {
                AppUtils.getInstance().openApp(activity, appAll.get(name));
            } else {
                Utils.getInstance().toast(activity, "无此应用");
            }
        }
    }
}
