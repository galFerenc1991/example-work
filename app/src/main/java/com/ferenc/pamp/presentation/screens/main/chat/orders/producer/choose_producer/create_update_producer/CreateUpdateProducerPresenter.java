package com.ferenc.pamp.presentation.screens.main.chat.orders.producer.choose_producer.create_update_producer;

import android.text.TextUtils;
import android.util.Log;

import com.ferenc.pamp.data.model.home.orders.Producer;
import com.ferenc.pamp.presentation.utils.ValidationManager;
import com.jakewharton.rxrelay2.PublishRelay;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by shonliu on 1/2/18.
 */

public class CreateUpdateProducerPresenter implements CreateUpdateProducerContract.Presenter {


    private CreateUpdateProducerContract.View mView;
    private CompositeDisposable mCompositeDisposable;
    private CreateUpdateProducerContract.Model mModel;
    private PublishRelay<Boolean> validerData;
    private boolean mIsCreate;
    private Producer mProducer;

    public CreateUpdateProducerPresenter(CreateUpdateProducerContract.View _view,
                                         CreateUpdateProducerContract.Model _model,
                                         boolean _isCreate,
                                         Producer _producer) {
        mView = _view;
        mModel = _model;
        mCompositeDisposable = new CompositeDisposable();
        validerData = PublishRelay.create();
        mIsCreate = _isCreate;
        mProducer = _producer;
        mView.setPresenter(this);
    }

    @Override
    public void subscribe() {
        mCompositeDisposable.add(validerData.subscribe(aBoolean -> mView.enableValidateBtn(aBoolean)));
        if (!mIsCreate)
            mView.setProducerData(mProducer);
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
    public void createUpdateProducer(String _name,
                                     String _email,
                                     String _phone,
                                     String _address,
                                     String _description) {

        mView.showProgress();
        mCompositeDisposable.add((mIsCreate
                ? mModel.createProducer(new Producer(_name, _email, _phone, _address, _description))
                : mModel.updateProducer(new Producer(
                mProducer.producerId,
                _name,
                _email,
                _phone,
                _address,
                _description)))
                .subscribe(producer -> {
                            mView.finishActivityWithResult(producer);
//                            mView.hideProgress();
                        },
                        e -> {
                            mView.hideProgress();
                            Log.d(mIsCreate ? "Create" : "Update", "Error " + e.getMessage());
                        }
                ));
    }

    @Override
    public boolean validateData(String _name,
                                String _email,
                                String _phone,
                                String _address,
                                String _description) {

        if (mIsCreate) {
            return !TextUtils.isEmpty(_name)
                    && ValidationManager.validateEmail(_email) == ValidationManager.OK;
        } else {
            return ((!TextUtils.isEmpty(_name)
                    && ValidationManager.validateEmail(_email) == ValidationManager.OK)&&
                    (!_name.equals(mProducer.name)
                    || !_email.equals(mProducer.email)
                    || !_phone.equals(mProducer.phone == null ? "" : mProducer.phone)
                    || !_address.equals(mProducer.address == null ? "" : mProducer.address)
                    || !_description.equals(mProducer.description == null ? "" : mProducer.description)));
        }

    }

    @Override
    public void clickOnAddress() {
        mView.openAutocompletePlaceScreen();
    }
}
