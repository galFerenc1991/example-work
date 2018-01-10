package com.ferenc.pamp.data.service;

import com.ferenc.pamp.data.model.base.ListResponse;
import com.ferenc.pamp.data.model.home.orders.MessageOrderResponse;
import com.ferenc.pamp.data.model.home.orders.Order;
import com.ferenc.pamp.data.model.home.orders.OrderRequest;
import com.ferenc.pamp.data.model.home.orders.PDFPreviewRequest;
import com.ferenc.pamp.data.model.home.orders.PDFPreviewResponse;
import com.ferenc.pamp.data.model.home.orders.Producer;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

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

    @GET("/producer")
    Observable<ListResponse<Producer>> getProducerList(@Query("page") int page, @Query("limit") int limit);

    @POST("/producer/{id}")
    Observable<PDFPreviewResponse> sendPurchase(@Path("id") String _producerID, @Body PDFPreviewRequest _requestBody);

    @GET("/order/{id}")
    Observable<PDFPreviewResponse> getOrderDetails(@Path("id") String _orderId);

    @Streaming
    @GET
    Observable<Response<ResponseBody>> downloadFile(@Url String _fileUrl);

    @GET("/order/my")
    Observable<ListResponse<Order>> getMyOrders(@Query("page") int page, @Query("limit") int limit);
}
