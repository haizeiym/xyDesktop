package xydesk.xy.base;

import android.view.View;
import android.view.ViewGroup;

import xydesk.xy.viewHolder.XYViewHolder;

/**
 * Created by haizeiym
 * on 2016/7/27
 */
public abstract class XYBaseAdapter extends android.widget.BaseAdapter {

    @Override
    public int getCount() {
        return setCount();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        XYViewHolder baseViewHolder;
        if (convertView == null) {
            baseViewHolder = new XYViewHolder();
            convertView = setHolderViewInit(baseViewHolder, position, parent);
            convertView.setTag(baseViewHolder);
        } else {
            baseViewHolder = (XYViewHolder) convertView.getTag();
        }
        setHolderViewData(baseViewHolder, position);
        return convertView;
    }

    //设置listView总数
    public abstract int setCount();

    //初始化Holder
    public abstract View setHolderViewInit(XYViewHolder holder, int position, ViewGroup parent);

    //holder设置数据
    public abstract void setHolderViewData(XYViewHolder holder, int position);
}
