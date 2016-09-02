package xydesk.xy.base;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

/**
 * Created by haizeiym
 * on 2016/7/27
 */
public abstract class XYBaseFragment extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    public XYBaseFragment instanceFragment;

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            setHandler(msg);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (instanceFragment == null) {
            instanceFragment = this;
        }
        createInit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return initCreateView(inflater, container);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        itemClick(view, position);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        longpressItem(view, position);
        return true;
    }

    //初始化试图
    public abstract View initCreateView(LayoutInflater inflater, ViewGroup container);

    //在onCreate中初始化
    public void createInit() {

    }

    //handler
    public void setHandler(Message msg) {

    }

    //item点击事件
    public void itemClick(View view, int position) {

    }

    //item长按事件
    public void longpressItem(View view, int position) {

    }
}
