package com.kubator.pamp.presentation.screens.main.profile.my_orders;

import com.kubator.pamp.data.model.base.ListResponse;
import com.kubator.pamp.data.model.home.orders.Order;
import com.kubator.pamp.presentation.base.BasePresenter;
import com.kubator.pamp.presentation.base.BaseView;
import com.kubator.pamp.presentation.screens.main.profile.my_orders.adapter.MyOrderDH;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by shonliu on 1/10/18.
 */

public interface MyOrdersContract {

    interface View extends BaseView<Presenter> {

        void showProgressBar();

        void hideProgressBar();

        void showPaginationProgressBar();

        void hidePaginationProgressBar();

        void setOrdersList(List<MyOrderDH> _orders);

        void addOrdersList(List<MyOrderDH> _orders);

        void openOrderPreview(String _orderId);

        void setPlaceholderVisibility(boolean isVisible);
    }

    interface Presenter extends BasePresenter {
        void loadNextPage();

        void selectItem(MyOrderDH item, int position);
    }

    interface Model {
        Observable<ListResponse<Order>> getMyOrders(int _page);
    }
}
