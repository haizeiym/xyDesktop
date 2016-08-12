package xydesk.xy.appAll.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import xydesk.xy.base.XYBaseAdapter;
import xydesk.xy.viewHolder.XYViewHolder;
import xydesk.xy.xydesk.R;

/**
 * Created by haizeiym
 * on 2016/8/3
 */
public class ItemViewAdapter extends XYBaseAdapter {
    Context context;
    String[] item;

    public ItemViewAdapter(Context context, String[] item) {
        this.context = context;
        this.item = item;
    }

    @Override
    public int setCount() {
        return item.length;
    }

    @Override
    public View setHolderViewInit(XYViewHolder holder, int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.long_item, parent, false);
        holder.long_press_item = (TextView) convertView.findViewById(R.id.long_item);
        holder.item_line = convertView.findViewById(R.id.item_line);
        return convertView;
    }

    @Override
    public void setHolderViewData(XYViewHolder holder, int position) {
        holder.long_press_item.setText(item[position]);
        if (position == 0) {
            holder.item_line.setVisibility(View.GONE);
        } else {
            holder.item_line.setVisibility(View.VISIBLE);
        }
    }
}
