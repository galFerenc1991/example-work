package com.ferenc.pamp.presentation.screens.auth.sign_up.create_password;


import com.ferenc.pamp.data.api.exceptions.ConnectionLostException;
import com.ferenc.pamp.data.model.auth.SignUpRequest;
import com.ferenc.pamp.data.model.base.GeneralMessageResponse;
import com.ferenc.pamp.presentation.utils.Constants;
import com.ferenc.pamp.presentation.utils.ToastManager;
import com.ferenc.pamp.presentation.utils.ValidationManager;
import com.google.gson.Gson;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import retrofit2.HttpException;

/**
 * Created by
 * Ferenc on 2017.11.13..
 */

public class CreatePasswordPresenter implements CreatePasswordContract.Presenter {

    private CreatePasswordContract.View mView;
    private CreatePasswordContract.Model mModel;
    private CompositeDisposable mCompositeDisposable;

    public CreatePasswordPresenter(CreatePasswordContract.View _view, CreatePasswordContract.Model _model) {
        this.mView = _view;
        this.mModel = _model;
        this.mCompositeDisposable = new CompositeDisposable();

        mView.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void signUp(String _firstName, String _lastName, String _email, String _country, String _password, String _confPassword) {
        int errCodePassword = ValidationManager.validatePassword(_password);
        int errCodePasswordMatches = ValidationManager.isPasswordsMatches(_password, _confPassword);

//        if (errCodePassword == ValidationManager.OK && errCodePasswordMatches == ValidationManager.OK) {
        if (errCodePasswordMatches == ValidationManager.OK) {

            mView.showProgressMain();
            mCompositeDisposable.add(mModel.signUp(new SignUpRequest(_firstName, _lastName, _email, _country, _password, _confPassword))
                    .subscribe(signUpResponse -> {
                        mView.hideProgress();
                        mView.openVerificationPopUpDialog();
                    }, throwableConsumer));
        } else
            mView.showErrorMessage(Constants.MessageType.EMPTY_FIELDS_OR_NOT_MATCHES_PASSWORDS);

    }

    private Consumer<Throwable> throwableConsumer = throwable -> {
        throwable.printStackTrace();
        mView.hideProgress();
        if (throwable instanceof ConnectionLostException) {
            ToastManager.showToast("Connection Lost");
        } else if (throwable instanceof HttpException) {
            int errorCode = ((HttpException) throwable).response().code();
            switch (errorCode) {
                case 400:
                    Gson gson = new Gson();
                    GeneralMessageResponse _data = gson.fromJson(((HttpException) throwable).response().errorBody().string(), GeneralMessageResponse.class);
                    ToastManager.showToast(_data.getMessage());
            }
        } else {
            ToastManager.showToast("Something went wrong");
        }
    };

    @Override
    public void openLoginScreen() {
        mView.openLoginScreen();
    }

    @Override
    public void backToSignUpScreen() {
        mView.openSignUpScreen();
    }

    @Override
    public void unsubscribe() {
        mCompositeDisposable.clear();
    }
}
