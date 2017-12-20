package com.ferenc.pamp.data.service;

import com.ferenc.pamp.data.model.home.orders.Order;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by
 * Ferenc on 2017.12.20..
 */

public interface OrderService {

    @GET("/deal/{id}/order")
    Observable<Order> getMyOrder(@Path("id") String _id);
}
