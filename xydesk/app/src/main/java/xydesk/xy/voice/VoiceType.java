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
    private static final String OPEN = "打开";
    private static final String ALL_APP = "全部应用";
    //默认数字
    private static final String[] H_N = {OPEN + "一", OPEN + "1", OPEN + "二", OPEN + "2", OPEN + "三", OPEN + "3"};
    //常用软件默认QQ，微信，心阳读屏
    private static final String[] USULLY_APP = {"com.tencent.mobileqq", "com.tencent.mm", "com.google.android.marvin.talkback8"};

    //打开应用
    public static void isIn(Activity activity, String userString) {
        if (userString.contains(H_N[0]) || userString.contains(H_N[1])) {
            AppUtils.getInstance().openApp(activity, USULLY_APP[0]);
            return;
        } else if (userString.contains(H_N[2]) || userString.contains(H_N[3])) {
            AppUtils.getInstance().openApp(activity, USULLY_APP[1]);
            return;
        } else if (userString.contains(H_N[4]) || userString.contains(H_N[5]) || userString.contains("伞") || userString.contains("散")) {
            AppUtils.getInstance().openApp(activity, USULLY_APP[2]);
            return;
        }
        //打开全部应用页面
        if (userString.contains(ALL_APP)) {
            Intent intent = new Intent(activity, AllAppShowUI.class);
            activity.startActivity(intent);
            return;
        }
        //打开应用
        Map<String, String> appAll = AppUtils.allAppName;
        if (!appAll.isEmpty()) {
            for (String s : appAll.keySet()) {
                if (userString.contains(OPEN + s)) {
                    AppUtils.getInstance().openApp(activity, appAll.get(s));
                    return;
                } else {
                    Utils.getInstance().toast(activity, "请正确发音");
                }
            }
        }
    }
}
