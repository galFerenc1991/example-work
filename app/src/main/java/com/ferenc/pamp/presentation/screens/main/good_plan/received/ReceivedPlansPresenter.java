package com.ferenc.pamp.presentation.screens.main.good_plan.received;

import com.ferenc.pamp.presentation.utils.Constants;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by
 * Ferenc on 2017.11.21..
 */

public class ReceivedPlansPresenter implements ReceivedPlansContract.Presenter {

    private ReceivedPlansContract.View mView;
    private CompositeDisposable mCompositeDisposable;

    public ReceivedPlansPresenter(ReceivedPlansContract.View _view) {
        this.mView = _view;
        this.mCompositeDisposable = new CompositeDisposable();

        mView.setPresenter(this);
    }

    @Override
    public void subscribe() {
        mView.showPlaceholder(Constants.PlaceholderType.EMPTY);
    }

    @Override
    public void unsubscribe() {
        mCompositeDisposable.clear();
    }
}
