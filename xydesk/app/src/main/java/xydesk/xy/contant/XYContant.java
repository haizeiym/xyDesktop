package xydesk.xy.contant;

/**
 * Created by haizeiym
 * on 2016/7/28
 */
public class XYContant {
    private static final int BASE = 0x00000;

    // 删除app
    public static final int DELETER_APP = BASE + 100;

    //添加app
    public static final int ADD_APP = BASE + 101;

    //修改app
    public static final int REPLAY_APP = BASE + 102;

    /**
     * 点击Item
     */
    //打开应用
    public static final String OPEN_APP = "打开应用";
    //添加到桌面
    public static final String ADD_DESK = "添加桌面";
    //删除应用
    public static final String DELE_APP = "删除应用";

    /**
     * whatFragment
     */
    //oneFragment
    public static final String ONE_FRAGMENT = "one_parents";
    //twoFragment
    public static final String TWO_FRAGMENT = "two_parents";
    //threeFragment
    public static final String THREE_FRAGMENT = "three_parents";
    //fourFragment
    public static final String FOUR_FRAGMENT = "four_parents";

    /**
     * 长按item
     */
    //修改应用名
//    public static final String NEW_APP_NAME = "修改应用名";
    //在桌面删除
    public static final String DELE_APP_IN_FRAGMENT = "在桌面删除";
    //卸载此应用
//    public static final String DELE_APP_IN_PHONE = "卸载此应用";
}
