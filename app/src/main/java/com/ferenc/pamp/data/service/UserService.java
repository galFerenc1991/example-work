package com.ferenc.pamp.data.service;

import com.ferenc.pamp.data.model.auth.CountryList;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * Created by
 * Ferenc on 2017.12.01..
 */

public interface UserService {

    @GET("/user/personal/contacts")
    Observable<List<String>> getSavedUserContact();
}
