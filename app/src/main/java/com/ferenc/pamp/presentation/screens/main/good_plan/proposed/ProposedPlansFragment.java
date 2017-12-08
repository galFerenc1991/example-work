package com.ferenc.pamp.presentation.screens.main.good_plan.proposed;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ferenc.pamp.R;
import com.ferenc.pamp.data.model.home.good_deal.GoodDealResponse;
import com.ferenc.pamp.domain.GoodDealRepository;
import com.ferenc.pamp.presentation.base.list.EndlessScrollListener;
import com.ferenc.pamp.presentation.base.refreshable.RefreshableFragment;
import com.ferenc.pamp.presentation.base.refreshable.RefreshablePresenter;
import com.ferenc.pamp.presentation.screens.main.MainActivity;
import com.ferenc.pamp.presentation.screens.main.good_plan.good_plan_adapter.GoodPlanAdapter;
import com.ferenc.pamp.presentation.utils.Constants;
import com.jakewharton.rxbinding2.view.RxView;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by
 * Ferenc on 2017.11.21..
 */
@EFragment
public class ProposedPlansFragment extends RefreshableFragment implements ProposedPlansContract.View {
    @Override
    public void setPresenter(ProposedPlansContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_proposed_plans;
    }

    @Override
    protected RefreshablePresenter getPresenter() {
        return mPresenter;
    }

    private ProposedPlansContract.Presenter mPresenter;
    protected EndlessScrollListener mScrollListener;


    @ViewById(R.id.rvReceivedPlans_FPP)
    protected RecyclerView rvProposedPlans;

    @Bean
    protected GoodDealRepository mGoodDealRepository;

    protected GoodPlanAdapter mGoodPlanAdapter;


    @AfterInject
    @Override
    public void initPresenter() {
        new ProposedPlansPresenter(this, mGoodDealRepository);
    }

    @AfterViews
    protected void initUI() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mScrollListener = new EndlessScrollListener(layoutManager, () -> {
            if (isRefreshing()) return false;
            mPresenter.loadNextPage();
            return true;
        });

        rvProposedPlans.setLayoutManager(layoutManager);
        mGoodPlanAdapter = new GoodPlanAdapter(Constants.ITEM_TYPE_REUSE);
        rvProposedPlans.setAdapter(mGoodPlanAdapter);
        rvProposedPlans.addOnScrollListener(mScrollListener);

        RxView.clicks(btnPlaceholderAction_VC)
                .throttleFirst(Constants.CLICK_DELAY, TimeUnit.MILLISECONDS)
                .subscribe(o -> mPresenter.openProposerFragment());

        mPresenter.subscribe();
    }

    @Override
    public void setProposedGoodPlanList(List<GoodDealResponse> _proposedGoodPlansList) {
        mScrollListener.reset();
        mGoodPlanAdapter.setList(_proposedGoodPlansList);
    }

    @Override
    public void addProposedGoodPlanList(List<GoodDealResponse> _receivedGoodPlansList) {
        mGoodPlanAdapter.addListDH(_receivedGoodPlansList);
    }

    @Override
    public void showPlaceholder(Constants.PlaceholderType placeholderType) {
        super.showPlaceholder(placeholderType);
        if (placeholderType == Constants.PlaceholderType.EMPTY) {
            tvPlaceholderMessage_VC.setText(R.string.msg_empty_proposed_good_plans);
            ivPlaceholderImage_VC.setVisibility(View.INVISIBLE);
            btnPlaceholderAction_VC.setVisibility(View.VISIBLE);
            btnPlaceholderAction_VC.setText(R.string.button_propose);
        }
    }

    @Override
    public void openProposerFragment() {
        MainActivity activity = (MainActivity) getActivity();
        activity.clickedPropose();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.unsubscribe();
    }
}
