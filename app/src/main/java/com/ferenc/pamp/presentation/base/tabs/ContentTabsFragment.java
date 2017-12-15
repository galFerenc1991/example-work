package com.ferenc.pamp.presentation.base.tabs;

import android.support.design.widget.TabLayout;
import android.view.View;

import com.ferenc.pamp.R;
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

    @ViewById(R.id.tlTabs_FCT)
    protected TabLayout tlTabs;
    @ViewById(R.id.vpContent_FCT)
    public ScrollableViewPager vpContent;

    @AfterInject
    protected void initData() {
        tabPagerAdapter = new TabPagerAdapter(getChildFragmentManager());
        addFragmentsToAdapter(tabPagerAdapter);
    }

    public abstract void addFragmentsToAdapter(TabPagerAdapter adapter);

    public void toggleTabsVisibility(Boolean hide) {
        if (hide) {
            tlTabs.setVisibility(View.GONE);
        } else {
            tlTabs.setVisibility(View.VISIBLE);
        }
    }

    @AfterViews
    protected void initUI() {
        mActivity.getToolbarManager().enableToolbarElevation(true);
        mActivity.getToolbarManager().hideToolbar(true);
        vpContent.setAdapter(tabPagerAdapter);
        tlTabs.setupWithViewPager(vpContent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mActivity.getToolbarManager().enableToolbarElevation(true);
    }
}

