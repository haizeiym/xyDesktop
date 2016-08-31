package xydesk.xy.base;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;


/**
 * Created by haizeiym
 * on 2016/7/27
 */
public abstract class XYBaseActivity extends FragmentActivity implements View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    public XYBaseActivity instance;
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            handler(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            instance = this;
            initView();
            initData();
            setAdapter();
        }
    }

    //初始化视图
    public abstract void initView();

    //初始化数据
    public void initData() {
    }

    //设置点击事件
    public void setOnclick(View v) {
    }

    //设置adapter
    public void setAdapter() {
    }

    //设置Item点击事件
    public void onItemClick(View view, int position) {
    }

    //设置Item长按事件
    public void onItemLongPressed(View view, int position) {

    }

    //handler
    public void handler(Message msg) {
    }

    @Override
    public void onClick(View v) {
        setOnclick(v);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        onItemClick(view, position);
    }


    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        onItemLongPressed(view, position);
        return false;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        instance = this;
        initView();
        initData();
        setAdapter();
    }
}
