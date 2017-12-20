package com.ferenc.pamp.presentation.screens.main.chat.messenger;

import com.ferenc.pamp.data.model.base.ListResponse;
import com.ferenc.pamp.data.model.home.good_deal.GoodDealCancelResponse;
import com.ferenc.pamp.data.model.home.good_deal.GoodDealCancelResponse;
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

        void addItem(List<MessagesDH> _list);

        void sendMessage();

        void addImage();

        void openCloseDatePicker(Calendar _calendar, long _startDeliveryDate);

        void openDeliveryDateScreen();

        void openCloseGoodDealPopUp();

        void openEndFlowScreen();

        void openCreateOrderPopUp();
    }

    interface Presenter extends RefreshablePresenter {
        void openMessengerFragment();

        void loadNextPage();

        void sendMessage(String messageText);

        void addImage();

        void cancelDealAction();

        void cancelDeal();

        void changeCloseDateAction();

        void setChangedCloseDate(Calendar _calendar);

        void changeDeliveryDateAction();

        void clickedCreateOrder();
    }

    interface Model {
        Observable<ListResponse<MessageResponse>> getMessages(String _chatID, int _page);
    }

    interface SocketModel {

        Observable<MessageResponse> getNewMessage();

        Observable<Void> sendMessage(String _userToken, String _dealId, String _messageText);
    }
    interface GoodDealModel extends BaseModel {
        Observable<GoodDealCancelResponse> cancelGoodDeal(String _id);
    }

    interface OrderModel {
        Observable<Order> getMyOrder(String _id);
    }
}
