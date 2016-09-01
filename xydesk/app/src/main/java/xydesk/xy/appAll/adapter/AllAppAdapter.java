package xydesk.xy.appAll.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import java.util.List;

import xydesk.xy.base.XYBaseAdapter;
import xydesk.xy.model.XYAllAppModel;
import xydesk.xy.utils.AppUtils;
import xydesk.xy.utils.Utils;
import xydesk.xy.viewHolder.XYViewHolder;
import xydesk.xy.xydesk.R;

/**
 * Created by haizeiym
 * on 2016/7/27
 * 获取所有app列表
 */
public class AllAppAdapter extends XYBaseAdapter implements SectionIndexer {
    Context context;
    public List<XYAllAppModel> xyAllAppModelList;

    public AllAppAdapter(Context context, List<XYAllAppModel> xyAllAppModelList) {
        this.context = context;
        this.xyAllAppModelList = xyAllAppModelList;
    }

    @Override
    public int setCount() {
        return xyAllAppModelList.size();
    }

    @Override
    public View setHolderViewInit(XYViewHolder holder, int position, ViewGroup parent) {
        View convertView = LayoutInflater.from(context).inflate(R.layout.all_app_item, parent, false);
        holder.app_name = (TextView) convertView.findViewById(R.id.app_name);
        holder.app_icon = (ImageView) convertView.findViewById(R.id.app_icon);
        holder.chart_index = (TextView) convertView.findViewById(R.id.chart_index);
        holder.chart_line = convertView.findViewById(R.id.chart_line);
        holder.item_lineTwo = convertView.findViewById(R.id.item_lineTwo);
        return convertView;
    }

    @Override
    public void setHolderViewData(XYViewHolder holder, int position) {
        //根据position获取分类的首字母的Char ascii值
        int section = getSectionForPosition(position);
        //如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
        if (position == getPositionForSection(section)) {
            holder.chart_index.setVisibility(View.VISIBLE);
            holder.chart_line.setVisibility(View.VISIBLE);
            holder.item_lineTwo.setVisibility(View.GONE);
            holder.chart_index.setText(xyAllAppModelList.get(position).sortLetters);
        } else {
            holder.chart_line.setVisibility(View.GONE);
            holder.chart_index.setVisibility(View.GONE);
            holder.item_lineTwo.setVisibility(View.VISIBLE);
        }
        XYAllAppModel xyAllAppModel = xyAllAppModelList.get(position);
        holder.app_name.setText(xyAllAppModel.appName);
        holder.app_icon.setImageBitmap(Utils.getInstance().drawableToBitmap(xyAllAppModel.appIcon));
    }

    @Override
    public Object[] getSections() {
        return null;
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        for (int i = 0; i < xyAllAppModelList.size(); i++) {
            String sortStr = xyAllAppModelList.get(i).sortLetters;
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == sectionIndex) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int getSectionForPosition(int position) {
        return xyAllAppModelList.get(position).sortLetters.charAt(0);
    }

    public void refresh(List<XYAllAppModel> xyAllAppModelList) {
        this.xyAllAppModelList = xyAllAppModelList;
        notifyDataSetChanged();
    }
}
