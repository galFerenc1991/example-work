package com.ferenc.pamp.presentation.screens.auth.login;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.ferenc.pamp.PampApp;
import com.ferenc.pamp.R;
import com.ferenc.pamp.domain.AuthRepository;
import com.ferenc.pamp.presentation.base.BasePresenter;
import com.ferenc.pamp.presentation.base.content.ContentFragment;
import com.ferenc.pamp.presentation.screens.main.MainActivity_;
import com.ferenc.pamp.presentation.utils.Constants;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.jakewharton.rxbinding2.view.RxView;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * Created by
 * Ferenc on 2017.11.13..
 */
@EFragment
public class LoginFragment extends ContentFragment implements LoginContract.View {
    @Override
    public void setPresenter(LoginContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fargment_login;
    }

    @Override
    protected BasePresenter getPresenter() {
        return mPresenter;
    }

    private LoginContract.Presenter mPresenter;
    private CallbackManager callbackManager;
    private GoogleSignInOptions gso;
    private GoogleSignInClient mGoogleSignInClient;

    @ViewById(R.id.ivBack_FL)
    protected ImageView btnBack;
    @ViewById(R.id.btnLoginWithPamp_FL)
    protected FrameLayout btnLoginWithPamp;
    @ViewById(R.id.btnLoginFacebook_FL)
    protected FrameLayout btnLoginFacebook;
    @ViewById(R.id.btnLoginGoogle_FL)
    protected FrameLayout btnLoginWithGoogle;

    @ViewById(R.id.etEmail_FL)
    protected EditText etEmail;
    @ViewById(R.id.etPassword_FL)
    protected EditText etPassword;

    @ViewById(R.id.tvForgotPassword_FL)
    protected TextView tvForgotPassword;


    @ViewById(R.id.tilEmail_FL)
    protected TextInputLayout tilEmail;
    @ViewById(R.id.tilPassword_FL)
    protected TextInputLayout tilPassword;

    @StringRes(R.string.err_msg_required)
    protected String errMsgRequired;
    @StringRes(R.string.err_msg_invalid_email)
    protected String errMsgInvalidEmail;
    @StringRes(R.string.err_msg_invalid_password)
    protected String errMsgInvalidPassword;
    @StringRes(R.string.forgot_password__message)
    protected String mMessage;
    @StringRes(R.string.forgot_password_dialog_title)
    protected String mTitle;
    @StringRes(R.string.button_cancel)
    protected String textCancel;
    @StringRes(R.string.button_send)
    protected String textSend;

    @Bean
    protected AuthRepository mAuthRepository;

    @AfterInject
    @Override
    public void initPresenter() {
        new LoginPresenter(this, mAuthRepository);

        createAndRegistrationFacebookCallbackManager();

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(Constants.GOOGLE_CLIENT_ID)
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
    }

    @AfterViews
    protected void initUI() {
        RxView.clicks(btnBack)
                .throttleFirst(Constants.CLICK_DELAY, TimeUnit.MILLISECONDS)
                .subscribe(o -> mPresenter.backToAuthScreen());

        RxView.clicks(btnLoginWithPamp)
                .throttleFirst(Constants.CLICK_DELAY, TimeUnit.MILLISECONDS)
                .subscribe(o -> mPresenter.loginWithPamp(etEmail.getText().toString(), etPassword.getText().toString()));

        RxView.clicks(btnLoginFacebook)
                .throttleFirst(Constants.CLICK_DELAY, TimeUnit.MILLISECONDS)
                .subscribe(o -> clickedFacebook());

        RxView.clicks(btnLoginWithGoogle)
                .throttleFirst(Constants.CLICK_DELAY, TimeUnit.MILLISECONDS)
                .subscribe(o -> signInWithGoogle());

        RxView.clicks(tvForgotPassword)
                .throttleFirst(Constants.CLICK_DELAY, TimeUnit.MILLISECONDS)
                .subscribe(o -> mPresenter.clickedForgotPassword());
    }

    public void createAndRegistrationFacebookCallbackManager() {
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                mPresenter.loginWithFacebook(loginResult.getAccessToken().getToken());
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                Log.d("error", "" + error);
            }
        });
    }

    private void clickedFacebook() {
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "user_friends", "email", "user_location"));
    }

    private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, Constants.REQUEST_CODE_GOOGLE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.REQUEST_CODE_GOOGLE) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            mPresenter.loginWithGoogle(account.getIdToken());
        } catch (ApiException e) {
            Log.d("Google", "signInResult:failed code=" + e.getStatusCode());
        }
    }


    @Override
    public void openAuthScreen() {
        mActivity.getSupportFragmentManager().popBackStack();
    }

    @Override
    public void openHomeScreen() {
        MainActivity_.intent(this)
                .flags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK)
                .start();
    }

    @Override
    public void openForgotPasswordPopUp() {
        AlertDialog.Builder alert = new AlertDialog.Builder(mActivity, R.style.DialogTheme);
        View view = View.inflate(mActivity, R.layout.view_edit_text, null);
        alert.setTitle(mTitle);
        alert.setMessage(mMessage);
        alert.setView(view);
        alert.setPositiveButton(textSend, (dialogInterface, i) -> {
            EditText editText = view.findViewById(R.id.dialog1Edittext);
            String text = editText.getText().toString();
            mPresenter.sendNewPassword(text);
        });

        alert.setNegativeButton(textCancel, (dialogInterface, i) -> {
        });
        alert.setCancelable(false);
        alert.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.unsubscribe();
    }

    ///// Input fields error handling/////

    @Override
    public void showErrorEmailRequired() {
        tilEmail.setError(errMsgRequired);
    }

    @Override
    public void showErrorEmailInvalid() {
        tilEmail.setError(errMsgInvalidEmail);
    }

    @Override
    public void showErrorPasswordRequired() {
        tilPassword.setError(errMsgRequired);
    }

    @Override
    public void showErrorPasswordInvalid() {
        tilPassword.setError(errMsgInvalidPassword);
    }

    @Override
    public void hideEmailError() {
        tilEmail.setErrorEnabled(false);
        tilEmail.setError(null);
    }

    @Override
    public void hidePasswordError() {
        tilPassword.setErrorEnabled(false);
        tilPassword.setError(null);
    }
}
