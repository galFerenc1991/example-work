package com.ferenc.pamp.presentation.screens.auth;

import android.widget.FrameLayout;

import com.ferenc.pamp.R;
import com.ferenc.pamp.presentation.base.BasePresenter;
import com.ferenc.pamp.presentation.base.content.ContentFragment;
import com.ferenc.pamp.presentation.screens.auth.login.LoginFragment_;
import com.ferenc.pamp.presentation.screens.auth.sign_up.SignUpFragment_;
import com.ferenc.pamp.presentation.utils.Constants;
import com.jakewharton.rxbinding2.view.RxView;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.concurrent.TimeUnit;

/**
 * Created by
 * Ferenc on 2017.11.13..
 */
@EFragment
public class AuthFragment extends ContentFragment implements AuthContract.View {
    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_auth;
    }

    @Override
    protected BasePresenter getPresenter() {
        return mPresenter;
    }

    @Override
    public void setPresenter(AuthContract.Presenter presenter) {
        mPresenter = presenter;
    }

    private AuthContract.Presenter mPresenter;

    @ViewById(R.id.btnLoginPamp_FA)
    protected FrameLayout btnLoginPamp;
    @ViewById(R.id.btnSignInEmail_FA)
    protected FrameLayout btnSignInEmail;

    @AfterInject
    @Override
    public void initPresenter() {
        new AuthPresenter(this);
    }

    @AfterViews
    protected void initUI() {
        RxView.clicks(btnLoginPamp)
                .throttleFirst(Constants.CLICK_DELAY, TimeUnit.MILLISECONDS)
                .subscribe(o -> mPresenter.signUp());

        RxView.clicks(btnSignInEmail)
                .throttleFirst(Constants.CLICK_DELAY, TimeUnit.MILLISECONDS)
                .subscribe(o -> mPresenter.login());

    }

    @Override
    public void startLoginScreen() {
        mActivity.replaceFragment(LoginFragment_
                .builder()
                .build());
    }

    @Override
    public void startSignUpScreen() {
        mActivity.replaceFragment(SignUpFragment_
                .builder()
                .build());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.unsubscribe();
    }
}
