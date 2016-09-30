package xydesk.xy.model;

import android.graphics.drawable.Drawable;

import java.io.Serializable;

/**
 * Created by haizeiym
 * on 2016/7/27
 */
public class XYAllAppModel implements Serializable {
    /**
     * 所有APP
     */
    //APP名称
    public String appName;
    //APP图标
    public Drawable appIcon;
    //APP包名
    public String appPackageName;
    //程序主类名
    public String activityMainName;
    //显示数据拼音的首字母
    public String sortLetters;
    //版本号
    public String appVersion;

    public XYAllAppModel(String appName, String appPackageName, Drawable appIcon, String sortLetters, String activityMainName, String appVersion) {
        this.appName = appName;
        this.appIcon = appIcon;
        this.appPackageName = appPackageName;
        this.activityMainName = activityMainName;
        this.sortLetters = sortLetters;
        this.appVersion = appVersion;
    }

    public XYAllAppModel() {
    }
}
