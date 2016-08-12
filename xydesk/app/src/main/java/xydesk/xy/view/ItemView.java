package xydesk.xy.view;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;

import xydesk.xy.appAll.adapter.ItemViewAdapter;
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

    public String[] item = {XYContant.OPEN_APP, XYContant.ADD_DESK, XYContant.DELE_APP};

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
        ListView itemList = (ListView) window.findViewById(R.id.item_long_pressed);
        itemList.setAdapter(new ItemViewAdapter(context, item));
        itemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                alertDialog.dismiss();
                viewI.click(view, position);
            }
        });
    }
}
