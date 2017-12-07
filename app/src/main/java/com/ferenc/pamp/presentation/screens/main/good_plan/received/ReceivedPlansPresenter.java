package com.ferenc.pamp.presentation.screens.main.good_plan.received;

import android.net.Uri;

import com.ferenc.pamp.presentation.utils.Constants;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by
 * Ferenc on 2017.11.21..
 */

public class ReceivedPlansPresenter implements ReceivedPlansContract.Presenter {

    private ReceivedPlansContract.View mView;
    private ReceivedPlansContract.Model mModel;
    private CompositeDisposable mCompositeDisposable;

    public ReceivedPlansPresenter(ReceivedPlansContract.View _view, ReceivedPlansContract.Model _model) {
        this.mView = _view;
        this.mModel = _model;
        this.mCompositeDisposable = new CompositeDisposable();

        mView.setPresenter(this);
    }

    @Override
    public void subscribe() {
        mView.showProgressMain();
        mCompositeDisposable.add(mModel.getReceivedGoodDeal()
                .subscribe(goodDealResponseListResponse -> {
                    mView.hideProgress();
                    if (!goodDealResponseListResponse.data.isEmpty()) {
                        mView.setReceivedGoodPlanList(goodDealResponseListResponse.data);
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
    public void sharePlayStoreLincInSMS() {

    }

    @Override
    public void unsubscribe() {
        mCompositeDisposable.clear();
    }
}
