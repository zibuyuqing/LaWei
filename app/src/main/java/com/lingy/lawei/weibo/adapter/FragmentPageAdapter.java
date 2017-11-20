package com.lingy.lawei.weibo.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Xijun.Wang on 2017/10/23.
 */

public class FragmentPageAdapter<T extends Fragment> extends FragmentPagerAdapter {
    private FragmentManager manager;
    private List<T> fragments;
    public FragmentPageAdapter(FragmentManager fm, List<T> fragments) {
        super(fm);
        this.manager = fm;
        this.fragments = fragments;
    }

    @Override
    public T getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }
}
