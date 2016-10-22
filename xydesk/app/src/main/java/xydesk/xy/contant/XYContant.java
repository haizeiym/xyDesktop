package xydesk.xy.contant;

/**
 * Created by haizeiym
 * on 2016/7/28
 */
public class XYContant {

    //常量值
    public interface XYContants {
        int BASE = 0x00000;
        //全局初始化常量
        String F = "FUCK";
        // 删除app
        int DELETER_APP = BASE + 100;
        //添加app
        int ADD_APP = BASE + 101;
        //刷新底部APP
        int REFRESH_BOTTOM_APP = BASE + 103;
        //刷新Fragment
        int REFRESH_FRAGMENT = BASE + 104;
    }

    /**
     * 点击Item
     */
    public interface ClickItem {
        //打开应用
        String OPEN_APP = "打开应用";
        //应用信息
        String APP_INFO = "应用信息";
        //屏幕图标
        String DESK_APP = "屏幕图标";
        //托盘图标
        String BOTTOM_APP = "托盘图标";
        //添加到哪一个屏幕
        String ADD_DESK_ONE = "添加第一屏";
        String ADD_DESK_TWO = "添加第二屏";
        String ADD_DESK_THREE = "添加第三屏";
        String ADD_DESK_FOUR = "添加第四屏";
        //底部位置
        String BOTTOM_ONE = "位置一";
        String BOTTOM_TWO = "位置二";
        String BOTTOM_THREE = "位置三";
        String BOTTOM_FOUR = "位置四";
        String BOTTOM_FIVE = "位置五";
        //应用卸载
        String DELE_APP = "卸载应用";
        //设置语音名称
        String XFNAME = "添加语音名称";
    }

    /**
     * whatFragment
     */
    public interface WharFragment {
        //oneFragment
        String ONE_FRAGMENT = "one_parents";
        //twoFragment
        String TWO_FRAGMENT = "two_parents";
        //threeFragment
        String THREE_FRAGMENT = "three_parents";
        //fourFragment
        String FOUR_FRAGMENT = "four_parents";
    }

    /**
     * 长按item
     */
    public interface LongPressItem {
        //在桌面删除
        String DELE_APP_IN_FRAGMENT = "在桌面删除";
    }

    /**
     * 语音设置
     */
    public interface VoiceSet {
        //添加语音名称
        String NAME_SET = "name_set";
        //是否为语音设置
        String IS_VOICE = "isVoice";
    }

    /**
     * 点击菜单
     */
    public interface ClickMenu {
        //全部应用
        String ALL_APP_IN_MENU = "全部应用";
        //语音助手设置
        String APP_SET_IN_MENU = "语音助手设置";
        //联系人别名设置
        String CONTACT_NAME_SET = "联系人别名设置";
        //刷新fragment
        String REFRESH_FRAGMENT_IN_MENU = "刷新";
    }

    /**
     * fragment传值
     */
    public interface ValuesToFragment {
        String KEY_LIST = "key_list";
        String KEY_INT = "key_int";
        String KEY_WHAT = "key_what";
    }
}
