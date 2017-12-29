package com.ferenc.pamp.presentation.screens.main.chat.create_order.payment.save_card;

import com.ferenc.pamp.data.model.home.orders.Order;
import com.ferenc.pamp.data.model.home.orders.OrderRequest;
import com.ferenc.pamp.presentation.base.BaseModel;
import com.ferenc.pamp.presentation.base.BasePresenter;
import com.ferenc.pamp.presentation.base.BaseView;
import com.ferenc.pamp.presentation.base.content.ContentView;

import io.reactivex.Observable;

/**
 * Created by
 * Ferenc on 2017.12.25..
 */

public interface SaveCardContract {
    interface View extends ContentView, BaseView<Presenter> {
        void openSuccessCreatedCardScreen();
    }

    interface Presenter extends BasePresenter {
        void setSaveCardInProfile(boolean isSave);

        void createOrder();
    }

    interface Model extends BaseModel {
        Observable<Order> createOrder(OrderRequest _orderRequest);
    }
}
