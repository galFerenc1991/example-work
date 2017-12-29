package com.ferenc.pamp.data.service;

import com.ferenc.pamp.data.model.common.User;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

/**
 * Created by
 * Ferenc on 2017.12.01..
 */

public interface UserService {

    @GET("/user/personal/contacts")
    Observable<List<String>> getSavedUserContact();

    @GET("/user/personal/addresses")
    Observable<List<String>> getSavedUserAddresses();

    @GET("/user")
    Observable<User> getUser();

    @Multipart
    @PUT("/user")
    Observable<User> updateUser(@PartMap() Map<String, RequestBody> user);

    @Multipart
    @PUT("/user")
    Observable<User> updateUser(@PartMap() Map<String, RequestBody> user, @Part MultipartBody.Part file);
}
