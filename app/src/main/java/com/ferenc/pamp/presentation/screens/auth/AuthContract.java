package com.ferenc.pamp.presentation.screens.auth;

import com.ferenc.pamp.presentation.base.BaseModel;
import com.ferenc.pamp.presentation.base.BasePresenter;
import com.ferenc.pamp.presentation.base.BaseView;
import com.ferenc.pamp.presentation.base.content.ContentView;

/**
 * Created by
 * Ferenc on 2017.11.13..
 */

public interface AuthContract {
    interface View extends ContentView, BaseView<Presenter> {
        void startSignUpScreen();

        void startLoginScreen();
    }

    interface Presenter extends BasePresenter {
        void signUp();

        void login();
    }

    interface Model extends BaseModel {

    }
}
