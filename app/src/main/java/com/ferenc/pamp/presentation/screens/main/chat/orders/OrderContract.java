package com.ferenc.pamp.presentation.screens.main.chat.orders;

import com.ferenc.pamp.data.model.base.ListResponse;
import com.ferenc.pamp.data.model.common.User;
import com.ferenc.pamp.data.model.home.good_deal.GoodDealCancelResponse;
import com.ferenc.pamp.data.model.home.orders.Order;
import com.ferenc.pamp.data.model.home.orders.OrdersList;
import com.ferenc.pamp.presentation.base.BaseModel;
import com.ferenc.pamp.presentation.base.BasePresenter;
import com.ferenc.pamp.presentation.base.BaseView;
import com.ferenc.pamp.presentation.base.content.ContentView;
import com.ferenc.pamp.presentation.base.refreshable.RefreshablePresenter;
import com.ferenc.pamp.presentation.screens.main.chat.orders.order_adapter.OrderDH;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by
 * Ferenc on 2017.12.15..
 */

public interface OrderContract {

    interface View extends ContentView, BaseView<Presenter> {
        void setOrdersList(List<OrderDH> _orders);

        void addOrder(List<OrderDH> _order);

        void showPlaceHolderText();

        void hidePlaceholderText();

        void setDealInfo(String _productName, String _util, String _closeDate);

        void showConfButton();

        void hideConfButton();

        void showBankAccountErrorPopUp();

        void showConfirmPopUp();

        void openCreateBankAccountFlow();
    }

    interface Presenter extends RefreshablePresenter {
        void loadNextPage();

        void clickedConfButton(List<OrderDH> _orders);

        void openCreateBankAccountFlow();

        void doConfirm();
    }

    interface Model extends BaseModel {
        Observable<ListResponse<Order>> getOrders(String _dealId, int _page);
    }

    interface GoodDealModel extends BaseModel {
        Observable<GoodDealCancelResponse> confirmDeal(String _dealId, OrdersList list);
    }

    interface UserModel extends BaseModel {
        Observable<User> getUserProfile();
    }

}
