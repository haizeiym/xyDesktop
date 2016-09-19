package xydesk.xy.fragmentf;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

import xydesk.xy.base.XYBaseFragment;

/**
 * Created by haizeiym
 * on 2016/9/2
 */
public class FragmentViewAdapter extends FragmentPagerAdapter {
    private List<XYBaseFragment> fragments; // 每个Fragment对应一个Page

    public FragmentViewAdapter(FragmentManager fragmentManager, List<XYBaseFragment> fragments) {
        super(fragmentManager);
        this.fragments = fragments;
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }
}
