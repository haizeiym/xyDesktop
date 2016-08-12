package xydesk.xy.viewFragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import java.util.List;

import xydesk.xy.contant.XYContant;
import xydesk.xy.db.DeskDB;
import xydesk.xy.model.XYAppInfoInDesk;
import xydesk.xy.utils.AppUtils;
import xydesk.xy.xydesk.R;

/**
 * Created by haizeiym
 * on 2016/7/28
 */
public class OneAppFragment extends Fragment {
    GridView fragmentApp;
    List<XYAppInfoInDesk> xyAppInfoInDesks;
    DeskDB deskDB;
    public static OneAppFragment instance;
    XYFragmentAdapter xyFragmentAdapter;
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case XYContant.ADD_APP:
                    xyAppInfoInDesks = deskDB.getAllApp(XYContant.ONE_FRAGMENT);
                    xyFragmentAdapter.refresh(xyAppInfoInDesks);
                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.base_fragment, container, false);
        fragmentApp = (GridView) view.findViewById(R.id.app_list);
        deskDB = new DeskDB(getActivity());
        xyAppInfoInDesks = deskDB.getAllApp(XYContant.ONE_FRAGMENT);
        xyFragmentAdapter = new XYFragmentAdapter(getActivity(), xyAppInfoInDesks);
        setAdapter();
        return view;
    }

    private void setAdapter() {
        fragmentApp.setAdapter(xyFragmentAdapter);
        fragmentApp.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AppUtils.getInstance().openApp(getActivity(), xyAppInfoInDesks.get(position).appPackageName);
            }
        });
    }
}
