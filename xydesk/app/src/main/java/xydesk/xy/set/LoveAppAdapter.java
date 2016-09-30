package xydesk.xy.set;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import xydesk.xy.base.XYBaseAdapter;
import xydesk.xy.model.XYAppInfoInDesk;
import xydesk.xy.viewHolder.XYViewHolder;
import xydesk.xy.xydesk.R;

/**
 * Created by haizeiym
 * on 2016/9/30
 */
public class LoveAppAdapter extends XYBaseAdapter {
    private List<XYAppInfoInDesk> list;
    private Context context;

    public LoveAppAdapter(Context context, List<XYAppInfoInDesk> list) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int setCount() {
        return list.size();
    }

    @Override
    public View setHolderViewInit(XYViewHolder holder, int position, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.love_app_item, parent, false);
        holder.loveAppName = (TextView) view.findViewById(R.id.love_app_item);
        return view;
    }

    @Override
    public void setHolderViewData(XYViewHolder holder, int position) {
        holder.loveAppName.setText(list.get(position).appName);
    }

    public void refresh(List<XYAppInfoInDesk> list) {
        this.list = list;
        notifyDataSetChanged();
    }
}
