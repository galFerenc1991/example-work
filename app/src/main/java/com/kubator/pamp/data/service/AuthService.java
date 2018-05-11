package com.kubator.pamp.data.service;

import com.kubator.pamp.data.model.auth.CountryList;
import com.kubator.pamp.data.model.auth.ForgotPasswordRequest;
import com.kubator.pamp.data.model.auth.RefreshTokenResponse;
import com.kubator.pamp.data.model.auth.SignInRequest;
import com.kubator.pamp.data.model.auth.SignUpRequest;
import com.kubator.pamp.data.model.auth.SignUpResponse;
import com.kubator.pamp.data.model.auth.TokenRequest;
import com.kubator.pamp.data.model.common.User;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;

/**
 * Created by
 * Ferenc on 2017.11.16..
 */

public interface AuthService {

    @POST("token/renew/")
    Observable<RefreshTokenResponse> refreshToken();

    @PUT("/user/signout")
    Observable<SignUpResponse> signOut();

    @PUT("/user/forgotPassword")
    Observable<SignUpResponse> forgotPassword(@Body ForgotPasswordRequest request);

    @POST("/user/signin/")
    Observable<User> signIn(@Body SignInRequest request);

    @POST("/user/facebook/")
    Observable<User> signInFacebook(@Body TokenRequest request);

    @POST("/user/google/")
    Observable<User> signInGoogle(@Body TokenRequest request);

    @POST("/user/signup/")
    Observable<SignUpResponse> signUp(@Body SignUpRequest request);

    @GET("resources/countries/")
    Observable<CountryList> getCountryList();
}
