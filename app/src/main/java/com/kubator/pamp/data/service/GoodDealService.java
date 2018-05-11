package com.kubator.pamp.data.service;

import com.kubator.pamp.data.model.base.ListResponse;
import com.kubator.pamp.data.model.home.good_deal.ConnectGoodDealResponse;
import com.kubator.pamp.data.model.home.good_deal.GoodDealCancelResponse;
import com.kubator.pamp.data.model.home.good_deal.GoodDealRequest;
import com.kubator.pamp.data.model.home.good_deal.GoodDealResponse;
import com.kubator.pamp.data.model.home.orders.OrdersList;

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

    @PUT("/deal/{id}")
    Observable<GoodDealResponse> updateGoodDeal(@Path("id") String _id, @Body GoodDealRequest request);

    @PUT("/deal/{id}/confirm")
    Observable<GoodDealCancelResponse> confirmDeal(@Path("id") String _dealId, @Body OrdersList list);

    @GET("/deal/{id}")
    Observable<GoodDealResponse> getDealById(@Path("id") String _dealId);
}
