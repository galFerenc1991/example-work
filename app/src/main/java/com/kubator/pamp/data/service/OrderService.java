package com.kubator.pamp.data.service;

import com.kubator.pamp.data.model.base.ListResponse;
import com.kubator.pamp.data.model.home.good_deal.GoodDealResponse;
import com.kubator.pamp.data.model.home.orders.ChangeOrderDeliveryStateRequest;
import com.kubator.pamp.data.model.home.orders.MessageOrderResponse;
import com.kubator.pamp.data.model.home.orders.Order;
import com.kubator.pamp.data.model.home.orders.OrderRequest;
import com.kubator.pamp.data.model.home.orders.PDFPreviewRequest;
import com.kubator.pamp.data.model.home.orders.PDFPreviewResponse;
import com.kubator.pamp.data.model.home.orders.Producer;
import com.kubator.pamp.data.model.home.orders.SendPDFRequest;

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

    @PUT("/producer/{id}")
    Observable<Producer> updateProducer(@Path("id") String _producerID, @Body Producer _producer);

    @GET("/producer")
    Observable<ListResponse<Producer>> getProducerList(@Query("page") int page, @Query("limit") int limit);

    @GET("/order")
    Observable<ListResponse<Order>> getOrders(@Query("dealId") String _dealId,
                                             @Query("page") int page,
                                             @Query("limit") int limit);
    @POST("/producer/{id}")
    Observable<PDFPreviewResponse> sendPurchase(@Path("id") String _producerID, @Body PDFPreviewRequest _requestBody);

    @PUT("/producer/sent")
    Observable<Object> sendPDFToProducer(@Body SendPDFRequest _requestBody);


    @GET("/order/{id}")
    Observable<PDFPreviewResponse> getOrderDetails(@Path("id") String _orderId);

    @Streaming
    @GET
    Observable<Response<ResponseBody>> downloadFile(@Url String _fileUrl);

    @GET("/order/my")
    Observable<ListResponse<Order>> getMyOrders(@Query("page") int page, @Query("limit") int limit);

    @PUT("/order/{id}/delivered")
    Observable<MessageOrderResponse> changeDeliveryState(@Path("id") String _orderId, @Body ChangeOrderDeliveryStateRequest _request);

    @GET("/deal/{id}")
    Observable<GoodDealResponse> getDealById(@Path("id") String _dealId);
}
