package xydesk.xy.viewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by haizeiym
 * on 2016/7/27
 * 所有APP的ViewHolder
 */
public class XYViewHolder {

    /**
     * 显示所有app
     */
    //应用名称
    public TextView app_name;
    //应用图标
    public ImageView app_icon;
    //应用角标
    public TextView chart_index;
    //角标分割线
    public View chart_line;
    //item 分割线
    public View item_lineTwo;
    /**
     * 点击Item显示的条目
     */
    //点击显示的Item
    public TextView long_press_item;
    //item分割线
    public View item_line;

    /**
     * 在主页中显示的app信息
     */
    //应用名称
    public TextView fragment_app_name;
    //应用图标
    public ImageView fragment_app_icon;
}

