package com.ferenc.pamp.presentation.screens.main.home;

import com.ferenc.pamp.presentation.utils.Constants;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by
 * Ferenc on 2017.11.17..
 */

public class HomePresenter implements HomeContract.Presenter {

    private HomeContract.View mView;
    private HomeContract.Model mModel;
    private CompositeDisposable mCompositeDisposable;

    public HomePresenter(HomeContract.View mView, HomeContract.Model mModel) {
        this.mView = mView;
        this.mModel = mModel;
        this.mCompositeDisposable = new CompositeDisposable();

        mView.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void signOut() {
        mView.showProgressMain();
        mCompositeDisposable.add(mModel.signOut()
                .subscribe(signUpResponse -> {
                    mView.hideProgress();
                    mView.openAuthScreen();
                }, throwable -> {
                    mView.hideProgress();
                    mView.showErrorMessage(Constants.MessageType.UNKNOWN);
                }));
    }

    @Override
    public void unsubscribe() {

    }
}
