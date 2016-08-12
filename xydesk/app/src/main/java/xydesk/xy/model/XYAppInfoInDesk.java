package xydesk.xy.model;

import java.io.Serializable;

/**
 * Created by haizeiym
 * on 2016/8/8
 */
public class XYAppInfoInDesk implements Serializable {
    //app名称
    public String appName;
    //app包名
    public String appPackageName;
    //app启动activity
    public String appMainActivity;
    //app在那个屏
    public String appPonitParents;
    //app在屏幕的横向位置
    public String appPonitOne;
    //app在屏幕的纵向位置
    public String appPonitTwo;
}
