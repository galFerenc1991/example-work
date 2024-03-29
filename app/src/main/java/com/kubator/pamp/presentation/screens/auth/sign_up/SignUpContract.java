package com.kubator.pamp.presentation.screens.auth.sign_up;

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

public interface SignUpContract {
    interface View extends ContentView, BaseView<Presenter> {
        void openAuthScreen();

        void openHomeScreen();

        void openCreatePasswordScreen(String _firstName, String _lastName, String _email, String _country);

        void startSelectCountryScreen(String _selectedCountry);

        void setCountry(String _selectedCountry);

        void toggleNameError(boolean visibility);

        void toggleSurNameError(boolean visibility);

        void toggleEmailError(boolean visibility);
    }

    interface Presenter extends BasePresenter {
        void backToAuthScreen();

        void openCreatePasswordScreen(String _firstName, String _lastName, String _email, String _country);

        void loginOrCreateWithFacebook(String _facebookToken);

        void loginOrCreateWithGoogle(String _googleToken);

        void selectCountry();

        void setSelectedCountry(String _country);

    }

    interface Model extends BaseModel {

        Observable<User> createOrLoginWithFacebook(TokenRequest _token);
        Observable<User> createOrLoginWithGoogle(TokenRequest _token);
    }
}
