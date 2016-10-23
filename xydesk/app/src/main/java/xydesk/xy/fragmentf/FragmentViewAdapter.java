package xydesk.xy.fragmentf;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.ViewGroup;

import java.util.List;

import xydesk.xy.base.XYBaseFragment;

/**
 * Created by haizeiym
 * on 2016/9/2
 */
public class FragmentViewAdapter extends FragmentStatePagerAdapter {
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

    public void refreshFragment(List<XYBaseFragment> fragments) {
        this.fragments = fragments;
        notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }
}
