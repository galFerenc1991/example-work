package com.ferenc.pamp.data.service;

import com.ferenc.pamp.data.model.base.ListResponse;
import com.ferenc.pamp.data.model.home.orders.MessageOrderResponse;
import com.ferenc.pamp.data.model.home.orders.Order;
import com.ferenc.pamp.data.model.home.orders.OrderRequest;
import com.ferenc.pamp.data.model.home.orders.Producer;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by
 * Ferenc on 2017.12.20..
 */

public interface OrderService {

    @GET("/deal/{id}/order")
    Observable<Order> getMyOrder(@Path("id") String _id);

    @POST("/order")
    Observable<Order> createOrder(@Body OrderRequest _orderRequest);

    @DELETE("/order/{id}")
    Observable<MessageOrderResponse> deleteOrder(@Path("id") String _id);

    @PUT("/order/{id}")
    Observable<MessageOrderResponse> updateOrder(@Path("id") String _id, @Body OrderRequest _orderRequest);

    @POST("/producer")
    Observable<Producer> createProducer(@Body Producer _producer);

//    @GET("/producer")
//    Observable<ListResponse<Producer>> getProducer(@Query("page") int page, @Query("limit") int limit);

    @POST("/producer/{id}")
    Observable<Object> sendPurchase(@Path("id") String _producerID, @Body OrderRequest _localOrder);

    @GET("/order")
    Observable<ListResponse<Order>> getOrders(@Query("dealId") String _dealId,
                                             @Query("page") int page,
                                             @Query("limit") int limit);
}
