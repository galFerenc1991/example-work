package com.ferenc.pamp.presentation.screens.main;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ferenc.pamp.R;
import com.ferenc.pamp.presentation.base.BaseActivity;
import com.ferenc.pamp.presentation.screens.main.good_plan.GoodPlanFragment_;
import com.ferenc.pamp.presentation.utils.Constants;
import com.jakewharton.rxbinding2.view.RxView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.concurrent.TimeUnit;

@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity {

    @ViewById(R.id.toolbar_AM)
    protected Toolbar mToolBar;

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
        initFragment();
    }

    protected void initFragment() {
        replaceFragment(GoodPlanFragment_.builder().build());
    }

    @AfterViews
    protected void initBottomBar() {
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
                });
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
