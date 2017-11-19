package com.ferenc.pamp.presentation.screens.main.home;

import android.content.Intent;
import android.widget.Button;

import com.ferenc.pamp.R;
import com.ferenc.pamp.domain.AuthRepository;
import com.ferenc.pamp.presentation.base.BasePresenter;
import com.ferenc.pamp.presentation.base.content.ContentFragment;
import com.ferenc.pamp.presentation.screens.auth.AuthActivity_;
import com.ferenc.pamp.presentation.utils.Constants;
import com.jakewharton.rxbinding2.view.RxView;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.concurrent.TimeUnit;

/**
 * Created by
 * Ferenc on 2017.11.17..
 */

@EFragment
public class HomeFragment extends ContentFragment implements HomeContract.View {
    @Override
    public void setPresenter(HomeContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_home;
    }

    @Override
    protected BasePresenter getPresenter() {
        return mPresenter;
    }

    private HomeContract.Presenter mPresenter;

    @ViewById(R.id.btnSignOut_FH)
    protected Button btnSignOut;

    @Bean
    protected AuthRepository mAuthRepository;

    @AfterInject
    @Override
    public void initPresenter() {
        new HomePresenter(this, mAuthRepository);
    }

    @AfterViews
    protected void initUI() {
        RxView.clicks(btnSignOut)
                .throttleFirst(Constants.CLICK_DELAY, TimeUnit.MILLISECONDS)
                .subscribe(o -> mPresenter.signOut());
    }

    @Override
    public void openAuthScreen() {
        AuthActivity_.intent(this)
                .flags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK)
                .start();
    }
}
