package com.kubator.pamp.data.service;

import com.kubator.pamp.data.model.auth.TokenRequest;
import com.kubator.pamp.data.model.base.GeneralMessageResponse;
import com.kubator.pamp.data.model.common.ChangePasswordRequest;
import com.kubator.pamp.data.model.common.User;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
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

    @PUT("/user/changePassword")
    Observable<GeneralMessageResponse> changePassword(@Body ChangePasswordRequest request);

    @PUT("/user/notificationToken")
    Observable<GeneralMessageResponse> setNotifToken(@Body TokenRequest request);

    @GET("/static/cgu")
    Observable<ResponseBody> getCGU();

    @GET("/static/rules")
    Observable<ResponseBody> getRules();

    @GET("/static/about_us")
    Observable<ResponseBody> getAboutUs();

}
