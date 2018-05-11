package com.kubator.pamp.presentation.screens.main.profile.edit_profile.change_password;

import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.kubator.pamp.R;
import com.kubator.pamp.domain.UserRepository;
import com.kubator.pamp.presentation.base.BasePresenter;
import com.kubator.pamp.presentation.base.content.ContentFragment;
import com.kubator.pamp.presentation.utils.Constants;
import com.jakewharton.rxbinding2.view.RxView;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;

import java.util.concurrent.TimeUnit;

/**
 * Created by
 * Ferenc on 2018.01.10..
 */

@EFragment
public class ChangePasswordFragment extends ContentFragment implements ChangePasswordContract.View {
    @Override
    public void setPresenter(ChangePasswordContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_change_password;
    }

    @Override
    protected BasePresenter getPresenter() {
        return mPresenter;
    }

    private ChangePasswordContract.Presenter mPresenter;

    @ViewById(R.id.tilCurrPassword_FCP)
    protected TextInputLayout tilCurrPassword;
    @ViewById(R.id.etCurrPassword_FCP)
    protected EditText etCurrPassword;
    @ViewById(R.id.tilNewPassword_FCP)
    protected TextInputLayout tilNewPassword;
    @ViewById(R.id.etNewPassword_FCP)
    protected EditText etNewPassword;
    @ViewById(R.id.tilConfPassword_FCP)
    protected TextInputLayout tilConfPassword;
    @ViewById(R.id.etConfPassword_FCP)
    protected EditText etConfPassword;
    @ViewById(R.id.btnConfirm_FCP)
    protected Button btnConfirm;

    @StringRes(R.string.err_msg_invalid_password)
    protected String errMsgInvalidPassword;
    @StringRes(R.string.err_msg_required)
    protected String errMsgRequired;
    @StringRes(R.string.err_msg_passwords_not_matches)
    protected String errMsgNotMatches;
    @StringRes(R.string.err_msg_entered_password_is_incorrect)
    protected String errMsgEnteredIncorrectPassword;
    @StringRes(R.string.title_change_password)
    protected String titleChangePassword;

    @Bean
    protected UserRepository mUserRepository;

    @AfterInject
    @Override
    public void initPresenter() {
        new ChangePasswordPresenter(this, mUserRepository);
    }

    @AfterViews
    protected void initUI() {

        RxView.clicks(btnConfirm)
                .throttleFirst(Constants.CLICK_DELAY, TimeUnit.MILLISECONDS)
                .subscribe(o -> mPresenter.setNewPassword(etCurrPassword.getText().toString(),
                        etNewPassword.getText().toString(),
                        etConfPassword.getText().toString()));
    }

    @Override
    public void showErrorNewPasswordRequired() {
        tilNewPassword.setError(errMsgRequired);
    }

    @Override
    public void showErrorOldPasswordRequired() {
        tilCurrPassword.setError(errMsgRequired);
    }

    @Override
    public void showErrorOldPasswordInvalid() {
        tilCurrPassword.setError(errMsgInvalidPassword);
    }

    @Override
    public void showErrorEnteredOldPasswordIncorrect() {
        tilCurrPassword.setError(errMsgEnteredIncorrectPassword);
    }

    @Override
    public void showErrorNewPasswordInvalid() {
        tilNewPassword.setError(errMsgInvalidPassword);
    }

    @Override
    public void hideNewPasswordError() {
        tilNewPassword.setError(null);
    }

    @Override
    public void hideConfPasswordError() {
        tilConfPassword.setError(null);
    }

    @Override
    public void showErrorPassNotMatch() {
        tilConfPassword.setError(errMsgNotMatches);
    }


    @Override
    public void closeChangePasswordScreen() {
        InputMethodManager imm = (InputMethodManager)mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etConfPassword.getWindowToken(), 0);
        mActivity.finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.unsubscribe();
    }
}
