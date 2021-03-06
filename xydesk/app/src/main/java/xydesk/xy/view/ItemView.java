package xydesk.xy.view;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import xydesk.xy.contant.XYContant;
import xydesk.xy.i.ViewI;
import xydesk.xy.xydesk.R;

/**
 * Created by haizeiym
 * on 2016/8/1
 */
public class ItemView {
    public static ItemView getInstance() {
        return ItemInstance.instance;
    }

    private ItemView() {
    }

    private static class ItemInstance {
        private static final ItemView instance = new ItemView();
    }

    //点击应用
    public String[] itemAll = {XYContant.ClickItem.OPEN_APP, XYContant.ClickItem.APP_INFO, XYContant.ClickItem.DESK_APP, XYContant.ClickItem.BOTTOM_APP, XYContant.ClickItem.DELE_APP, XYContant.ClickItem.XFNAME};
    //放到那个屏幕
    public String[] deskApp = {XYContant.ClickItem.ADD_DESK_ONE, XYContant.ClickItem.ADD_DESK_TWO, XYContant.ClickItem.ADD_DESK_THREE, XYContant.ClickItem.ADD_DESK_FOUR};
    //底部APP
    public String[] bottomApp = {XYContant.ClickItem.BOTTOM_ONE, XYContant.ClickItem.BOTTOM_TWO, XYContant.ClickItem.BOTTOM_THREE, XYContant.ClickItem.BOTTOM_FOUR, XYContant.ClickItem.BOTTOM_FIVE};
    //长按应用
    public String[] itemLong = {XYContant.LongPressItem.DELE_APP_IN_FRAGMENT/*, XYContant.DELE_APP*/};
    //点击菜单
    public String[] menu_click = {XYContant.ClickMenu.ALL_APP_IN_MENU, XYContant.ClickMenu.APP_SET_IN_MENU, XYContant.ClickMenu.CONTACT_NAME_SET, /*XYContant.REFRESH_FRAGMENT_IN_MENU*/};

    public void showLongView(Context context, String[] item, final ViewI viewI) {
        final Dialog alertDialog = new Dialog(context);
        alertDialog.show();
        Window window = alertDialog.getWindow();
        window.setContentView(R.layout.item_long_pressed_view);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        window.setAttributes(params);
        window.setBackgroundDrawable(null);
        LinearLayout itemList = (LinearLayout) window.findViewById(R.id.add_view_item);
        itemList.removeAllViews();
        for (String i : item) {
            View view = View.inflate(context, R.layout.add_item_view, null);
            TextView clickItem = (TextView) view.findViewById(R.id.add_item_view_text);
            clickItem.setTag(i);
            clickItem.setText(i);
            clickItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                    viewI.click(v, 0);
                }
            });
            itemList.addView(view);
        }
    }
}
