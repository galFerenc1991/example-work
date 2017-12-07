package com.ferenc.pamp.presentation.screens.main.good_plan.received;

import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ferenc.pamp.R;
import com.ferenc.pamp.data.model.home.good_deal.GoodDealResponse;
import com.ferenc.pamp.domain.GoodDealRepository;
import com.ferenc.pamp.presentation.base.BasePresenter;
import com.ferenc.pamp.presentation.base.content.ContentFragment;
import com.ferenc.pamp.presentation.screens.main.good_plan.good_plan_adapter.GoodPlanAdapter;
import com.ferenc.pamp.presentation.utils.Constants;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.jakewharton.rxbinding2.view.RxView;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by
 * Ferenc on 2017.11.21..
 */
@EFragment
public class ReceivedPlansFragment extends ContentFragment implements ReceivedPlansContract.View {
    @Override
    public void setPresenter(ReceivedPlansContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_received_plans;
    }

    @Override
    protected BasePresenter getPresenter() {
        return mPresenter;
    }

    private ReceivedPlansContract.Presenter mPresenter;

    @ViewById(R.id.rvReceivedPlans_FRP)
    protected RecyclerView rvReceivedPlans;

    @Bean
    protected GoodDealRepository mGoodDealRepository;

    protected GoodPlanAdapter mGoodPlanAdapter;

    @AfterInject
    @Override
    public void initPresenter() {
        new ReceivedPlansPresenter(this, mGoodDealRepository);
    }

    @AfterViews
    protected void initUI() {

        rvReceivedPlans.setLayoutManager(new LinearLayoutManager(getContext()));
        mGoodPlanAdapter = new GoodPlanAdapter(Constants.ITEM_TYPE_RE_BROADCAST);
        rvReceivedPlans.setAdapter(mGoodPlanAdapter);

        RxView.clicks(btnPlaceholderAction_VC)
                .throttleFirst(Constants.CLICK_DELAY, TimeUnit.MILLISECONDS)
                .subscribe();

        mPresenter.subscribe();
    }

    @Override
    public void setReceivedGoodPlanList(List<GoodDealResponse> _receivedGoodPlansList) {
        mGoodPlanAdapter.setList(_receivedGoodPlansList);
    }

    @Override
    public void sharePlayStoreLincInSMS() {

    }

    @Override
    public void showPlaceholder(Constants.PlaceholderType placeholderType) {
        super.showPlaceholder(placeholderType);
        if (placeholderType == Constants.PlaceholderType.EMPTY) {
            tvPlaceholderMessage_VC.setText(R.string.msg_empty_received_good_plans);
            ivPlaceholderImage_VC.setVisibility(View.INVISIBLE);
            btnPlaceholderAction1_VC.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.unsubscribe();
    }
}
