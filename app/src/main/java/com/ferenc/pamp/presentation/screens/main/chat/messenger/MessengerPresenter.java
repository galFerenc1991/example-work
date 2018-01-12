package com.ferenc.pamp.presentation.screens.main.chat.messenger;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.ferenc.pamp.R;
import com.ferenc.pamp.data.api.exceptions.ConnectionLostException;
import com.ferenc.pamp.data.model.home.good_deal.GoodDealRequest;
import com.ferenc.pamp.data.model.home.good_deal.GoodDealResponse;
import com.ferenc.pamp.data.model.message.MessageResponse;
import com.ferenc.pamp.domain.SocketRepository;
import com.ferenc.pamp.presentation.utils.Constants;
import com.ferenc.pamp.presentation.utils.GoodDealManager;
import com.ferenc.pamp.presentation.utils.GoodDealResponseManager;
import com.ferenc.pamp.presentation.utils.ToastManager;
import com.ferenc.pamp.presentation.utils.SignedUserManager;
import com.ferenc.pamp.presentation.screens.main.chat.messenger.adapter.MessagesDH;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;


/**
 * Created by shonliu on 12/12/17.
 */

public class MessengerPresenter implements MessengerContract.Presenter {

    private MessengerContract.View mView;
    private MessengerContract.Model mModel;
    private MessengerContract.SocketModel mSocketModel;
    private MessengerContract.GoodDealModel mGoodDealModel;
    private CompositeDisposable mCompositeDisposable;
    private GoodDealResponseManager mGoodDealResponseManager;
    private GoodDealManager mGoodDealManager;

    private int mPage;
    private int mTotalPages = Integer.MAX_VALUE;
    private GoodDealResponse mGoodDealResponse;
    private List<MessagesDH> mMessagesDH;
    private SignedUserManager mSignedUserManager;
    private Context mContext;

    public MessengerPresenter(MessengerContract.View mView
            , MessengerContract.Model _messageRepository
            , MessengerContract.GoodDealModel _goodDealModel
            , SocketRepository _socketRepository
            , SignedUserManager _signedUserManager
            , Context _context
            , GoodDealResponseManager _goodDealResponseManager
            , GoodDealManager _goodDealManager) {

        this.mView = mView;
        this.mModel = _messageRepository;
        this.mGoodDealModel = _goodDealModel;
        this.mCompositeDisposable = new CompositeDisposable();
        this.mPage = 1;
        this.mSignedUserManager = _signedUserManager;
        this.mContext = _context;
        this.mSocketModel = _socketRepository;
        this.mGoodDealResponse = _goodDealResponseManager.getGoodDealResponse();
        this.mGoodDealResponseManager = _goodDealResponseManager;
        this.mGoodDealManager = _goodDealManager;

        mView.setPresenter(this);
    }

    @Override
    public void subscribe() {
        initCreateOrderButton();
        connectSocket();
        getMessage();

        loadData(mPage, false);
    }

    @Override
    public void initCreateOrderButton() {
        GoodDealResponse goodDealResponse = mGoodDealResponseManager.getGoodDealResponse();
        if (!goodDealResponse.owner.getId().equals(mSignedUserManager.getCurrentUser().getId())
                && goodDealResponse.state.equals(Constants.STATE_PROGRESS)) {
            if (goodDealResponse.hasOrders) {
                mView.initCreateOrderButton(true);
            } else mView.initCreateOrderButton(false);
        }
    }

    private void connectSocket() {
        mCompositeDisposable.add(
                mSocketModel
                        .connectSocket(mGoodDealResponse.id)
                        .subscribe()
        );
    }

    private void getMessage() {
        mCompositeDisposable.add(mSocketModel.getNewMessage()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(messageResponse -> {
                    mMessagesDH.add(0, new MessagesDH(messageResponse, mGoodDealResponse, mSignedUserManager.getCurrentUser(), mContext, typeDistributor(messageResponse.code)));
                    mView.addItem(mMessagesDH);
                    if (messageResponse.code.equals(Constants.M6_GOOD_DEAL_CLOSING_DATE_CHANGED)) {
                        GoodDealResponse goodDealResponse = mGoodDealResponseManager.getGoodDealResponse();
                        goodDealResponse.closingDate = messageResponse.description.closingDate;
                        mGoodDealResponseManager.saveGoodDealResponse(goodDealResponse);
                    }
                }, throwable -> {
                    Log.d("MessengerPresenter", "Error while getting new message " + throwable.getMessage());
                }));
    }

    private void loadData(int _page, boolean isLoadMoreList) {
        if (!isLoadMoreList) {
            mView.showProgressMain();
        } else {
            mView.showProgressPagination();
        }
        mCompositeDisposable.add(mModel.getMessages(mGoodDealResponse.id, _page)
                .subscribe(model -> {
                    mView.hideProgress();
                    Log.d("MessengerPresenter", "getMessages Successfully");
                    mMessagesDH = new ArrayList<>();

                    for (MessageResponse messageResponse : model.data) {
                        mMessagesDH.add(new MessagesDH(messageResponse, mGoodDealResponse, mSignedUserManager.getCurrentUser(), mContext, typeDistributor(messageResponse.code)));
                    }

                    Collections.reverse(mMessagesDH);

                    if (!isLoadMoreList)
                        mView.setMessagesList(mMessagesDH);
                    else
                        mView.addMessagesList(mMessagesDH);

                    mTotalPages = model.meta.pages;
                    mPage++;

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
        if (mPage - 1 != mTotalPages) {
            loadData(mPage, true);
        }
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
                    mView.hideProgress();
                    mView.openEndFlowScreen();
                }, throwableConsumer));
    }

