package com.ferenc.pamp.presentation.screens.main.chat.messenger;

import android.util.Log;

import com.ferenc.pamp.data.model.common.User;
import com.ferenc.pamp.data.model.home.good_deal.GoodDealResponse;
import com.ferenc.pamp.data.model.message.MessageResponse;
import com.ferenc.pamp.presentation.screens.main.chat.messenger.adapter.MessagesDH;
import com.ferenc.pamp.presentation.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

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

    public MessengerPresenter(MessengerContract.View mView, MessengerContract.Model _messageRepository, GoodDealResponse _goodDealResponse, User _myUser) {
        this.mView = mView;
        this.mModel = _messageRepository;
        this.mGoodDealResponse = _goodDealResponse;
        this.mCompositeDisposable = new CompositeDisposable();
        this.page = 1;
        this.mUser = _myUser;
        needRefresh = true;

        mView.setPresenter(this);
    }

    @Override
    public void subscribe() {
        mView.showProgressMain();
        mCompositeDisposable.add(mModel.getMessages(mGoodDealResponse.id, page)
        .subscribe(model -> {
            mView.hideProgress();
            Log.d("MessengerPresenter", "getMessages Successfully");
            mMessagesDH = new ArrayList<>();

            for (MessageResponse messageResponse : model.data) {

                if (messageResponse.code !=null) {
                    mMessagesDH.add(new MessagesDH(messageResponse, mGoodDealResponse, mUser));
                } else {
                    mMessagesDH.add(new MessagesDH(messageResponse, mGoodDealResponse, mUser));
                }
            }

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
    public void sendMessage() {
        mView.sendMessage();
    }

    @Override
    public void addImage() {
        mView.addImage();
    }

}
