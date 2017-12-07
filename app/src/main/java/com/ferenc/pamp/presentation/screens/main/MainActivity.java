package com.ferenc.pamp.presentation.screens.main;

import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ferenc.pamp.R;
import com.ferenc.pamp.domain.GoodDealRepository;
import com.ferenc.pamp.presentation.base.BaseActivity;
import com.ferenc.pamp.presentation.base.models.GoodDeal;
import com.ferenc.pamp.presentation.screens.main.good_plan.GoodPlanFragment_;
import com.ferenc.pamp.presentation.screens.main.profile.ProfileFragment_;
import com.ferenc.pamp.presentation.screens.main.propose.ProposeFragment_;
import com.ferenc.pamp.presentation.utils.Constants;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.jakewharton.rxbinding2.view.RxView;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.concurrent.TimeUnit;

@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity implements MainContract.View {

    private MainContract.Presenter mPresenter;

    @Bean
    protected GoodDealRepository mGoodDealRepository;

    @ViewById(R.id.toolbar_AM)
    protected Toolbar mToolBar;
    @ViewById(R.id.tlBottomBar)
    protected TabLayout tlBottomBar;

    @ViewById(R.id.llTabGoodPlan_AM)
    protected LinearLayout mTabGoodPlan;
    @ViewById(R.id.llTabPropose_AM)
    protected LinearLayout mTabPropose;
    @ViewById(R.id.llTabAccount_AM)
    protected LinearLayout mTabAccount;

    @ViewById(R.id.ivGoodPlan_AM)
    protected ImageView ivGoodPlan;
    @ViewById(R.id.tvGoodPlan_AM)
    protected TextView tvGoodPlnan;
    @ViewById(R.id.viewGoodPlan_AM)
    protected View viewGoodPlanIndicator;

    @ViewById(R.id.ivPropose_AM)
    protected ImageView ivPropose;
    @ViewById(R.id.tvPropose_AM)
    protected TextView tvPropose;
    @ViewById(R.id.viewPropose_AM)
    protected View viewProposeIndicator;

    @ViewById(R.id.ivAccount_AM)
    protected ImageView ivAccount;
    @ViewById(R.id.tvAccount_AM)
    protected TextView tvAccount;
    @ViewById(R.id.viewAccount_AM)
    protected View viewAccountIndicator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null)
            initFragment();
    }

    private void initFragment() {
        replaceFragment(GoodPlanFragment_.builder().build());
    }

    @AfterInject
    @Override
    public void initPresenter() {
        new MainPresenter(this, mGoodDealRepository);
    }

    @Override
    public void setPresenter(MainContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @AfterViews
    protected void initBottomBar() {

        FirebaseDynamicLinks
                .getInstance().getDynamicLink(this.getIntent())
                .addOnSuccessListener(pendingDynamicLinkData -> {
                    Uri deepLink = null;
                    if (pendingDynamicLinkData != null) {
                        deepLink = pendingDynamicLinkData.getLink();
                        String id = deepLink.getQueryParameter("id");
                        mPresenter.connectGoodDeal(id);
                    }
                });

        TabLayout.Tab tab1 = tlBottomBar.newTab();
        tab1.setIcon(R.drawable.selector_bb_tab1);
        tab1.setText("bon plan");
//        tab1.setCustomView(R.layout.view_tab1);
        TabLayout.Tab tab2 = tlBottomBar.newTab();
        tab2.setIcon(R.drawable.selector_bb_tab2);
        tab2.setText("proposer");
//        tab2.setCustomView(R.layout.view_tab2);
        TabLayout.Tab tab3 = tlBottomBar.newTab();
        tab3.setIcon(R.drawable.selector_bb_tab3);
        tab3.setText("compte");
//        tab3.setCustomView(R.layout.view_tab3);
        tlBottomBar.addTab(tab1);
        tlBottomBar.addTab(tab2);
        tlBottomBar.addTab(tab3);
        tlBottomBar.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        replaceFragmentClearBackstack(GoodPlanFragment_.builder().build());
                        break;
                    case 1:
                        replaceFragmentClearBackstack(ProposeFragment_.builder().build());
                        break;
                    case 2:
                        replaceFragmentClearBackstack(ProfileFragment_.builder().build());
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        RxView.clicks(mTabGoodPlan)
                .throttleFirst(Constants.CLICK_DELAY, TimeUnit.MILLISECONDS)
                .subscribe(o -> {
                    viewGoodPlanIndicator.setBackgroundColor(ContextCompat.getColor(this, R.color.textColorGreen));
                    tvGoodPlnan.setTextColor(ContextCompat.getColor(this, R.color.textColorGreen));
                    ivGoodPlan.setImageResource(R.drawable.ic_bon_plan_act_ic);

                    viewProposeIndicator.setBackgroundColor(ContextCompat.getColor(this, R.color.textColorWhiteTransparent));
                    viewAccountIndicator.setBackgroundColor(ContextCompat.getColor(this, R.color.textColorWhiteTransparent));

                    tvPropose.setTextColor(ContextCompat.getColor(this, R.color.colorGray));
                    tvAccount.setTextColor(ContextCompat.getColor(this, R.color.colorGray));

                    ivPropose.setImageResource(R.drawable.ic_proposer_ic);
                    ivAccount.setImageResource(R.drawable.ic_compte_ic);

                    replaceFragmentClearBackstack(GoodPlanFragment_.builder().build());
                });

        RxView.clicks(mTabPropose)
                .throttleFirst(Constants.CLICK_DELAY, TimeUnit.MILLISECONDS)
                .subscribe(o -> {
                    viewProposeIndicator.setBackgroundColor(ContextCompat.getColor(this, R.color.textColorGreen));
                    tvPropose.setTextColor(ContextCompat.getColor(this, R.color.textColorGreen));
                    ivPropose.setImageResource(R.drawable.ic_proposer_act_ic);

                    viewGoodPlanIndicator.setBackgroundColor(ContextCompat.getColor(this, R.color.textColorWhiteTransparent));
                    viewAccountIndicator.setBackgroundColor(ContextCompat.getColor(this, R.color.textColorWhiteTransparent));

                    tvGoodPlnan.setTextColor(ContextCompat.getColor(this, R.color.colorGray));
                    tvAccount.setTextColor(ContextCompat.getColor(this, R.color.colorGray));

                    ivGoodPlan.setImageResource(R.drawable.ic_bon_plan_ic);
                    ivAccount.setImageResource(R.drawable.ic_compte_ic);

                    replaceFragmentClearBackstack(ProposeFragment_.builder().build());
                });

        RxView.clicks(mTabAccount)
                .throttleFirst(Constants.CLICK_DELAY, TimeUnit.MILLISECONDS)
                .subscribe(o -> {
                    viewAccountIndicator.setBackgroundColor(ContextCompat.getColor(this, R.color.textColorGreen));
                    tvAccount.setTextColor(ContextCompat.getColor(this, R.color.textColorGreen));
                    ivAccount.setImageResource(R.drawable.ic_compte_act_ic);

                    viewGoodPlanIndicator.setBackgroundColor(ContextCompat.getColor(this, R.color.textColorWhiteTransparent));
                    viewProposeIndicator.setBackgroundColor(ContextCompat.getColor(this, R.color.textColorWhiteTransparent));

                    tvGoodPlnan.setTextColor(ContextCompat.getColor(this, R.color.colorGray));
                    tvPropose.setTextColor(ContextCompat.getColor(this, R.color.colorGray));

                    ivGoodPlan.setImageResource(R.drawable.ic_bon_plan_ic);
                    ivPropose.setImageResource(R.drawable.ic_proposer_ic);

                    replaceFragmentClearBackstack(ProfileFragment_.builder().build());
                });
    }

    public void clickedPropose() {
        viewProposeIndicator.setBackgroundColor(ContextCompat.getColor(this, R.color.textColorGreen));
        tvPropose.setTextColor(ContextCompat.getColor(this, R.color.textColorGreen));
        ivPropose.setImageResource(R.drawable.ic_proposer_act_ic);

        viewGoodPlanIndicator.setBackgroundColor(ContextCompat.getColor(this, R.color.textColorWhiteTransparent));
        viewAccountIndicator.setBackgroundColor(ContextCompat.getColor(this, R.color.textColorWhiteTransparent));

        tvGoodPlnan.setTextColor(ContextCompat.getColor(this, R.color.colorGray));
        tvAccount.setTextColor(ContextCompat.getColor(this, R.color.colorGray));

        ivGoodPlan.setImageResource(R.drawable.ic_bon_plan_ic);
        ivAccount.setImageResource(R.drawable.ic_compte_ic);

        replaceFragmentClearBackstack(ProposeFragment_.builder().build());
    }

    @Override
    protected int getContainer() {
        return R.id.flContainer_AM;
    }

    @Override
    protected Toolbar getToolbar() {
        return mToolBar;
    }
}
