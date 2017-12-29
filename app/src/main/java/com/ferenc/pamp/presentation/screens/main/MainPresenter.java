package com.ferenc.pamp.presentation.screens.main;

import com.ferenc.pamp.presentation.screens.main.good_plan.proposed.propose_relay.ProposeRelay;
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
    private ProposeRelay mProposeRelay;

    public MainPresenter(MainContract.View _view, MainContract.Model _model, ProposeRelay _proposeRelay) {
        this.mView = _view;
        this.mModel = _model;
        this.mProposeRelay = _proposeRelay;
        this.mCompositeDisposable = new CompositeDisposable();

        mView.setPresenter(this);
    }

    @Override
    public void subscribe() {
        mProposeRelay.proposeRelay.subscribe(aBoolean -> {
            mView.openProposedFlow();
        });
    }

    @Override
    public void connectGoodDeal(String _id) {
        mCompositeDisposable.add(mModel.connectGoodDeal(_id)
                .subscribe(connectGoodDealResponse -> {
                    ToastManager.showToast(" GoodDeal : " + _id + "connected! \nif you don't see it in received list, please do refresh");
                }, throwable -> {

                }));
    }

    @Override
    public void unsubscribe() {
mCompositeDisposable.clear();
    }
}
