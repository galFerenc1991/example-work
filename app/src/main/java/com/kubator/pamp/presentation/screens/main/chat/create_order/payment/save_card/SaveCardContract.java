package com.kubator.pamp.presentation.screens.main.chat.create_order.payment.save_card;

import com.kubator.pamp.data.model.home.orders.Order;
import com.kubator.pamp.data.model.home.orders.OrderRequest;
import com.kubator.pamp.presentation.base.BaseModel;
import com.kubator.pamp.presentation.base.BasePresenter;
import com.kubator.pamp.presentation.base.BaseView;
import com.kubator.pamp.presentation.base.content.ContentView;

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
