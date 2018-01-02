package com.ferenc.pamp.presentation.screens.main.chat.orders.producer.choose_producer.create_new_producer;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by shonliu on 1/2/18.
 */

public class CreateNewProducerPresenter implements CreateNewProducerContract.Presenter {


    private CreateNewProducerContract.View mView;
    private CompositeDisposable mCompositeDisposable;
    private CreateNewProducerContract.Model mModel;

    public CreateNewProducerPresenter(CreateNewProducerContract.View _view, CreateNewProducerContract.Model _model) {
        mView = _view;
        mModel = _model;
        mCompositeDisposable = new CompositeDisposable();
        mView.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {
        mCompositeDisposable.clear();
    }
}
