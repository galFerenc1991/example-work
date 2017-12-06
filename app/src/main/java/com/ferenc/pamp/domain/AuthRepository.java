package com.ferenc.pamp.domain;

import android.util.Log;

import com.ferenc.pamp.data.api.Rest;
import com.ferenc.pamp.data.model.auth.ForgotPasswordRequest;
import com.ferenc.pamp.data.model.auth.SignInRequest;
import com.ferenc.pamp.data.model.auth.SignUpRequest;
import com.ferenc.pamp.data.model.auth.SignUpResponse;
import com.ferenc.pamp.data.model.auth.TokenRequest;
import com.ferenc.pamp.data.model.common.User;
import com.ferenc.pamp.data.service.AuthService;
import com.ferenc.pamp.presentation.screens.auth.login.LoginContract;
import com.ferenc.pamp.presentation.screens.auth.sign_up.SignUpContract;
import com.ferenc.pamp.presentation.screens.auth.sign_up.country_picker.CountryContract;
import com.ferenc.pamp.presentation.screens.auth.sign_up.create_password.CreatePasswordContract;
import com.ferenc.pamp.presentation.screens.main.profile.ProfileContract;
import com.ferenc.pamp.presentation.utils.SharedPrefManager_;
import com.ferenc.pamp.presentation.utils.SignedUserManager;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.util.Arrays;
import java.util.List;

import io.reactivex.Observable;

/**
 * Created by
 * Ferenc on 2017.11.16..
 */
@EBean(scope = EBean.Scope.Singleton)
public class AuthRepository extends NetworkRepository implements CreatePasswordContract.Model, LoginContract.Model, SignUpContract.Model, CountryContract.Model ,ProfileContract.Model{
    @Bean
    protected Rest rest;
    @Bean
    protected SignedUserManager mSignedUserManager;

    @Pref
    protected SharedPrefManager_ mSharedPrefManager;

    private AuthService authService;

    @AfterInject
    protected void initServices() {
        authService = rest.getAuthService();
    }

    @Override
    public Observable<SignUpResponse> signUp(SignUpRequest _request) {
        return getNetworkObservable(authService.signUp(_request));
    }

    @Override
    public Observable<User> signIn(SignInRequest request) {
        return getNetworkObservable(authService.signIn(request))
                .flatMap(user -> {
                    mSharedPrefManager.edit()
                            .getAccessToken()
                            .put("Bearer " + user.getToken())
                            .apply();
                    Log.d("Token", "Writed token: " + mSharedPrefManager.getAccessToken().get());
                    return Observable.just(user);
                });
    }

    @Override
    public Observable<User> signInWithFacebook(TokenRequest _token) {
        return getNetworkObservable(authService.signInFacebook(_token))
                .flatMap(user -> {
                    mSharedPrefManager.edit()
                            .getAccessToken()
                            .put("Bearer " + user.getToken())
                            .apply();
                    Log.d("Token", "Writed token: " + mSharedPrefManager.getAccessToken().get());
                    return Observable.just(user);
                });
    }

    @Override
    public Observable<User> createOrLoginWithFacebook(TokenRequest _token) {
        return getNetworkObservable(authService.signInFacebook(_token))
                .flatMap(user -> {
                    mSharedPrefManager.edit()
                            .getAccessToken()
                            .put("Bearer " + user.getToken())
                            .apply();
                    Log.d("Token", "Writed token: " + mSharedPrefManager.getAccessToken().get());
                    return Observable.just(user);
                });
    }

    @Override
    public Observable<User> signInWithGoogle(TokenRequest _tokenRequest) {
        return getNetworkObservable(authService.signInGoogle(_tokenRequest))
                .flatMap(user -> {
                    mSharedPrefManager.edit()
                            .getAccessToken()
                            .put("Bearer " + user.getToken())
                            .apply();
                    Log.d("Token", "Writed token: " + mSharedPrefManager.getAccessToken().get());
                    return Observable.just(user);
                });
    }

    @Override
    public Observable<User> createOrLoginWithGoogle(TokenRequest _token) {
        return getNetworkObservable(authService.signInGoogle(_token))
                .flatMap(user -> {
                    mSharedPrefManager.edit()
                            .getAccessToken()
                            .put("Bearer " + user.getToken())
                            .apply();
                    Log.d("Token", "Writed token: " + mSharedPrefManager.getAccessToken().get());
                    return Observable.just(user);
                });
    }

    @Override
    public Observable<SignUpResponse> signOut() {
        return getNetworkObservable(authService.signOut())
                .flatMap(signUpResponse -> {
                    mSharedPrefManager.edit()
                            .getAccessToken()
                            .remove()
                            .apply();
                    Log.d("Token", "Writed token: " + mSharedPrefManager.getAccessToken().get());
                    return Observable.just(signUpResponse);
                });
    }

    @Override
    public Observable<SignUpResponse> forgotPassword(ForgotPasswordRequest request) {
        return getNetworkObservable(authService.forgotPassword(request));
    }

    @Override
    public Observable<List<String>> getCountryList() {
        return getNetworkObservable(authService.getCountryList())
                .flatMap(countryList -> {
                 List<String> country = Arrays.asList(countryList.getCountry());
                    return Observable.just(country);
                });
    }
}