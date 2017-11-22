package com.ferenc.pamp.presentation.screens.auth.login;

import com.ferenc.pamp.data.model.auth.ForgotPasswordRequest;
import com.ferenc.pamp.data.model.auth.SignInRequest;
import com.ferenc.pamp.data.model.auth.TokenRequest;
import com.ferenc.pamp.presentation.utils.Constants;
import com.ferenc.pamp.presentation.utils.ValidationManager;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by
 * Ferenc on 2017.11.13..
 */

public class LoginPresenter implements LoginContract.Presenter {

    private LoginContract.View mView;
    private LoginContract.Model mModel;
    private CompositeDisposable mCompositeDisposable;

    public LoginPresenter(LoginContract.View _view, LoginContract.Model _model) {
        this.mView = _view;
        this.mModel = _model;
        this.mCompositeDisposable = new CompositeDisposable();

        mView.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void clickedForgotPassword() {
        mView.openForgotPasswordPopUp();
    }

    @Override
    public void loginWithPamp(String _email, String _password) {

        int errCodeEmail = ValidationManager.validateEmail(_email);
        int errCodePass = ValidationManager.validatePassword(_password);
//        switch (errCodeEmail) {
//            case ValidationManager.EMPTY:
//                mView.showErrorEmailRequired();
//                break;
//            case ValidationManager.INVALID:
//                mView.showErrorEmailInvalid();
//                break;
//            case ValidationManager.OK:
//                mView.hideEmailError();
//                break;
//        }
//        switch (errCodePass) {
//            case ValidationManager.EMPTY:
//                mView.showErrorPasswordRequired();
//                break;
//            case ValidationManager.INVALID:
//                mView.showErrorPasswordInvalid();
//                break;
//            case ValidationManager.OK:
//                mView.hidePasswordError();
//                break;
//        }
        if (errCodeEmail == ValidationManager.OK && errCodePass == ValidationManager.OK) {
            mView.showProgressMain();
            mCompositeDisposable.add(mModel.signIn(new SignInRequest(_email.replaceAll("\\s+$", ""), _password))
                    .subscribe(user -> {
                        mView.hideProgress();
                        mView.openHomeScreen();
                    }, throwable -> {
                        mView.hideProgress();
                        mView.showCustomMessage(throwable.getMessage(), true);
                    }));
        } else {
            mView.showErrorMessage(Constants.MessageType.BAD_CREDENTIALS);
        }
    }

    @Override
    public void sendNewPassword(String _email) {
        int errCodeEmail = ValidationManager.validateEmail(_email);
        if (errCodeEmail == ValidationManager.OK) {
            mView.showProgressMain();
            mCompositeDisposable.add(mModel.forgotPassword(new ForgotPasswordRequest(_email.replaceAll("\\s+$", "")))
                    .subscribe(signUpResponse -> {
                        mView.hideProgress();
                        mView.showCustomMessage("Try to login with the new password", false);
                    }, throwable -> {
                        mView.hideProgress();
                        mView.showCustomMessage(throwable.getMessage(), true);
                    }));
        }
    }

    @Override
    public void loginWithFacebook(String _facebookToken) {
        mView.showProgressMain();
        mCompositeDisposable.add(mModel.signInWithFacebook(new TokenRequest(_facebookToken))
                .subscribe(user -> {
                    mView.hideProgress();
                    mView.openHomeScreen();
                }, throwable -> {
                    mView.hideProgress();
                    mView.showCustomMessage(throwable.getMessage(), true);
                }));
    }

    @Override
    public void loginWithGoogle(String _googleToken) {
        mView.showProgressMain();
        mCompositeDisposable.add(mModel.signInWithGoogle(new TokenRequest(_googleToken))
                .subscribe(user -> {
                    mView.hideProgress();
                    mView.openHomeScreen();
                }, throwable -> {
                    mView.hideProgress();
                    mView.showCustomMessage(throwable.getMessage(), true);
                }));
    }

    @Override
    public void backToAuthScreen() {
        mView.openAuthScreen();
    }

    @Override
    public void unsubscribe() {
        mCompositeDisposable.clear();
    }
}
