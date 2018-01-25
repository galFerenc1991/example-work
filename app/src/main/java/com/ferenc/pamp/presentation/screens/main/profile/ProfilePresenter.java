package com.ferenc.pamp.presentation.screens.main.profile;


import com.ferenc.pamp.data.model.common.User;
import com.ferenc.pamp.presentation.utils.SignedUserManager;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by
 * Ferenc on 2017.12.04..
 */

public class ProfilePresenter implements ProfileContract.Presenter {

    private ProfileContract.View mView;
    private ProfileContract.SignOutModel mModel;
    private ProfileContract.UserProfileModel mProfileModel;
    private CompositeDisposable mCompositeDisposable;
    private SignedUserManager mUserManager;
    private UserRelay mUserRelay;

    public ProfilePresenter(ProfileContract.View _view
            , ProfileContract.SignOutModel _model
            , ProfileContract.UserProfileModel _profileModel
            , SignedUserManager _userManager
            , UserRelay _userRelay) {

        this.mView = _view;
        this.mModel = _model;
        this.mProfileModel = _profileModel;
        this.mUserManager = _userManager;
        this.mUserRelay = _userRelay;
        this.mCompositeDisposable = new CompositeDisposable();

        mView.setPresenter(this);
    }

    @Override
    public void subscribe() {
        User signedUser = mUserManager.getCurrentUser();
        if (signedUser != null) {
            mView.setUserProfilePictureAndName(signedUser.getAvatarUrl(), signedUser.getFirstName());
            mCompositeDisposable.add(mUserRelay.userRelay.subscribe(user -> {
                mView.setUserProfilePictureAndName(user.getAvatarUrl(), user.getFirstName());
            }));
        } else {
            mView.showProgressMain();
            mCompositeDisposable.add(mProfileModel.getUserProfile()
                    .subscribe(user -> {
                        mView.hideProgress();
                        mView.setUserProfilePictureAndName(user.getAvatarUrl(), user.getFirstName());
                    }, throwable -> {
                        mView.hideProgress();
                    }));
        }
    }

    @Override
    public void clickedSharePamp() {
        mView.sharePamp();
    }

    @Override
    public void clickedMyOrders() {
        mView.showMyOrders();
    }

    @Override
    public void clickContactUs() {
        mView.openMailSender();
    }

    @Override
    public void clickAbout() {
        mView.openAbout();
    }

    @Override
    public void clickedLogOut() {
        mView.showProgressMain();
        mCompositeDisposable.add(mModel.signOut()
                .subscribe(signUpResponse -> {
                    mView.hideProgress();
                    mView.logOut();
                }, throwable -> {
                    mView.hideProgress();
                }));
    }

    @Override
    public void clickedProfileInformation() {
        mView.openEditProfile();
    }

    @Override
    public void unsubscribe() {
        mCompositeDisposable.clear();
    }
}
