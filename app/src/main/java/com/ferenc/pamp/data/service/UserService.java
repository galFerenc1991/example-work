package com.ferenc.pamp.data.service;

import com.ferenc.pamp.data.model.auth.CountryList;
import com.ferenc.pamp.data.model.common.User;
import com.ferenc.pamp.data.model.common.UserUpdateRequest;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;

/**
 * Created by
 * Ferenc on 2017.12.01..
 */

public interface UserService {

    @GET("/user/personal/contacts")
    Observable<List<String>> getSavedUserContact();

    @GET("/user")
    Observable<User> getUser();

    @PUT("/user")
    Observable<User> updateUser(@Body UserUpdateRequest _user);
}
