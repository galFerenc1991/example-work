package com.kubator.pamp.presentation.screens.main.chat.messenger;

import com.kubator.pamp.data.model.base.ListResponse;
import com.kubator.pamp.data.model.home.good_deal.GoodDealCancelResponse;
import com.kubator.pamp.data.model.home.good_deal.GoodDealRequest;
import com.kubator.pamp.data.model.home.good_deal.GoodDealResponse;
import com.kubator.pamp.data.model.message.MessageResponse;
import com.kubator.pamp.presentation.base.BaseModel;
import com.kubator.pamp.presentation.base.BaseView;
import com.kubator.pamp.presentation.base.content.ContentView;
import com.kubator.pamp.presentation.base.refreshable.RefreshablePresenter;
import com.kubator.pamp.presentation.screens.main.chat.messenger.adapter.MessagesDH;

import java.io.File;
import java.util.Calendar;
import java.util.List;

import io.reactivex.Observable;

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

        void openCloseDatePicker(Calendar _calendar, long _startDeliveryDate);

        void openDeliveryDateScreen(long _currentStartDeliveryDate, long _currentEndDeliveryDate);

        void openCloseGoodDealPopUp();

        void openEndFlowScreen();

        void initCreateOrderButton(boolean _isHaveOrder);

        void openCreateOrderPopUp();

        void openDeleteOrderScreen();

        void openCreateOrderFlow(double _quantity);

        void openSendOrderListFlow();

        void selectImage();

        boolean isCameraPermissionNotGranted();

        void checkCameraPermission();

        void hideOrderBtn();

        void openDeliveryDateChangedNotif(String _title);

        List<MessagesDH> getMessagesDHs();

        void updateItem(int _position);

        void changeItem(MessagesDH _messagesDH, int _position);

        void changeRecyclerViewLayoutParams(boolean _isChange);

        void togglePaginationProgress(boolean visibility);

        void scrollToStart();
    }

    interface Presenter extends RefreshablePresenter {
        void openMessengerFragment();

        void initCreateOrderButton();

        void loadNextPage();

        void sendMessage(String messageText);

        void cancelDealAction();

        void cancelDeal();

        void changeCloseDateAction();

        void setChangedCloseDate(Calendar _calendar);

        void setChangedDeliveryDate(String _startDate, String _endDate);

        void changeDeliveryDateAction();

        void clickedCreateOrder();

        void resultQuantity(double _quantity);

        void sendOrders();

        void selectImage();

        void sendImage(File croppedFile);
    }

    interface Model {
        Observable<ListResponse<MessageResponse>> getMessages(String _chatID, int _page);
    }

    interface SocketModel {

        Observable<Void> connectSocket(String _dealId);

        Observable<MessageResponse> getNewMessage();

        Observable<Void> sendMessage(String _dealId, String _messageText);

        Observable<Void> disconnectSocket();

        void disconnect();

        Observable<Void> sendImage(String _dealId, String _messageText);

    }

    interface GoodDealModel extends BaseModel {
        Observable<GoodDealCancelResponse> cancelGoodDeal(String _id);

        Observable<GoodDealResponse> updateGoodDeal(String _id, GoodDealRequest request);
    }
}
