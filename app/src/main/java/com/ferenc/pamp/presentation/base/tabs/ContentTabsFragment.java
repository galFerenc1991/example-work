package com.ferenc.pamp.presentation.base.tabs;

import android.support.design.widget.TabLayout;

import com.ferenc.pamp.presentation.base.content.ContentFragment;
import com.ferenc.pamp.presentation.custom.ScrollableViewPager;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

/**
 * Created by
 * Ferenc on 2017.11.09..
 */

@EFragment
public abstract class ContentTabsFragment extends ContentFragment {

    public TabPagerAdapter tabPagerAdapter;

    @ViewById
    protected TabLayout tlTabs_FCT;
    @ViewById
    public ScrollableViewPager vpContent_FCT;

    @AfterInject
    protected void initData() {
        tabPagerAdapter = new TabPagerAdapter(getChildFragmentManager());
        addFragmentsToAdapter(tabPagerAdapter);
    }

    public abstract void addFragmentsToAdapter(TabPagerAdapter adapter);

    @AfterViews
    protected void initUI() {
        mActivity.getToolbarManager().enableToolbarElevation(false);
        vpContent_FCT.setAdapter(tabPagerAdapter);
        tlTabs_FCT.setupWithViewPager(vpContent_FCT);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mActivity.getToolbarManager().enableToolbarElevation(true);
    }
}

