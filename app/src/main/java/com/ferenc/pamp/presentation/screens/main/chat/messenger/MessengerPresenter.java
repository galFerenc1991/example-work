package com.ferenc.pamp.presentation.screens.main.chat.messenger;

import android.content.Context;
import android.util.Log;

import com.ferenc.pamp.data.model.common.User;
import com.ferenc.pamp.data.model.home.good_deal.GoodDealResponse;
import com.ferenc.pamp.data.model.message.MessageResponse;
import com.ferenc.pamp.presentation.utils.Constants;
import com.ferenc.pamp.presentation.utils.SocketUtil;
import com.ferenc.pamp.presentation.screens.main.chat.messenger.adapter.MessagesDH;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by shonliu on 12/12/17.
 */

public class MessengerPresenter implements MessengerContract.Presenter {

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
    private Context mContext;

    public MessengerPresenter(MessengerContract.View mView, MessengerContract.Model _messageRepository, GoodDealResponse _goodDealResponse, User _myUser, SocketUtil _socketUtil, Context _context) {
        this.mView = mView;
        this.mModel = _messageRepository;
        this.mGoodDealResponse = _goodDealResponse;
        this.mCompositeDisposable = new CompositeDisposable();
        this.page = 1;
        this.mUser = _myUser;
        this.mSocketUtil = _socketUtil;
        this.mContext = _context;
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
        });

        mSocketUtil.onNewMessage
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onNewMessage -> {
                    mMessagesDH.add(0, new MessagesDH(onNewMessage, mGoodDealResponse, mUser, mContext, Constants.DEFAULT_MSG_GROUP_TYPE));
                    mView.addItem(mMessagesDH);
                });

        mView.showProgressMain();
        mCompositeDisposable.add(mModel.getMessages(mGoodDealResponse.id, page)
                .subscribe(model -> {
                    mView.hideProgress();
                    Log.d("MessengerPresenter", "getMessages Successfully");
                    mMessagesDH = new ArrayList<>();

                    for (MessageResponse messageResponse : model.data) {
                        mMessagesDH.add(new MessagesDH(messageResponse, mGoodDealResponse, mUser, mContext, typeDistributor(messageResponse.code)));
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

        mMessagesDH.add(0, new MessagesDH(messageResponse, mGoodDealResponse, mUser, mContext, Constants.DEFAULT_MSG_GROUP_TYPE));

        mSocketUtil.sendMessage(mUser.getToken(), mGoodDealResponse.id, messageResponse.text);

        mView.addItem(mMessagesDH);
    }

    @Override
    public void addImage() {
        mView.addImage();
    }

    private int typeDistributor(String code) {
        if (code != null) {
            switch (code) {
                case Constants.M1_GOOD_DEAL_DIFFUSION:
                    return Constants.M1_MSG_GROUP_TYPE;
                case Constants.M2_PRODUCT_ORDERING:
                case Constants.M3_ORDER_CHANGING:
                case Constants.M4_ORDER_CANCELLATION:
                    return Constants.M2_M3_M4_MSG_GROUP_TYPE;
                case Constants.M5_GOOD_DEAL_DELIVERY_DATE_CHANGED:
                case Constants.M6_GOOD_DEAL_CLOSING_DATE_CHANGED:
                case Constants.M9_CLOSING_DATE:
                case Constants.M12_DELIVERY_DATE:
                    return Constants.M5_M6_M9_M12_MSG_GROUP_TYPE;
                case Constants.M8_GOOD_DEAL_CANCELLATION:
                case Constants.M10_GOOD_DEAL_CLOSING:
                    return Constants.M8_M10_MSG_GROUP_TYPE;
                case Constants.M11_1_GOOD_DEAL_CONFIRMATION:
                case Constants.M11_2_GOOD_DEAL_CONFIRMATION_REJECTED:
                case Constants.M11_3_GOOD_DEAL_CONFIRMATION_APPLYED:
                    return Constants.M11_1_M11_2_M11_3_MSG_GROUP_TYPE;
                default:
                    throw new RuntimeException("MessagesDH :: typeDistributor [Can find needed group type]");
            }
        } else {
            return Constants.DEFAULT_MSG_GROUP_TYPE;
        }
    }
}
