package com.ferenc.pamp.data.service;

import com.ferenc.pamp.data.model.auth.TokenRequest;
import com.ferenc.pamp.data.model.common.Card;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by
 * Ferenc on 2017.12.28..
 */

public interface CardService {
    @POST("/customer/card")
    Observable<Card> createCard(@Body TokenRequest request);
}
