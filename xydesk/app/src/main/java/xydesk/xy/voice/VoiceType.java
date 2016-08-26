package xydesk.xy.voice;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import xydesk.xy.appAll.ui.AllAppShowUI;
import xydesk.xy.utils.AppUtils;

/**
 * Created by haizeiym
 * on 2016/8/26
 * 语音操作
 */

public class VoiceType {
    private static final String OPEN = "打开";
    private static final String ALL_APP = "全部应用";

    //打开应用
    public static void isIn(Activity activity, String userString) {
        if (userString.contains(ALL_APP)) {
            Intent intent = new Intent(activity, AllAppShowUI.class);
            activity.startActivity(intent);
            return;
        }
        Map<String, String> appAll = AppUtils.allAppName;
        if (!appAll.isEmpty()) {
            for (String s : appAll.keySet()) {
                if (userString.contains(OPEN + s)) {
                    AppUtils.getInstance().openApp(activity, appAll.get(s));
                    return;
                }
            }
        }
    }
}
