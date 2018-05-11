package com.kubator.pamp.presentation.utils;

import android.text.TextUtils;

import com.kubator.pamp.data.model.common.User;
import com.google.gson.Gson;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.sharedpreferences.Pref;

/**
 * Created by
 * Ferenc on 2017.11.09..
 */

@EBean(scope = EBean.Scope.Singleton)
public class SignedUserManager {

    private Gson gson = new Gson();

    @Pref
    protected SharedPrefManager_ prefManager;

    public void saveUser(User user) {
        if (user == null) {
            clearUser();
        } else {
            prefManager
                    .edit()
                    .getUserProfile()
                    .put(gson.toJson(user))
                    .apply();
        }
    }

    public User getCurrentUser() {
        String userStr = prefManager.getUserProfile().get();
        return TextUtils.isEmpty(userStr)
                ? new User()
                : gson.fromJson(userStr, User.class);
    }


    public void clearUser() {
        prefManager
                .edit()
                .getUserProfile()
                .remove()
                .apply();
    }

}
