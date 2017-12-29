package com.ferenc.pamp.domain;

import com.ferenc.pamp.data.api.Rest;
import com.ferenc.pamp.data.model.home.orders.MessageOrderResponse;
import com.ferenc.pamp.data.model.home.orders.Order;
import com.ferenc.pamp.data.model.home.orders.OrderRequest;
import com.ferenc.pamp.data.service.OrderService;
import com.ferenc.pamp.presentation.screens.main.chat.create_order.create_order_pop_up.CreateOrderPopUpContract;
import com.ferenc.pamp.presentation.screens.main.chat.create_order.payment.add_card.AddCardContract;
import com.ferenc.pamp.presentation.screens.main.chat.messenger.MessengerContract;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import io.reactivex.Observable;

/**
 * Created by
 * Ferenc on 2017.12.20..
 */

@EBean(scope = EBean.Scope.Singleton)
public class OrderRepository extends NetworkRepository implements CreateOrderPopUpContract.OrderModel, AddCardContract.CreateOrderModel {

    @Bean
    protected Rest rest;

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
        return getNetworkObservable(orderService.createOrder(_orderRequest));
    }

    @Override
    public Observable<MessageOrderResponse> deleteOrder(String _orderId) {
        return getNetworkObservable(orderService.deleteOrder(_orderId));
    }

    @Override
    public Observable<MessageOrderResponse> updateOrder(String _orderId, OrderRequest _orderRequest) {
        return getNetworkObservable(orderService.updateOrder(_orderId, _orderRequest));
    }
}
