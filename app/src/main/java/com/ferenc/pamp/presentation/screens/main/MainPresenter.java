package com.ferenc.pamp.presentation.screens.main;

import com.ferenc.pamp.presentation.utils.ToastManager;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by
 * Ferenc on 2017.12.07..
 */

public class MainPresenter implements MainContract.Presenter {

    private MainContract.View mView;
    private MainContract.Model mModel;
    private CompositeDisposable mCompositeDisposable;

    public MainPresenter(MainContract.View _view, MainContract.Model _model) {
        this.mView = _view;
        this.mModel = _model;
        this.mCompositeDisposable = new CompositeDisposable();

        mView.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void connectGoodDeal(String _id) {
        mCompositeDisposable.add(mModel.connectGoodDeal(_id)
        .subscribe(connectGoodDealResponse -> {
            ToastManager.showToast(" GoodDeal : " + _id + "connected!");
        }, throwable -> {

        }));
    }

    @Override
    public void unsubscribe() {

    }
}
