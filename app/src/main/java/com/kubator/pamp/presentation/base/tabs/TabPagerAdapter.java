package com.kubator.pamp.presentation.base.tabs;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.kubator.pamp.presentation.base.content.ContentFragment;

import java.util.ArrayList;

/**
 * Created by
 * Ferenc on 2017.11.09..
 */

public class TabPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<String> titles;
    private ArrayList<ContentFragment> fragments;

    public TabPagerAdapter(FragmentManager fm) {
        super(fm);

        fragments = new ArrayList<>();
        titles = new ArrayList<>();
    }

    public void addFragment(ContentFragment fragment, String title) {
        fragments.add(fragment);
        titles.add(title);
        notifyDataSetChanged();
    }

    @Override
    public ContentFragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}


