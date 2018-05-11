package com.kubator.pamp.domain;

import com.kubator.pamp.data.api.Rest;
import com.kubator.pamp.data.api.RestConst;
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
import com.kubator.pamp.data.service.OrderService;
import com.kubator.pamp.presentation.screens.main.chat.create_order.create_order_pop_up.CreateOrderPopUpContract;
import com.kubator.pamp.presentation.screens.main.chat.create_order.payment.save_card.SaveCardContract;
import com.kubator.pamp.presentation.screens.main.chat.create_order.payment.select_card.SelectCardContract;
import com.kubator.pamp.presentation.screens.main.chat.orders.OrderContract;
import com.kubator.pamp.presentation.screens.main.chat.orders.producer.choose_producer.ChooseProducerContract;
import com.kubator.pamp.presentation.screens.main.chat.orders.producer.choose_producer.create_update_producer.CreateUpdateProducerContract;
import com.kubator.pamp.presentation.screens.main.chat.orders.producer.preview_pdf.PreviewPDFContract;
import com.kubator.pamp.presentation.screens.main.profile.my_orders.MyOrdersContract;
import com.kubator.pamp.presentation.utils.GoodDealResponseManager;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * Created by
 * Ferenc on 2017.12.20..
 */

@EBean(scope = EBean.Scope.Singleton)
public class OrderRepository extends NetworkRepository implements
        CreateOrderPopUpContract.OrderModel,
        SaveCardContract.Model,
        SelectCardContract.CreateOrderModel,
        ChooseProducerContract.Model,
        CreateUpdateProducerContract.Model,
        PreviewPDFContract.Model,
        OrderContract.Model,
        MyOrdersContract.Model {

    @Bean
    protected Rest rest;

    @Bean
    protected GoodDealResponseManager mGoodDealResponseManager;

    private OrderService orderService;

    @AfterInject
    protected void initService() {
        orderService = rest.getOrderService();
    }

    @Override
    public Observable<Order> getMyOrder(String _id) {
        return getNetworkObservable(orderService.getMyOrder(_id));
    }

    @Override
    public Observable<Order> createOrder(OrderRequest _orderRequest) {
        return getNetworkObservable(orderService.createOrder(_orderRequest)
                .flatMap(order -> {
                    GoodDealResponse goodDealResponse = mGoodDealResponseManager.getGoodDealResponse();
                    goodDealResponse.hasOrders = true;
                    mGoodDealResponseManager.saveGoodDealResponse(goodDealResponse);
                    return Observable.just(order);
                }));
    }

    @Override
    public Observable<MessageOrderResponse> deleteOrder(String _orderId) {
        return getNetworkObservable(orderService.deleteOrder(_orderId));
    }

    @Override
    public Observable<MessageOrderResponse> updateOrder(String _orderId, OrderRequest _orderRequest) {
        return getNetworkObservable(orderService.updateOrder(_orderId, _orderRequest));
    }

    @Override
    public Observable<ListResponse<Producer>> getProducerList(int _page) {
        return getNetworkObservable(orderService.getProducerList(_page, 20));
    }

    @Override
    public Observable<Response<ResponseBody>> getFileByUrl(String _url) {
        return orderService.downloadFile(_url);
    }

    @Override
    public Observable<PDFPreviewResponse> getPDFPreview(String _producerId, PDFPreviewRequest _requestBody) {
        return getNetworkObservable(orderService.sendPurchase(_producerId, _requestBody));
    }

    @Override
    public Observable<Object> sendPDFToProducer(SendPDFRequest _requestBody) {
        return getNetworkObservable(orderService.sendPDFToProducer(_requestBody));
    }

    @Override
    public Observable<PDFPreviewResponse> getPDFPreview(String _orderId) {
        return getNetworkObservable(orderService.getOrderDetails(_orderId));
    }


    @Override
    public Observable<Producer> createProducer(Producer _producer) {
        return getNetworkObservable(orderService.createProducer(_producer));
    }

    @Override
    public Observable<ListResponse<Order>> getOrders(String _dealId, int _page) {
        return getNetworkObservable(orderService.getOrders(_dealId, _page, RestConst.ITEMS_PER_PAGE));
    }

    @Override
    public Observable<Producer> updateProducer(Producer _producer) {
        return getNetworkObservable(orderService.updateProducer(_producer.producerId, _producer));
    }

    @Override
    public Observable<ListResponse<Order>> getMyOrders(int _page) {
        return getNetworkObservable(orderService.getMyOrders(_page, 20));
    }

    @Override
    public Observable<MessageOrderResponse> changeDeliveryState(String _orderId, ChangeOrderDeliveryStateRequest _request) {
        return getNetworkObservable(orderService.changeDeliveryState(_orderId, _request));
    }

    @Override
    public Observable<GoodDealResponse> getDialId(String _dealId) {
        return orderService.getDealById(_dealId)
                .flatMap(goodDealResponse -> {
                    mGoodDealResponseManager.saveGoodDealResponse(goodDealResponse);
                    return Observable.just(goodDealResponse);
                });
    }
}
