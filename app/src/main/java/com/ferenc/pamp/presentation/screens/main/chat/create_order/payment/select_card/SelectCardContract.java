package com.ferenc.pamp.presentation.screens.main.chat.create_order.payment.select_card;

import com.ferenc.pamp.data.model.common.User;
import com.ferenc.pamp.data.model.home.orders.Order;
import com.ferenc.pamp.data.model.home.orders.OrderRequest;
import com.ferenc.pamp.presentation.base.BaseModel;
import com.ferenc.pamp.presentation.base.BasePresenter;
import com.ferenc.pamp.presentation.base.BaseView;
import com.ferenc.pamp.presentation.base.content.ContentView;

import io.reactivex.Observable;

/**
 * Created by
 * Ferenc on 2017.12.22..
 */

public interface SelectCardContract {
    interface View extends ContentView, BaseView<Presenter> {
        void setCardNumber(String _cardNumber, String _brand);

        void showEndFlowCreateOrder();
    }

    interface Presenter extends BasePresenter {
        void createOrder();
    }

    interface Model extends BaseModel {
        Observable<User> getUserProfile();
    }

    interface CreateOrderModel extends BaseModel {
        Observable<Order> createOrder(OrderRequest _orderRequest);
    }

}
