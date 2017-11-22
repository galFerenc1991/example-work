package com.ferenc.pamp.presentation.screens.main.good_plan;

import com.ferenc.pamp.R;
import com.ferenc.pamp.presentation.base.BasePresenter;
import com.ferenc.pamp.presentation.base.tabs.ContentTabsFragment;
import com.ferenc.pamp.presentation.base.tabs.TabPagerAdapter;
import com.ferenc.pamp.presentation.screens.main.good_plan.proposed.ProposedPlansFragment_;
import com.ferenc.pamp.presentation.screens.main.good_plan.received.ReceivedPlansFragment_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.res.StringRes;

/**
 * Created by
 * Ferenc on 2017.11.17..
 */

@EFragment
public class GoodPlanFragment extends ContentTabsFragment {

    @StringRes(R.string.title_received_plans)
    protected String receivedTabTitle;
    @StringRes(R.string.title_proposed_plans)
    protected String proposedTabTitle;

    @Override
    public void addFragmentsToAdapter(TabPagerAdapter adapter) {
        adapter.addFragment(ReceivedPlansFragment_.builder().build(), receivedTabTitle);
        adapter.addFragment(ProposedPlansFragment_.builder().build(), proposedTabTitle);
    }

    @AfterViews
    public void configViewPager(){
        vpContent.setPagingEnabled(false);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_content_tabs;
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }
}
