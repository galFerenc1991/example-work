package com.ferenc.pamp.domain;

import android.util.Log;

import com.ferenc.pamp.data.api.Rest;
import com.ferenc.pamp.data.model.auth.TokenRequest;
import com.ferenc.pamp.data.model.base.GeneralMessageResponse;
import com.ferenc.pamp.data.model.common.ChangePasswordRequest;
import com.ferenc.pamp.data.model.common.User;


import com.ferenc.pamp.data.service.UserService;
import com.ferenc.pamp.presentation.screens.main.chat.create_order.payment.select_card.SelectCardContract;
import com.ferenc.pamp.presentation.screens.main.chat.orders.OrderContract;
import com.ferenc.pamp.presentation.screens.main.profile.ProfileContract;

import com.ferenc.pamp.presentation.screens.main.profile.edit_profile.EditProfileContract;
import com.ferenc.pamp.presentation.screens.main.profile.edit_profile.change_password.ChangePasswordContract;
import com.ferenc.pamp.presentation.utils.SharedPrefManager_;
import com.ferenc.pamp.presentation.utils.SignedUserManager;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;

/**
 * Created by
 * Ferenc on 2017.12.01..
 */
@EBean(scope = EBean.Scope.Singleton)
public class UserRepository extends NetworkRepository implements ProfileContract.UserProfileModel,
        EditProfileContract.Model,
        SelectCardContract.Model,
        OrderContract.UserModel,
        ChangePasswordContract.Model {

    @Bean
    protected Rest rest;
    @Bean
    protected SignedUserManager mSignedUserManager;

    @Pref
    protected SharedPrefManager_ mSharedPrefManager;

    private UserService userService;

    @AfterInject
    protected void initServices() {
        userService = rest.getUserService();
    }

    @Override
    public Observable<User> getUserProfile() {
        return getNetworkObservable(userService.getUser()
                .flatMap(user -> {
                    mSignedUserManager.saveUser(user);
                    return Observable.just(user);
                }));
    }

    @Override
    public Observable<User> updateUser(Map<String, RequestBody> userBody, MultipartBody.Part avatar) {
        return getNetworkObservable(userService.updateUser(userBody, avatar).flatMap(user -> {
            return getUserProfile();
        }));
    }

    @Override
    public Observable<GeneralMessageResponse> changePassword(ChangePasswordRequest request) {
        return getNetworkObservable(userService.changePassword(request));
    }

    public Observable<GeneralMessageResponse> setNotifToken(TokenRequest request) {
        Log.d("FT:", "" + request.getToken());
        return getNetworkObservable(userService.setNotifToken(request));
    }
}
