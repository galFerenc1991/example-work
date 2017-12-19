package com.ferenc.pamp.data.service;

import com.ferenc.pamp.data.model.base.ListResponse;
import com.ferenc.pamp.data.model.home.good_deal.ConnectGoodDealResponse;
import com.ferenc.pamp.data.model.home.good_deal.GoodDealCancelResponse;
import com.ferenc.pamp.data.model.home.good_deal.GoodDealRequest;
import com.ferenc.pamp.data.model.home.good_deal.GoodDealResponse;
import com.ferenc.pamp.presentation.base.models.GoodDeal;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by
 * Ferenc on 2017.12.01..
 */

public interface GoodDealService {

    @POST("/deal")
    Observable<GoodDealResponse> createGoodDeal(@Body GoodDealRequest request);

    @GET("/deal/{type}/list")
    Observable<ListResponse<GoodDealResponse>> getGoodDeals(@Path("type") String _type,
                                                            @Query("page") int page,
                                                            @Query("limit") int limit);

    @PUT("/deal/{id}/connect")
    Observable<ConnectGoodDealResponse> connectGoodDeal(@Path("id") String _id);

    @PUT("/deal/{id}/cancel")
    Observable<GoodDealCancelResponse> cancelGoodDeal(@Path("id") String _id);

    @POST("/deal/{id}/resend")
    Observable<GoodDealResponse> resendGoodDeal(@Path("id") String _id, @Body GoodDealRequest request);
}
