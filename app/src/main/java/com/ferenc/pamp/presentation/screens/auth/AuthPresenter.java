package com.ferenc.pamp.presentation.screens.auth;

/**
 * Created by
 * Ferenc on 2017.11.13..
 */

public class AuthPresenter implements AuthContract.Presenter {

    private AuthContract.View mView;

    public AuthPresenter(AuthContract.View _view) {
        this.mView = _view;

        mView.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void login() {
        mView.startLoginScreen();
    }

    @Override
    public void signUp() {
        mView.startSignUpScreen();
    }

    @Override
    public void unsubscribe() {

    }
}
