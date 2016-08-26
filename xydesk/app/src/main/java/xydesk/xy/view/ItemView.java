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
    public String[] itemAll = {XYContant.OPEN_APP, XYContant.ADD_DESK, XYContant.DELE_APP};
    //长按应用
    public String[] itemLong = {XYContant.DELE_APP_IN_FRAGMENT};

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
            TextView clickItem = (TextView) view.findViewById(R.id.add_item_view);
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
