package com.kubator.pamp.domain;

import com.kubator.pamp.data.api.Rest;
import com.kubator.pamp.data.model.base.ListResponse;
import com.kubator.pamp.data.model.message.MessageResponse;
import com.kubator.pamp.data.service.ChatService;
import com.kubator.pamp.presentation.screens.main.chat.messenger.MessengerContract;

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
        return getNetworkObservable(chatService.getMessages(_chatID, _page, 10));
    }
}
