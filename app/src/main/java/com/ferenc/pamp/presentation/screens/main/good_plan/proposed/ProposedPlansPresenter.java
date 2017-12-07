package com.ferenc.pamp.presentation.screens.main.good_plan.proposed;

import com.ferenc.pamp.domain.GoodDealRepository;
import com.ferenc.pamp.presentation.utils.Constants;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by
 * Ferenc on 2017.11.21..
 */

public class ProposedPlansPresenter implements ProposedPlansContract.Presenter {

    private ProposedPlansContract.View mView;
    private ProposedPlansContract.Model mModel;
    private CompositeDisposable mCompositeDisposable;

    public ProposedPlansPresenter(ProposedPlansContract.View mView, ProposedPlansContract.Model _goodDealRepository) {
        this.mView = mView;
        this.mModel = _goodDealRepository;
        this.mCompositeDisposable = new CompositeDisposable();

        mView.setPresenter(this);
    }

    @Override
    public void openProposerFragment() {
        mView.openProposerFragment();
    }

    @Override
    public void subscribe() {
        mView.showProgressMain();
        mCompositeDisposable.add(mModel.getProposedGoodDeal()
                .subscribe(goodDealResponseListResponse -> {
                    if (!goodDealResponseListResponse.data.isEmpty()) {
                        mView.hideProgress();
                        mView.setProposedGoodPlanList(goodDealResponseListResponse.data);
                    } else {
                        mView.showPlaceholder(Constants.PlaceholderType.EMPTY);
                    }
                }, throwable -> {
                    mView.hideProgress();
                }));
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void unsubscribe() {
        mCompositeDisposable.clear();
    }
}
