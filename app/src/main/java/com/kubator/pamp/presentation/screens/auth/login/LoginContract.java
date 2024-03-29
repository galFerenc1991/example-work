package com.kubator.pamp.presentation.screens.auth.login;

import com.kubator.pamp.data.model.auth.ForgotPasswordRequest;
import com.kubator.pamp.data.model.auth.SignInRequest;
import com.kubator.pamp.data.model.auth.SignUpResponse;
import com.kubator.pamp.data.model.auth.TokenRequest;
import com.kubator.pamp.data.model.common.User;
import com.kubator.pamp.presentation.base.BaseModel;
import com.kubator.pamp.presentation.base.BasePresenter;
import com.kubator.pamp.presentation.base.BaseView;
import com.kubator.pamp.presentation.base.content.ContentView;

import io.reactivex.Observable;

/**
 * Created by
 * Ferenc on 2017.11.13..
 */

public interface LoginContract {
    interface View extends ContentView, BaseView<Presenter> {
        void openAuthScreen();

        void openHomeScreen();

        void openForgotPasswordPopUp();

        void showErrorEmailRequired();
        void showErrorEmailInvalid();
        void showErrorPasswordRequired();
        void showErrorPasswordInvalid();
        void hideEmailError();
        void hidePasswordError();
    }

    interface Presenter extends BasePresenter {
        void loginWithPamp(String _email, String _password);

        void loginWithFacebook(String _facebookToken);

        void loginWithGoogle(String _googleToken);

        void backToAuthScreen();

        void clickedForgotPassword();

        void sendNewPassword(String _email);
    }

    interface Model extends BaseModel {
        Observable<User> signIn(SignInRequest request);

        Observable<User> signInWithFacebook(TokenRequest _tokenRequest);
        Observable<User> signInWithGoogle(TokenRequest _tokenRequest);

        Observable<SignUpResponse> forgotPassword(ForgotPasswordRequest request);
    }
}
