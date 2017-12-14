package com.ferenc.pamp.presentation.screens.main.profile;

import com.ferenc.pamp.data.model.auth.SignUpResponse;
import com.ferenc.pamp.data.model.common.User;
import com.ferenc.pamp.presentation.base.BaseModel;
import com.ferenc.pamp.presentation.base.BasePresenter;
import com.ferenc.pamp.presentation.base.BaseView;
import com.ferenc.pamp.presentation.base.content.ContentView;

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
    }

    interface Presenter extends BasePresenter {
        void clickedLogOut();
        void clickedProfileInformation();
        void clickedSharePamp();

    }

    interface SignOutModel extends BaseModel {
        Observable<SignUpResponse> signOut();
    }
    interface UserProfileModel extends BaseModel{
        Observable<User> getUserProfile();
    }
}
