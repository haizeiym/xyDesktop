package xydesk.xy.viewFragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import xydesk.xy.base.XYBaseAdapter;
import xydesk.xy.model.XYAppInfoInDesk;
import xydesk.xy.utils.AppUtils;
import xydesk.xy.viewHolder.XYViewHolder;
import xydesk.xy.xydesk.R;

/**
 * Created by haizeiym
 * on 2016/8/8
 */
public class XYFragmentAdapter extends XYBaseAdapter {
    Context context;
    List<XYAppInfoInDesk> xyAppInfoInDesks;

    public XYFragmentAdapter(Context context, List<XYAppInfoInDesk> xyAppInfoInDesks) {
        this.context = context;
        this.xyAppInfoInDesks = xyAppInfoInDesks;
    }

    @Override
    public int setCount() {
        return xyAppInfoInDesks.size();
    }

    @Override
    public View setHolderViewInit(XYViewHolder holder, int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.gridview_app_item, parent, false);
        holder.fragment_app_name = (TextView) convertView.findViewById(R.id.app_name);
        holder.fragment_app_icon = (ImageView) convertView.findViewById(R.id.app_icon);
        return convertView;
    }

    @Override
    public void setHolderViewData(XYViewHolder holder, int position) {
        XYAppInfoInDesk xyAppInfoInDesk = xyAppInfoInDesks.get(position);
        holder.fragment_app_name.setText(xyAppInfoInDesk.appName);
        holder.fragment_app_icon.setImageBitmap(AppUtils.getInstance().getIconFromPackName(context, xyAppInfoInDesk.appPackageName));
    }

    public void refresh(List<XYAppInfoInDesk> xyAppInfoInDesks) {
        this.xyAppInfoInDesks = xyAppInfoInDesks;
        notifyDataSetChanged();
    }
}
