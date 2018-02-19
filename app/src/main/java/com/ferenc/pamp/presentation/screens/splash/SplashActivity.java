package com.ferenc.pamp.presentation.screens.splash;

import android.support.v7.app.AppCompatActivity;

import com.ferenc.pamp.R;
import com.ferenc.pamp.presentation.screens.auth.AuthActivity_;
import com.ferenc.pamp.presentation.screens.main.MainActivity_;
import com.ferenc.pamp.presentation.screens.tutorial.TutorialActivity_;
import com.ferenc.pamp.presentation.utils.SharedPrefManager_;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.sharedpreferences.Pref;

/**
 * Created by
 * Ferenc on 2017.11.10..
 */
@EActivity(R.layout.activity_splash)
public class SplashActivity extends AppCompatActivity implements SplashContract.View {

    private SplashContract.Presenter mPresenter;

    @Pref
    protected SharedPrefManager_ mSharedPrefManager;

    @AfterInject
    @Override
    public void initPresenter() {
        new SplashPresenter(this, mSharedPrefManager.getIsViewedTutorial().get(), mSharedPrefManager.getAccessToken().get());
        mPresenter.subscribe();
    }

    @Override
    public void setPresenter(SplashContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void startTutorial() {
        TutorialActivity_.intent(this).start();
    }
    @Override
    public void openAuth() {
        AuthActivity_.intent(this).start();
    }

    @Override
    public void openMain() {
        MainActivity_.intent(this).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.unsubscribe();
    }
}
