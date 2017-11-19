package com.ferenc.pamp.presentation.screens.auth.sign_up.create_password;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.ferenc.pamp.R;
import com.ferenc.pamp.domain.AuthRepository;
import com.ferenc.pamp.presentation.base.BasePresenter;
import com.ferenc.pamp.presentation.base.content.ContentFragment;
import com.ferenc.pamp.presentation.screens.auth.login.LoginFragment_;
import com.ferenc.pamp.presentation.utils.Constants;
import com.jakewharton.rxbinding2.view.RxView;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;


import java.util.concurrent.TimeUnit;

/**
 * Created by
 * Ferenc on 2017.11.13..
 */
@EFragment
public class CreatePasswordFragment extends ContentFragment implements CreatePasswordContract.View {
    @Override
    public void setPresenter(CreatePasswordContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_create_password;
    }

    @Override
    protected BasePresenter getPresenter() {
        return mPresenter;
    }

    private CreatePasswordContract.Presenter mPresenter;

    @ViewById(R.id.ivBack_FCP)
    protected ImageView btnBack;
    @ViewById(R.id.btnConfirm_FCP)
    protected Button btnConfirm;
    @ViewById(R.id.etPassword_FCP)
    protected EditText etPassword;
    @ViewById(R.id.etConfPassword_FCP)
    protected EditText etConfPassword;

    @FragmentArg
    protected String mFirstName;
    @FragmentArg
    protected String mLastName;
    @FragmentArg
    protected String mEmail;
    @FragmentArg
    protected String mCountry;

    @StringRes(R.string.button_ok)
    protected String ok;
    @StringRes(R.string.verification_dialog_title)
    protected String mDialogTitle;
    @StringRes(R.string.verification_dialog_message)
    protected String mDialogMessage;

    @Bean
    protected AuthRepository mAuthRepository;

    @AfterInject
    @Override
    public void initPresenter() {
        new CreatePasswordPresenter(this, mAuthRepository);
    }

    @AfterViews
    protected void initUI() {
        RxView.clicks(btnBack)
                .throttleFirst(Constants.CLICK_DELAY, TimeUnit.MILLISECONDS)
                .subscribe(o -> mPresenter.backToSignUpScreen());

        RxView.clicks(btnConfirm)
                .throttleFirst(Constants.CLICK_DELAY, TimeUnit.MILLISECONDS)
                .subscribe(o -> mPresenter.signUp(mFirstName
                        , mLastName
                        , mEmail
                        , mCountry
                        , etPassword.getText().toString()
                        , etConfPassword.getText().toString()));
    }

    @Override
    public void openSignUpScreen() {
        mActivity.getSupportFragmentManager().popBackStack();
    }

    @Override
    public void openVerificationPopUpDialog() {
        int bigTextSize = getResources().getDimensionPixelSize(R.dimen.text_size_title);
        int smallTextSize = getResources().getDimensionPixelSize(R.dimen.text_size_default);

        Spannable titleSpannable = new SpannableString(mDialogTitle);
        Spannable messageSpannable = new SpannableString(mDialogMessage);

        titleSpannable.setSpan(new StyleSpan(Typeface.NORMAL), 0, 26, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        titleSpannable.setSpan(new ForegroundColorSpan(Color.BLACK), 0, 26, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        titleSpannable.setSpan(new AbsoluteSizeSpan(bigTextSize), 0, 26, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        messageSpannable.setSpan(new StyleSpan(Typeface.NORMAL), 0, 26, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        messageSpannable.setSpan(new ForegroundColorSpan(Color.BLACK), 0, 26, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        messageSpannable.setSpan(new AbsoluteSizeSpan(smallTextSize), 0, 26, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity, R.style.DialogTheme);
        builder.setMessage(messageSpannable)
                .setTitle(titleSpannable)
                .setPositiveButton(ok, (dialog, which) -> {
                    mPresenter.openLoginScreen();
                })
                .setCancelable(false)
                .create()
                .show();
    }

    @Override
    public void openLoginScreen() {
        mActivity.replaceFragmentClearBackstack(LoginFragment_.builder().build());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.unsubscribe();
    }
}
