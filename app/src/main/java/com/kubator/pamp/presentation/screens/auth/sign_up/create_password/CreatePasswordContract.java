package com.kubator.pamp.presentation.screens.auth.sign_up.create_password;

import com.kubator.pamp.data.model.auth.SignUpRequest;
import com.kubator.pamp.data.model.auth.SignUpResponse;
import com.kubator.pamp.presentation.base.BaseModel;
import com.kubator.pamp.presentation.base.BasePresenter;
import com.kubator.pamp.presentation.base.BaseView;
import com.kubator.pamp.presentation.base.content.ContentView;

import io.reactivex.Observable;

/**
 * Created by
 * Ferenc on 2017.11.13..
 */

public interface CreatePasswordContract {
    interface View extends ContentView, BaseView<Presenter> {
        void openSignUpScreen();

        void openVerificationPopUpDialog();

        void openLoginScreen();

        void togglePasswordError(boolean visibility);
    }

    interface Presenter extends BasePresenter {
        void backToSignUpScreen();

        void openLoginScreen();

        void signUp(String _firstName, String _lastName, String _email, String _country, String _password, String _confPassword);
    }

    interface Model extends BaseModel {
        Observable<SignUpResponse> signUp(SignUpRequest _request);
    }
}
