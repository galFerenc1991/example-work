package com.kubator.pamp.presentation.screens.auth.sign_up;

import com.kubator.pamp.data.model.auth.TokenRequest;
import com.kubator.pamp.presentation.utils.ValidationManager;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by
 * Ferenc on 2017.11.13..
 */

public class SignUpPresenter implements SignUpContract.Presenter {

    private SignUpContract.View mView;
    private SignUpContract.Model mModel;
    private CompositeDisposable mCompositeDisposable;
    private String mCountry;

    public SignUpPresenter(SignUpContract.View _view, SignUpContract.Model _model) {
        this.mView = _view;
        this.mModel = _model;
        this.mCompositeDisposable = new CompositeDisposable();

        mView.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void backToAuthScreen() {
        mView.openAuthScreen();
    }

    @Override
    public void openCreatePasswordScreen(String _firstName, String _lastName, String _email, String _country) {
        int errCodeName = ValidationManager.firstLastName(_firstName);
        int errCodeSurname = ValidationManager.firstLastName(_lastName);
        int errCodeEmail = ValidationManager.validateEmail(_email);

        switch (errCodeName) {
            case ValidationManager.EMPTY:
                mView.toggleNameError(true);
                break;
            case ValidationManager.INVALID:
                mView.toggleNameError(true);
                break;
            case ValidationManager.OK:
                mView.toggleNameError(false);
                break;
        }
        switch (errCodeSurname) {
            case ValidationManager.EMPTY:
                mView.toggleSurNameError(true);
                break;
            case ValidationManager.INVALID:
                mView.toggleSurNameError(true);
                break;
            case ValidationManager.OK:
                mView.toggleSurNameError(false);
                break;
        }

        switch (errCodeEmail) {
            case ValidationManager.EMPTY:
                mView.toggleEmailError(true);
                break;
            case ValidationManager.INVALID:
                mView.toggleEmailError(true);
                break;
            case ValidationManager.OK:
                mView.toggleEmailError(false);
                break;
        }


        if (errCodeEmail == ValidationManager.OK && errCodeSurname == ValidationManager.OK && errCodeName == ValidationManager.OK) {
            mView.openCreatePasswordScreen(textRefactor(_firstName)
                    , textRefactor(_lastName)
                    , textRefactor(_email)
                    , textRefactor(_country));
        }
//        else {
//            mView.showErrorMessage(Constants.MessageType.INVALID_EMAIL_OR_SOME_FIELDS_EMPTY);
//        }
    }

    private String textRefactor(String text) {
        return text.replaceAll("\\s+$", "");
    }

    @Override
    public void loginOrCreateWithFacebook(String _facebookToken) {
        mView.showProgressMain();
        mCompositeDisposable.add(mModel.createOrLoginWithFacebook(new TokenRequest(_facebookToken))
                .subscribe(user -> {
                    mView.hideProgress();
                    mView.openHomeScreen();
                }, throwable -> {
                    mView.hideProgress();
                    mView.showCustomMessage(throwable.getMessage(), true);
                }));
    }

    @Override
    public void loginOrCreateWithGoogle(String _googleToken) {
        mView.showProgressMain();
        mCompositeDisposable.add(mModel.createOrLoginWithGoogle(new TokenRequest(_googleToken))
                .subscribe(user -> {
                    mView.hideProgress();
                    mView.openHomeScreen();
                }, throwable -> {
                    mView.hideProgress();
                    mView.showCustomMessage(throwable.getMessage(), true);
                }));
    }

    @Override
    public void selectCountry() {
        mView.startSelectCountryScreen(mCountry);
    }

    @Override
    public void setSelectedCountry(String _country) {
        mCountry = _country;
        mView.setCountry(_country);
    }

    @Override
    public void unsubscribe() {
        mCompositeDisposable.clear();
    }
}
