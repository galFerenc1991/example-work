package com.kubator.pamp.presentation.screens.main.profile;

import com.kubator.pamp.data.model.auth.SignUpResponse;
import com.kubator.pamp.data.model.common.User;
import com.kubator.pamp.presentation.base.BaseModel;
import com.kubator.pamp.presentation.base.BasePresenter;
import com.kubator.pamp.presentation.base.BaseView;
import com.kubator.pamp.presentation.base.content.ContentView;

import io.reactivex.Observable;

/**
 * Created by
 * Ferenc on 2017.12.04..
 */

public interface ProfileContract {

    interface View extends ContentView, BaseView<Presenter> {
        void logOut();
        void openEditProfile();
        void setUserProfilePictureAndName(String _avatarUrl, String _name);
        void sharePamp();

        void showMyOrders();

        void openMailSender();

        void openAbout();
    }

    interface Presenter extends BasePresenter {
        void clickedLogOut();
        void clickedProfileInformation();
        void clickedSharePamp();

        void clickedMyOrders();

        void clickContactUs();

        void clickAbout();
    }

    interface SignOutModel extends BaseModel {
        Observable<SignUpResponse> signOut();
    }
    interface UserProfileModel extends BaseModel{
        Observable<User> getUserProfile();
    }
}
