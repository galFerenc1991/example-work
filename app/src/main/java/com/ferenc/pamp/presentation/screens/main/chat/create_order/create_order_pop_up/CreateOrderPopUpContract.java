package com.ferenc.pamp.presentation.screens.main.chat.create_order.create_order_pop_up;

import com.ferenc.pamp.data.model.home.orders.MessageOrderResponse;
import com.ferenc.pamp.data.model.home.orders.Order;
import com.ferenc.pamp.data.model.home.orders.OrderRequest;
import com.ferenc.pamp.presentation.base.BasePresenter;
import com.ferenc.pamp.presentation.base.BaseView;

import io.reactivex.Observable;

/**
 * Created by
 * Ferenc on 2017.12.21..
 */

public interface CreateOrderPopUpContract {

    interface View extends BaseView<Presenter> {
        void showProgress();

        void hideProgress(boolean _closeActivity);

        void showQuantity(String _quantity);

        void setQuantityColorToRed(boolean _isRed);

        void showProductName(String _productName);

        void showPriceDescription(String _priceDescription);

        void showPrice(String _price);

        void showTotal(String _total);

        void closeActivityForResult(double _quantity);

    }

    interface Presenter extends BasePresenter {
        void clickedAddQuantity();

        void clickedRemoveQuantity();

        void clickedOk();
    }

    interface OrderModel {
        Observable<Order> getMyOrder(String _id);

        Observable<Order> createOrder(OrderRequest _orderRequest);

        Observable<MessageOrderResponse> deleteOrder(String _orderId);

        Observable<MessageOrderResponse> updateOrder(String _orderId, OrderRequest _orderRequest);

    }
}
