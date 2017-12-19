package com.ferenc.pamp.presentation.screens.main.chat.messenger;

import android.util.Log;

import com.ferenc.pamp.R;
import com.ferenc.pamp.data.api.exceptions.ConnectionLostException;
import com.ferenc.pamp.data.model.common.User;
import com.ferenc.pamp.data.model.home.good_deal.GoodDealResponse;
import com.ferenc.pamp.data.model.message.MessageResponse;
import com.ferenc.pamp.presentation.screens.main.chat.messenger.adapter.MessagesDH;
import com.ferenc.pamp.presentation.utils.Constants;
import com.ferenc.pamp.presentation.utils.ToastManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

/**
 * Created by shonliu on 12/12/17.
 */

public class MessengerPresenter implements MessengerContract.Presenter {

    private MessengerContract.View mView;
    private MessengerContract.Model mModel;
    private MessengerContract.GoodDealModel mGoodDealModel;
    private CompositeDisposable mCompositeDisposable;

    private int page;
    private int totalPages = Integer.MAX_VALUE;
    private boolean needRefresh;
    private GoodDealResponse mGoodDealResponse;
    private List<MessagesDH> mMessagesDH;
    private User mUser;

    public MessengerPresenter(MessengerContract.View mView
            , MessengerContract.Model _messageRepository
            , MessengerContract.GoodDealModel _goodDealModel
            , GoodDealResponse _goodDealResponse
            , User _myUser) {

        this.mView = mView;
        this.mModel = _messageRepository;
        this.mGoodDealModel = _goodDealModel;
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

                        if (messageResponse.code != null) {
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
    public void onRefresh() {
        mView.hideProgress();
    }

    @Override
    public void openMessengerFragment() {
        mView.openMessengerFragment();
    }

    @Override
    public void loadNextPage() {

    }

    @Override
    public void cancelDealAction() {
        mView.openCloseGoodDealPopUp();

    }

    @Override
    public void cancelDeal() {
        mView.showProgressMain();
        mCompositeDisposable.add(mGoodDealModel.cancelGoodDeal(mGoodDealResponse.id)
                .subscribe(goodDealCancelResponse -> {
                    mView.showProgressMain();
                    mView.openEndFlowScreen();
                }, throwableConsumer));
    }

    private Consumer<Throwable> throwableConsumer = throwable -> {
        throwable.printStackTrace();
        mView.hideProgress();
        if (throwable instanceof ConnectionLostException) {
            ToastManager.showToast(R.string.err_msg_connection_problem);
//            mView.showErrorMessage(Constants.MessageType.CONNECTION_PROBLEMS);
        } else {
            ToastManager.showToast(R.string.err_msg_something_goes_wrong);
//            mView.showErrorMessage(Constants.MessageType.UNKNOWN);
        }
    };

    @Override
    public void changeCloseDateAction() {
        Calendar date = Calendar.getInstance();
        date.setTimeInMillis(mGoodDealResponse.closingDate);
        mView.openCloseDatePicker(date, mGoodDealResponse.deliveryStartDate);
    }

    @Override
    public void setChangedCloseDate(Calendar _changedCloseDate) {
        /// do request
    }

    @Override
    public void changeDeliveryDateAction() {
        mView.openDeliveryDateScreen();
    }

    @Override
    public void sendMessage() {
        mView.sendMessage();
    }

    @Override
    public void addImage() {
        mView.addImage();
    }

    @Override
    public void unsubscribe() {
        mCompositeDisposable.clear();
    }
}
