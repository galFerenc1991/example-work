package com.ferenc.pamp.presentation.screens.main.chat.messenger;

import android.util.Log;

import com.ferenc.pamp.data.model.common.User;
import com.ferenc.pamp.data.model.home.good_deal.GoodDealResponse;
import com.ferenc.pamp.data.model.message.MessageResponse;
import com.ferenc.pamp.presentation.utils.SocketUtil;
import com.ferenc.pamp.presentation.screens.main.chat.messenger.adapter.MessagesDH;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.socket.client.Socket;

/**
 * Created by shonliu on 12/12/17.
 */

public class MessengerPresenter implements MessengerContract.Presenter{

    private MessengerContract.View mView;
    private MessengerContract.Model mModel;
    private CompositeDisposable mCompositeDisposable;

    private int page;
    private int totalPages = Integer.MAX_VALUE;
    private boolean needRefresh;
    private GoodDealResponse mGoodDealResponse;
    private List<MessagesDH> mMessagesDH;
    private User mUser;
    private SocketUtil mSocketUtil;
    private Socket mSocket;

    public MessengerPresenter(MessengerContract.View mView, MessengerContract.Model _messageRepository, GoodDealResponse _goodDealResponse, User _myUser, SocketUtil _socketUtil) {
        this.mView = mView;
        this.mModel = _messageRepository;
        this.mGoodDealResponse = _goodDealResponse;
        this.mCompositeDisposable = new CompositeDisposable();
        this.page = 1;
        this.mUser = _myUser;
        this.mSocketUtil = _socketUtil;
        needRefresh = true;

        mView.setPresenter(this);
    }

    @Override
    public void subscribe() {

        mSocketUtil.initSocket();

        mSocketUtil.connectSocket();

        mSocketUtil.changeConnectionObservable.subscribe(isConnected -> {
            if (isConnected) {
                mSocketUtil.joinRoom(mUser.getToken(), mGoodDealResponse.id);
            }
            Log.d("changeConnection", String.valueOf(isConnected));
        });

        mSocketUtil.onNewMessage
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onNewMessage ->{
            mMessagesDH.add(0, new MessagesDH(onNewMessage, mGoodDealResponse, mUser));
            mView.addItem(mMessagesDH);
        });

        mView.showProgressMain();
        mCompositeDisposable.add(mModel.getMessages(mGoodDealResponse.id, page)
        .subscribe(model -> {
            mView.hideProgress();
            Log.d("MessengerPresenter", "getMessages Successfully");
            mMessagesDH = new ArrayList<>();

            for (MessageResponse messageResponse : model.data) {
                mMessagesDH.add(new MessagesDH(messageResponse, mGoodDealResponse, mUser));
            }

            Collections.reverse(mMessagesDH);

            mView.setMessagesList(mMessagesDH);

        }, throwable -> {
            mView.hideProgress();
            Log.d("MessengerPresenter", "Error " + throwable.getMessage());
        }));
    }

    @Override
    public void unsubscribe() {
        mCompositeDisposable.clear();
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void openMessengerFragment() {
        mView.openMessengerFragment();
    }

    @Override
    public void loadNextPage() {

    }

    @Override
    public void sendMessage(MessageResponse messageResponse) {

        mMessagesDH.add(0, new MessagesDH(messageResponse, mGoodDealResponse, mUser));

        mSocketUtil.sendMessage(mUser.getToken(),mGoodDealResponse.id, messageResponse.text);

        mView.addItem(mMessagesDH);
    }

    @Override
    public void addImage() {
        mView.addImage();
    }

}
