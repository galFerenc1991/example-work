package com.kubator.pamp.presentation.screens.main.propose;

import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;

import com.kubator.pamp.R;
import com.kubator.pamp.presentation.base.BasePresenter;
import com.kubator.pamp.presentation.base.tabs.ContentTabsFragment;
import com.kubator.pamp.presentation.base.tabs.TabPagerAdapter;
import com.kubator.pamp.presentation.screens.main.propose.delivery.DeliveryFragment_;
import com.kubator.pamp.presentation.screens.main.propose.description.DescriptionFragment_;
import com.kubator.pamp.presentation.screens.main.propose.share.ShareFragment_;
import com.kubator.pamp.presentation.utils.Constants;
import com.kubator.pamp.presentation.utils.GoodDealManager;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

/**
 * Created by
 * Ferenc on 2017.11.23..
 */
@EFragment
public class ProposeFragment extends ContentTabsFragment {

    @ViewById(R.id.tlTabs_FCT)
    protected TabLayout tlTabLayout;
    @ViewById(R.id.rlTitle_FCT)
    protected RelativeLayout rlTitle;
    @ViewById(R.id.tvTitle_FCT)
    protected TextView tvTitle;
    @ViewById(R.id.ivBack_FCT)
    protected ImageView ivBack;

    @StringRes(R.string.title_rebroadcast_tab_fragment)
    protected String mTitleReBroadcast;
    @StringRes(R.string.title_description)
    protected String mDescriptionTabName;
    @StringRes(R.string.title_delivery)
    protected String mDeliveryTabName;
    @StringRes(R.string.title_share)
    protected String mShareTabName;
    @StringRes(R.string.title_rebroadcast)
    protected String mReBroadcastName;

    @FragmentArg
    protected boolean isReBroadcastFlow;

    @Bean
    protected GoodDealManager mGoodDealManager;

    @Override
    public void addFragmentsToAdapter(TabPagerAdapter adapter) {
        if (!isReBroadcastFlow) {
            adapter.addFragment(DescriptionFragment_.builder().build(), mDescriptionTabName);
            adapter.addFragment(DeliveryFragment_.builder().isReBroadcastFlow(isReBroadcastFlow).build(), mDeliveryTabName);
            adapter.addFragment(ShareFragment_.builder().isReBroadcastFlow(isReBroadcastFlow).isUpdateGoodDeal(false).build(), mShareTabName);
        } else {

            adapter.addFragment(DeliveryFragment_.builder().isReBroadcastFlow(isReBroadcastFlow).build(), mDeliveryTabName);
            adapter.addFragment(ShareFragment_.builder().isReBroadcastFlow(isReBroadcastFlow).isUpdateGoodDeal(false).build(), mReBroadcastName);
        }
    }

    @AfterViews
    public void configViewPager() {
        rlTitle.setVisibility(View.VISIBLE);
        if (!isReBroadcastFlow) {
            vpContent.setOffscreenPageLimit(2);
        } else {
            RxView.clicks(ivBack)
                    .throttleFirst(Constants.CLICK_DELAY, TimeUnit.MILLISECONDS)
                    .subscribe(o -> {
                        mActivity.finish();
                    });
            ivBack.setVisibility(View.VISIBLE);
            tvTitle.setText(mTitleReBroadcast);
            tlTabLayout.setTabTextColors(ContextCompat.getColorStateList(mActivity,R.color.color_tab_text_indicator));
            tlTabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.color_re_broadcast_tab_indicator));
        }

        vpContent.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                hideKeyboard();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
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
