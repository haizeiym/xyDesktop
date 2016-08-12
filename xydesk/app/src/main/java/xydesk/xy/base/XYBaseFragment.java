package xydesk.xy.base;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by haizeiym
 * on 2016/7/27
 */
public abstract class XYBaseFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createInit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return initCreateView(inflater);
    }

    //初始化试图
    public abstract View initCreateView(LayoutInflater inflater);

    //在onCreate中初始化
    public void createInit() {

    }
}
