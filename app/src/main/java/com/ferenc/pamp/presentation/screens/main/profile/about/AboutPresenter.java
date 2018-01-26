package com.ferenc.pamp.presentation.screens.main.profile.about;


import android.util.Log;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by shonliu on 1/25/18.
 */

public class AboutPresenter implements AboutContract.Presenter{

    private AboutContract.View mView;
    private CompositeDisposable mCompositeDisposable;
    private AboutContract.Model mModel;

    public AboutPresenter(AboutContract.View _view, AboutContract.Model _model) {
        mView = _view;
        mModel = _model;
        mCompositeDisposable = new CompositeDisposable();
        mView.setPresenter(this);
    }

    @Override
    public void subscribe() {
        mView.initClickListeners();
        mView.initBar();
    }

    @Override
    public void unsubscribe() {
        mCompositeDisposable.clear();
    }

    @Override
    public void clickCGU() {
        mView.showProgressBar();
        mCompositeDisposable.add(mModel.getCGU()
                .subscribe(cgu -> {
                    mView.hideProgressBar();
                    mView.showHTML(cgu.string());
                    Log.d("CGU success", cgu.string());
                }, throwable -> {
                    mView.hideProgressBar();
                    Log.d("CGU error", throwable.getMessage());
                })
        );
    }

    @Override
    public void clickRules() {
        mView.showProgressBar();
        mCompositeDisposable.add(mModel.getRules()
                .subscribe(rules -> {
                    mView.hideProgressBar();
                    mView.showHTML(rules.string());
                    Log.d("Rules success", rules.string());
                }, throwable -> {
                    mView.hideProgressBar();
                    Log.d("Rules error", throwable.getMessage());
                })
        );
    }

    @Override
    public void clickAboutUs() {
        mView.showProgressBar();
        mCompositeDisposable.add(mModel.getAboutUs()
                .subscribe(aboutUs -> {
                    mView.hideProgressBar();
                    mView.showHTML(aboutUs.string());
                    Log.d("AboutUs success", aboutUs.string());
                }, throwable -> {
                    mView.hideProgressBar();
                    Log.d("AboutUs error", throwable.getMessage());
                })
        );
    }
}
