package com.ferenc.pamp.presentation.screens.main.good_plan.proposed;

import com.ferenc.pamp.presentation.utils.Constants;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by
 * Ferenc on 2017.11.21..
 */

public class ProposedPlansPresenter implements ProposedPlansContract.Presenter {

    private ProposedPlansContract.View mView;
    private CompositeDisposable mCompositeDisposable;

    public ProposedPlansPresenter(ProposedPlansContract.View mView) {
        this.mView = mView;
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
