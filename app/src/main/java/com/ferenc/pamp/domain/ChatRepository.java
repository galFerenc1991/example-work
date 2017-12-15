package com.ferenc.pamp.domain;

import com.ferenc.pamp.data.api.Rest;
import com.ferenc.pamp.data.api.RestConst;
import com.ferenc.pamp.data.model.base.ListResponse;
import com.ferenc.pamp.data.model.message.MessageResponse;
import com.ferenc.pamp.data.service.ChatService;
import com.ferenc.pamp.presentation.screens.main.chat.messenger.MessengerContract;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import io.reactivex.Observable;

/**
 * Created by shonliu on 12/14/17.
 */

@EBean(scope = EBean.Scope.Singleton)
public class ChatRepository extends NetworkRepository implements MessengerContract.Model {

    @Bean
    protected Rest rest;

    private ChatService chatService;

    @AfterInject
    protected void initService() {
        chatService = rest.getChatService();
    }


    @Override
    public Observable<ListResponse<MessageResponse>> getMessages(String _chatID, int _page) {
        return getNetworkObservable(chatService.getMessages(_chatID, _page, RestConst.ITEMS_PER_PAGE));
    }
}
