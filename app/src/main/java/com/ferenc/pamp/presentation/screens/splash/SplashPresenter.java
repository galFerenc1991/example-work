package com.ferenc.pamp.presentation.screens.splash;

import android.text.TextUtils;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by
 * Ferenc on 2017.11.10..
 */

public class SplashPresenter implements SplashContract.Presenter {

    private SplashContract.View mView;
    private CompositeDisposable mCompositeDisposable;
    private boolean mIsViwedTutorial;
    private String mToken;

    public SplashPresenter(SplashContract.View _view, boolean _isViewedTutorial, String _token) {
        this.mView = _view;
        mCompositeDisposable = new CompositeDisposable();
        mIsViwedTutorial = _isViewedTutorial;
        this.mToken = _token;

        mView.setPresenter(this);
    }

    @Override
    public void subscribe() {
        mCompositeDisposable.add(Observable.just(mIsViwedTutorial)
                .delay(3, TimeUnit.SECONDS)
                .subscribe(aBoolean -> {
                    if (!aBoolean) {
                        mView.startTutorial();
                    } else if (!TextUtils.isEmpty(mToken)) {
                        mView.openMain();
                    } else {
                        mView.openAuth();
                    }
                }));
    }

    @Override
    public void unsubscribe() {
        mCompositeDisposable.clear();
    }
}
