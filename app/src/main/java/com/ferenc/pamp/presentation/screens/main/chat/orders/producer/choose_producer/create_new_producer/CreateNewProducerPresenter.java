package com.ferenc.pamp.presentation.screens.main.chat.orders.producer.choose_producer.create_new_producer;

import com.ferenc.pamp.data.model.home.orders.Producer;
import com.jakewharton.rxrelay2.PublishRelay;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by shonliu on 1/2/18.
 */

public class CreateNewProducerPresenter implements CreateNewProducerContract.Presenter {


    private CreateNewProducerContract.View mView;
    private CompositeDisposable mCompositeDisposable;
    private CreateNewProducerContract.Model mModel;
    private PublishRelay<Boolean> validerData;

    public CreateNewProducerPresenter(CreateNewProducerContract.View _view, CreateNewProducerContract.Model _model) {
        mView = _view;
        mModel = _model;
        mCompositeDisposable = new CompositeDisposable();
        validerData = PublishRelay.create();
        mView.setPresenter(this);
    }

    @Override
    public void subscribe() {
        mCompositeDisposable.add(validerData.subscribe(aBoolean -> mView.enableValidateBtn(aBoolean)));

    }

    @Override
    public void unsubscribe() {
        mCompositeDisposable.clear();
    }

    @Override
    public PublishRelay<Boolean> validateFields() {
        return validerData;
    }

    @Override
    public void createProducer(String _name, String _email, String _phone, String _address, String _description) {

        mCompositeDisposable.add(mModel.createProducer(new Producer(_name, _email, _phone, _address, _description)).subscribe(producer -> {
            mView.finish();
        }));
    }
}
