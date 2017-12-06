package com.ferenc.pamp.presentation.screens.main.profile;


import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by
 * Ferenc on 2017.12.04..
 */

public class ProfilePresenter implements ProfileContract.Presenter {

    private ProfileContract.View mView;
    private ProfileContract.Model mModel;
    private CompositeDisposable mCompositeDisposable;

    public ProfilePresenter(ProfileContract.View _view, ProfileContract.Model _model) {
        this.mView = _view;
        this.mModel = _model;
        this.mCompositeDisposable = new CompositeDisposable();

        mView.setPresenter(this);
    }

    @Override
    public void subscribe() {

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

    }

    @Override
    public void unsubscribe() {
        mCompositeDisposable.clear();
    }
}
