package com.kubator.pamp.presentation.screens.tutorial;

import com.kubator.pamp.presentation.utils.SharedPrefManager_;

/**
 * Created by
 * Ferenc on 2017.11.12..
 */

public class TutorialPresenter implements TutorialContract.Presenter {

    private TutorialContract.View mView;
    private SharedPrefManager_ mSharedPrefManager;


    public TutorialPresenter(TutorialContract.View _view, SharedPrefManager_ _sharedPrefManager) {
        this.mView = _view;
        this.mSharedPrefManager = _sharedPrefManager;

        mView.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void skip() {
        mSharedPrefManager.edit()
                .getIsViewedTutorial()
                .put(true)
                .apply();
        mView.openAuth();
    }

    @Override
    public void unsubscribe() {

    }
}
