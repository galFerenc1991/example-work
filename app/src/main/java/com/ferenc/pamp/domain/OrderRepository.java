package com.ferenc.pamp.domain;

import com.ferenc.pamp.data.api.Rest;
import com.ferenc.pamp.data.model.base.ListResponse;
import com.ferenc.pamp.data.model.home.good_deal.GoodDealResponse;
import com.ferenc.pamp.data.model.home.orders.MessageOrderResponse;
import com.ferenc.pamp.data.model.home.orders.Order;
import com.ferenc.pamp.data.model.home.orders.OrderRequest;
import com.ferenc.pamp.data.model.home.orders.PDFPreviewRequest;
import com.ferenc.pamp.data.model.home.orders.PDFPreviewResponse;
import com.ferenc.pamp.data.model.home.orders.Producer;
import com.ferenc.pamp.data.service.OrderService;
import com.ferenc.pamp.presentation.screens.main.chat.create_order.create_order_pop_up.CreateOrderPopUpContract;
import com.ferenc.pamp.presentation.screens.main.chat.create_order.payment.add_card.AddCardContract;
import com.ferenc.pamp.presentation.screens.main.chat.create_order.payment.save_card.SaveCardContract;
import com.ferenc.pamp.presentation.screens.main.chat.create_order.payment.select_card.SelectCardContract;
import com.ferenc.pamp.presentation.screens.main.chat.messenger.MessengerContract;
import com.ferenc.pamp.presentation.screens.main.chat.orders.producer.choose_producer.ChooseProducerContract;
import com.ferenc.pamp.presentation.screens.main.chat.orders.producer.choose_producer.create_new_producer.CreateNewProducerContract;
import com.ferenc.pamp.presentation.screens.main.chat.orders.producer.preview_pdf.PreviewPDFContract;
import com.ferenc.pamp.presentation.utils.GoodDealResponseManager;

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
        CreateNewProducerContract.Model,
        PreviewPDFContract.Model {

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
        return getNetworkObservable(orderService.getProducerList(_page, 10));
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
    public Observable<Producer> createProducer(Producer _producer) {
        return getNetworkObservable(orderService.createProducer(_producer));
    }
}
