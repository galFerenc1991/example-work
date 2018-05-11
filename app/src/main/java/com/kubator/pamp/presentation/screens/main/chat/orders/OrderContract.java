package com.kubator.pamp.presentation.screens.main.chat.orders;

import com.kubator.pamp.data.model.base.ListResponse;
import com.kubator.pamp.data.model.common.User;
import com.kubator.pamp.data.model.home.good_deal.GoodDealCancelResponse;
import com.kubator.pamp.data.model.home.orders.ChangeOrderDeliveryStateRequest;
import com.kubator.pamp.data.model.home.orders.MessageOrderResponse;
import com.kubator.pamp.data.model.home.orders.Order;
import com.kubator.pamp.data.model.home.orders.OrdersList;
import com.kubator.pamp.presentation.base.BaseModel;
import com.kubator.pamp.presentation.base.BaseView;
import com.kubator.pamp.presentation.base.content.ContentView;
import com.kubator.pamp.presentation.base.refreshable.RefreshablePresenter;
import com.kubator.pamp.presentation.screens.main.chat.orders.order_adapter.OrderDH;

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

        void showChangeDeliveryProgress();

        void hideChangeDeliveryProgress();

        void updateOrder(OrderDH _changedDeliveryStatusOrder, int position);

        void initSendPdfInfo(boolean isSentEmpty);

        void setResendTitle(String _resendTitle);
    }

    interface Presenter extends RefreshablePresenter {
        void loadNextPage();

        void clickedConfButton(List<OrderDH> _orders);

        void openCreateBankAccountFlow();

        void doConfirm();

        void changeDeliveryState(OrderDH _orderDH, int _orderPosition);

        void initSendPdfInfo();
    }

    interface Model extends BaseModel {
        Observable<ListResponse<Order>> getOrders(String _dealId, int _page);

        Observable<MessageOrderResponse> changeDeliveryState(String _orderId, ChangeOrderDeliveryStateRequest _request);

    }

    interface GoodDealModel extends BaseModel {
        Observable<GoodDealCancelResponse> confirmDeal(String _dealId, OrdersList list);
    }

    interface UserModel extends BaseModel {
        Observable<User> getUserProfile();
    }

}
