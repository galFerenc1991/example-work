package com.ferenc.pamp.presentation.screens.main.chat.messenger;

import com.ferenc.pamp.data.model.base.ListResponse;
import com.ferenc.pamp.data.model.home.good_deal.GoodDealCancelResponse;
import com.ferenc.pamp.data.model.home.good_deal.GoodDealCancelResponse;
import com.ferenc.pamp.data.model.home.good_deal.GoodDealRequest;
import com.ferenc.pamp.data.model.home.good_deal.GoodDealResponse;
import com.ferenc.pamp.data.model.home.orders.Order;
import com.ferenc.pamp.data.model.message.MessageResponse;
import com.ferenc.pamp.presentation.base.BaseModel;
import com.ferenc.pamp.presentation.base.BaseView;
import com.ferenc.pamp.presentation.base.content.ContentView;
import com.ferenc.pamp.presentation.base.refreshable.RefreshablePresenter;
import com.ferenc.pamp.presentation.screens.main.chat.messenger.adapter.MessagesDH;
import com.jakewharton.rxrelay2.Relay;

import java.util.Calendar;
import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Path;

/**
 * Created by shonliu on 12/12/17.
 */

public interface MessengerContract {

    interface View extends ContentView, BaseView<Presenter> {

        void openMessengerFragment();

        void setMessagesList(List<MessagesDH> _list);

        void addMessagesList(List<MessagesDH> _list);

        void addItem(List<MessagesDH> _list);

        void clearInputText();

        void addImage();

        void openCloseDatePicker(Calendar _calendar, long _startDeliveryDate);

        void openDeliveryDateScreen();

        void openCloseGoodDealPopUp();

        void openEndFlowScreen();

        void initCreateOrderButton(boolean _isHaveOrder);

        void openCreateOrderPopUp();

        void openDeleteOrderScreen();

        void openCreateOrderFlow(int _quantity);

        void openSendOrderListFlow();
    }

    interface Presenter extends RefreshablePresenter {
        void openMessengerFragment();

        void initCreateOrderButton();

        void loadNextPage();

        void sendMessage(String messageText);

        void addImage();

        void cancelDealAction();

        void cancelDeal();

        void changeCloseDateAction();

        void setChangedCloseDate(Calendar _calendar);

        void setChangedDeliveryDate(String _startDate, String _endDate);

        void changeDeliveryDateAction();

        void clickedCreateOrder();

        void resultQuantity(int _quantity);

        void sendOrders();
    }

    interface Model {
        Observable<ListResponse<MessageResponse>> getMessages(String _chatID, int _page);
    }

    interface SocketModel {

        Observable<Void> connectSocket(String _userToken, String _dealId);

        Observable<MessageResponse> getNewMessage();

        Observable<Void> sendMessage(String _userToken, String _dealId, String _messageText);

        Observable<Void> disconnectSocket();
    }

    interface GoodDealModel extends BaseModel {
        Observable<GoodDealCancelResponse> cancelGoodDeal(String _id);

        Observable<GoodDealResponse> updateGoodDeal(String _id, GoodDealRequest request);
    }
}
