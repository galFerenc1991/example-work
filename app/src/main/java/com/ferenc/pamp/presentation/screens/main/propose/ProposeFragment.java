package com.ferenc.pamp.presentation.screens.main.propose;

import android.view.View;
import android.widget.RelativeLayout;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;

import com.ferenc.pamp.R;
import com.ferenc.pamp.presentation.base.BasePresenter;
import com.ferenc.pamp.presentation.base.tabs.ContentTabsFragment;
import com.ferenc.pamp.presentation.base.tabs.TabPagerAdapter;
import com.ferenc.pamp.presentation.screens.main.propose.delivery.DeliveryFragment_;
import com.ferenc.pamp.presentation.screens.main.propose.description.DescriptionFragment_;
import com.ferenc.pamp.presentation.screens.main.propose.share.ShareFragment_;
import com.ferenc.pamp.presentation.utils.GoodDealManager;

/**
 * Created by
 * Ferenc on 2017.11.23..
 */
@EFragment
public class ProposeFragment extends ContentTabsFragment {

    @ViewById(R.id.rlTitle_FCT)
    protected RelativeLayout rlTitle;

    @StringRes(R.string.title_description)
    protected String mDescriptionTabName;
    @StringRes(R.string.title_delivery)
    protected String mDeliveryTabName;
    @StringRes(R.string.title_share)
    protected String mShareTabName;

    @Bean
    protected GoodDealManager mGoodDealManager;

    @Override
    public void addFragmentsToAdapter(TabPagerAdapter adapter) {
        adapter.addFragment(DescriptionFragment_.builder().build(), mDescriptionTabName);
        adapter.addFragment(DeliveryFragment_.builder().build(), mDeliveryTabName);
        adapter.addFragment(ShareFragment_.builder().build(), mShareTabName);
    }

    @AfterViews
    public void configViewPager() {
        rlTitle.setVisibility(View.VISIBLE);
        vpContent.setOffscreenPageLimit(3);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_content_tabs;
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mGoodDealManager.clearGoodDeal();
    }
}
