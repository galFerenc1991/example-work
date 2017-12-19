package com.ferenc.pamp.presentation.screens.main.chat.messenger;

import com.ferenc.pamp.data.model.base.ListResponse;
import com.ferenc.pamp.data.model.message.MessageResponse;
import com.ferenc.pamp.presentation.base.BaseView;
import com.ferenc.pamp.presentation.base.content.ContentView;
import com.ferenc.pamp.presentation.base.refreshable.RefreshablePresenter;
import com.ferenc.pamp.presentation.screens.main.chat.messenger.adapter.MessagesDH;
import com.jakewharton.rxrelay2.Relay;

import java.util.List;

import io.reactivex.Observable;

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

    }

    interface Presenter extends RefreshablePresenter {
        void openMessengerFragment();

        void loadNextPage();

        void sendMessage(String messageText);

        void addImage();

    }


    interface Model {
        Observable<ListResponse<MessageResponse>> getMessages(String _chatID, int _page);
    }

    interface SocketModel {

        Observable<MessageResponse> getNewMessage();

        Observable<Void> sendMessage(String _userToken, String _dealId, String _messageText);
    }
}