    private Consumer<Throwable> throwableConsumer = throwable -> {
        throwable.printStackTrace();
        mView.hideProgress();
        if (throwable instanceof ConnectionLostException) {
            ToastManager.showToast(R.string.err_msg_connection_problem);
//            mView.showErrorMessage(Constants.MessageType.CONNECTION_PROBLEMS);
        } else if (throwable instanceof HttpException) {
            HttpException e = (HttpException) throwable;
            switch (e.code()) {
                case 204:
//                    mView.hideCreateOrderProgress(false);
            }
        } else {
            ToastManager.showToast(R.string.err_msg_something_goes_wrong);
//            mView.showErrorMessage(Constants.MessageType.UNKNOWN);
        }
    };

    @Override
    public void changeCloseDateAction() {
        Calendar date = Calendar.getInstance();
        date.setTimeInMillis(mGoodDealResponseManager.getGoodDealResponse().closingDate);
        mView.openCloseDatePicker(date, mGoodDealResponseManager.getGoodDealResponse().deliveryStartDate);
    }

    @Override
    public void setChangedCloseDate(Calendar _changedCloseDate) {
        mView.showProgressMain();
        mCompositeDisposable.add(mGoodDealModel.updateGoodDeal(mGoodDealResponse.id, new GoodDealRequest.Builder()
                .setClosingDate(_changedCloseDate.getTimeInMillis())
                .build())
                .subscribe(goodDealResponse -> {
                    mGoodDealResponseManager.saveGoodDealResponse(goodDealResponse);
                    mView.hideProgress();
                }, throwable -> {
                    mView.hideProgress();
                    mView.showErrorMessage(Constants.MessageType.UNKNOWN);
                }));

    }

    @Override
    public void setChangedDeliveryDate(String _startDate, String _endDate) {
        mView.showProgressMain();
        mCompositeDisposable.add(mGoodDealModel.updateGoodDeal(mGoodDealResponse.id, new GoodDealRequest.Builder()
                .setDeliveryStartDate(mGoodDealManager.getGoodDeal().getDeliveryStartDate())
                .setDeliveryEndDate(mGoodDealManager.getGoodDeal().getDeliveryEndDate())
                .build())
                .subscribe(goodDealResponse -> {
                    mGoodDealResponseManager.saveGoodDealResponse(goodDealResponse);
                    mView.hideProgress();
                }, throwable -> {
                    mView.hideProgress();
                    mView.showErrorMessage(Constants.MessageType.UNKNOWN);
                }));
    }

    @Override
    public void changeDeliveryDateAction() {
        mView.openDeliveryDateScreen();
    }

    @Override
    public void sendMessage(String messageText) {

        if (!TextUtils.isEmpty(messageText)) {
            MessageResponse messageResponse = new MessageResponse();

            messageResponse.user = mSignedUserManager.getCurrentUser();
            messageResponse.text = messageText;

            mMessagesDH.add(0, new MessagesDH(messageResponse, mGoodDealResponse, mSignedUserManager.getCurrentUser(), mContext, Constants.DEFAULT_MSG_GROUP_TYPE));

            mCompositeDisposable.add(mSocketModel.sendMessage(mGoodDealResponse.id, messageText)
                    .subscribe(aVoid -> {
                    }));

            mView.addItem(mMessagesDH);

            mView.clearInputText();
        }
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
                case Constants.M11_3_GOOD_DEAL_CONFIRMATION_REJECTED:
                case Constants.M11_2_GOOD_DEAL_CONFIRMATION_APPLYED:
                    return Constants.M11_1_M11_2_M11_3_MSG_GROUP_TYPE;
                default:
                    throw new RuntimeException("MessagesDH :: typeDistributor [Can find needed group type]");
            }
        } else {
            return Constants.DEFAULT_MSG_GROUP_TYPE;
        }
    }

    @Override
    public void clickedCreateOrder() {
        mView.openCreateOrderPopUp();
    }

    @Override
    public void resultQuantity(int _quantity) {
        if (_quantity == 0) mView.openDeleteOrderScreen();
        else if (!mGoodDealResponseManager.getGoodDealResponse().hasOrders)
            mView.openCreateOrderFlow(_quantity);
    }

    @Override
    public void sendOrders() {
        mView.openSendOrderListFlow();
    }

    @Override
    public void selectImage() {
        mView.selectImage();
    }

    @Override
    public void sendImage(File croppedFile) {

        mCompositeDisposable.add(mSocketModel.sendImage(mGoodDealResponse.id, getBase64(croppedFile))
                .subscribe(aVoid -> {}));

        MessageResponse messageResponse = new MessageResponse();

        messageResponse.user = mSignedUserManager.getCurrentUser();
        messageResponse.localImage = croppedFile;

        mMessagesDH.add(0, new MessagesDH(messageResponse, mGoodDealResponse, mSignedUserManager.getCurrentUser(), mContext, Constants.DEFAULT_MSG_GROUP_TYPE));

        mView.addItem(mMessagesDH);

    }

    private String getBase64(File _croppedFile) {
        String imageBase64 = "";
        if (_croppedFile != null) {
            Bitmap bm = BitmapFactory.decodeFile(_croppedFile.getAbsolutePath());
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] b = baos.toByteArray();
            imageBase64 = Base64.encodeToString(b, Base64.DEFAULT);

            try {
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return imageBase64;
    }

    private void disconnectSocket() {
        mSocketModel.disconnectSocket()
                .subscribe();
    }

    @Override
    public void unsubscribe() {
        disconnectSocket();
        mCompositeDisposable.clear();
    }
}
