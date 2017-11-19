package com.ferenc.pamp.data.api;

import android.text.TextUtils;
import android.util.Log;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.ferenc.pamp.PampApp;
import com.ferenc.pamp.data.api.exceptions.ConnectionLostException;
import com.ferenc.pamp.data.api.exceptions.TimeoutException;
import com.ferenc.pamp.data.service.AuthService;
import com.ferenc.pamp.presentation.utils.SharedPrefManager_;
import com.google.gson.Gson;

import org.androidannotations.annotations.App;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by
 * Ferenc on 2017.11.16..
 */

@EBean(scope = EBean.Scope.Singleton)
public class Rest {

    private Retrofit retrofit;

    @App
    protected PampApp mApplication;

    @Pref
    protected SharedPrefManager_ mSharedPrefManager;

    public Rest() {

        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
                .connectTimeout(RestConst.TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(RestConst.TIMEOUT_READ, TimeUnit.SECONDS)
                .writeTimeout(RestConst.TIMEOUT_WRITE, TimeUnit.SECONDS)
                .addNetworkInterceptor(new StethoInterceptor())
//                .authenticator((route, response) -> {
//                    getAuthService().refreshToken()
//                            .subscribe(refreshTokenResponse -> {
//                                Log.d("AuthToken", refreshTokenResponse.token);
//
//                                mSharedPrefManager.edit()
//                                        .getAccessToken()
//                                        .put(refreshTokenResponse.token)
//                                        .apply();
//                            }, throwable -> throwable.printStackTrace());
//                    return response.request().newBuilder()
//                            .header(RestConst.HEADER_AUTH, mSharedPrefManager.getAccessToken().get())
//                            .build();
//                })
                .addInterceptor(chain -> {
                    try {
                        if (!mApplication.hasInternetConnection()) {
                            throw new ConnectionLostException();
                        } else {
                            Request.Builder requestBuilder = chain.request().newBuilder()
                                    .header(RestConst.HEADER_CONTENT_TYPE, RestConst.HEADER_VALUE_HTML);
                            requestBuilder.header(RestConst.HEADER_AUTH, mSharedPrefManager.getAccessToken().get());
                            return chain.proceed(requestBuilder.build());
                        }
                    } catch (SocketTimeoutException e) {
                        throw new TimeoutException();
                    }
                })
                .addInterceptor(chain -> {
                    Response originalResponse = chain.proceed(chain.request());
                    String auth = originalResponse.header(RestConst.HEADER_AUTH);
                    if (!TextUtils.isEmpty(auth)) {
                        mSharedPrefManager.edit()
                                .getAccessToken()
                                .put(auth)
                                .apply();
                    }
                    return originalResponse;
                });

        retrofit = new Retrofit.Builder()
                .baseUrl(RestConst.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(clientBuilder.build())
                .build();
    }

    public AuthService getAuthService() {
        return retrofit.create(AuthService.class);
    }
}
