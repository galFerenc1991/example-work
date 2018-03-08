package com.ferenc.pamp.presentation.screens.auth.sign_up;

import android.content.Intent;
import android.util.Log;
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
import com.ferenc.pamp.R;
import com.ferenc.pamp.domain.AuthRepository;
import com.ferenc.pamp.presentation.base.BasePresenter;
import com.ferenc.pamp.presentation.base.content.ContentFragment;
import com.ferenc.pamp.presentation.screens.auth.sign_up.country_picker.CountryPickerActivity_;
import com.ferenc.pamp.presentation.screens.auth.sign_up.create_password.CreatePasswordFragment_;
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
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * Created by
 * Ferenc on 2017.11.13..
 */
@EFragment
public class SignUpFragment extends ContentFragment implements SignUpContract.View {
    @Override
    public void setPresenter(SignUpContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_sign_up;
    }

    @Override
    protected BasePresenter getPresenter() {
        return mPresenter;
    }

    private SignUpContract.Presenter mPresenter;
    private CallbackManager callbackManager;

    private GoogleSignInOptions gso;
    private GoogleSignInClient mGoogleSignInClient;

    @ViewById(R.id.ivBack_FSU)
    protected ImageView ivBack;
    @ViewById(R.id.btnCreatePamp_FSU)
    protected FrameLayout btnCreatePamp;
    @ViewById(R.id.btnLoginFacebook_FSU)
    protected FrameLayout btnCreateFacebook;
    @ViewById(R.id.btnLoginGoogle_FSU)
    protected FrameLayout btnCreateGoogle;
    @ViewById(R.id.llCountrySpinner_FSU)
    protected LinearLayout btnCountrySpinner;

    @ViewById(R.id.etSurname_FSU)
    protected EditText etSurname;
    @ViewById(R.id.etName_FSU)
    protected EditText etName;
    @ViewById(R.id.etEmail_FSU)
    protected EditText etEmail;
    @ViewById(R.id.tvCountry_FSU)
    protected TextView tvCountry;

    @Bean
    protected AuthRepository mAuthRepository;

    @AfterInject
    @Override
    public void initPresenter() {
        new SignUpPresenter(this, mAuthRepository);

        createAndRegistrationFacebookCallbackManager();

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(Constants.GOOGLE_CLIENT_ID)
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
    }

    @AfterViews
    protected void initUI() {
        RxView.clicks(ivBack)
                .throttleFirst(Constants.CLICK_DELAY, TimeUnit.MILLISECONDS)
                .subscribe(o -> mPresenter.backToAuthScreen());
        RxView.clicks(btnCreatePamp)
                .throttleFirst(Constants.CLICK_DELAY, TimeUnit.MILLISECONDS)
                .subscribe(o -> mPresenter.openCreatePasswordScreen(etName.getText().toString()
                        , etSurname.getText().toString()
                        , etEmail.getText().toString()
                        , tvCountry.getText().toString()));

        RxView.clicks(btnCreateFacebook)
                .throttleFirst(Constants.CLICK_DELAY, TimeUnit.MILLISECONDS)
                .subscribe(o -> clickedFacebook());
        RxView.clicks(btnCreateGoogle)
                .throttleFirst(Constants.CLICK_DELAY, TimeUnit.MILLISECONDS)
                .subscribe(o -> signInWithGoogle());
        RxView.clicks(btnCountrySpinner)
                .throttleFirst(Constants.CLICK_DELAY, TimeUnit.MILLISECONDS)
                .subscribe(o -> mPresenter.selectCountry());
    }

    public void createAndRegistrationFacebookCallbackManager() {
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                mPresenter.loginOrCreateWithFacebook(loginResult.getAccessToken().getToken());
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
        LoginManager.getInstance().logOut();
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
            mPresenter.loginOrCreateWithGoogle(account.getIdToken());
        } catch (ApiException e) {
            Log.d("Google", "signInResult:failed code=" + e.getStatusCode());
        }
    }

    @Override
    public void setCountry(String _selectedCountry) {
        tvCountry.setText(_selectedCountry);
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
    public void openCreatePasswordScreen(String _firstName, String _lastName, String _email, String _country) {
        mActivity.replaceFragment(CreatePasswordFragment_
                .builder()
                .mFirstName(_firstName)
                .mLastName(_lastName)
                .mEmail(_email)
                .mCountry(_country)
                .build());
    }

    @Override
    public void startSelectCountryScreen(String _country) {
        CountryPickerActivity_.intent(this)
                .extra(Constants.KEY_COUNTRY, _country)
                .startForResult(Constants.REQUEST_CODE_COUNTRY_PICKER);
    }

    @OnActivityResult(Constants.REQUEST_CODE_COUNTRY_PICKER)
    protected void resultCountryCode(@OnActivityResult.Extra(Constants.KEY_COUNTRY) String country) {
        mPresenter.setSelectedCountry(country);
    }
}
