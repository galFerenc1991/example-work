package com.ferenc.pamp.presentation.service;

import android.util.Log;

import com.ferenc.pamp.data.model.auth.TokenRequest;
import com.ferenc.pamp.domain.UserRepository;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EService;

/**
 * Created by
 * Ferenc on 2018.01.17..
 */
@EService
public class FirebaseInstanceIDService extends FirebaseInstanceIdService {

    @Bean
    UserRepository mUserRepository;

    @Override
    public void onTokenRefresh() {
        mUserRepository.setNotifToken(new TokenRequest(FirebaseInstanceId.getInstance().getToken()))
        .subscribe(generalMessageResponse -> {
            Log.d("push", "succes");
        }, throwable -> {
            Log.d("push", "error:" + throwable.getMessage());

        });
    }
}
