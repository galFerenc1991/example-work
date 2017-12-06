package com.ferenc.pamp.data.service;

import com.ferenc.pamp.data.model.home.good_deal.GoodDealRequest;
import com.ferenc.pamp.data.model.home.good_deal.GoodDealResponse;
import com.ferenc.pamp.presentation.base.models.GoodDeal;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by
 * Ferenc on 2017.12.01..
 */

public interface GoodDealService {

    @POST("/deal")
    Observable<GoodDealResponse> createGoodDeal(@Body GoodDealRequest request);
}
