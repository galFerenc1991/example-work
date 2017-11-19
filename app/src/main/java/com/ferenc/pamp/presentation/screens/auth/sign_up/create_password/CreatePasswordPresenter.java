package com.ferenc.pamp.presentation.screens.auth.sign_up.create_password;


import com.ferenc.pamp.data.model.auth.SignUpRequest;
import com.ferenc.pamp.presentation.utils.Constants;
import com.ferenc.pamp.presentation.utils.ValidationManager;

import io.reactivex.disposables.CompositeDisposable;

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
                    }, throwable -> {
                        mView.hideProgress();
                        mView.showCustomMessage(throwable.getMessage(), true);
                    }));
        } else
            mView.showErrorMessage(Constants.MessageType.EMPTY_FIELDS_OR_NOT_MATCHES_PASSWORDS);

    }

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
