package com.ferenc.pamp.presentation.screens.main.propose.delivery;

import android.support.annotation.NonNull;

import com.ferenc.pamp.data.model.home.good_deal.GoodDealRequest;
import com.ferenc.pamp.presentation.utils.GoodDealManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by
 * Ferenc on 2017.11.22..
 */

public class DeliveryPresenter implements DeliveryContract.Presenter {

    private DeliveryContract.View mView;
    private GoodDealManager mGoodDealManager;
    private Calendar mCloseDate;
    private GoodDealRequest mGoodDeal;
    private boolean mIsReBroadcastFlow;

    public DeliveryPresenter(DeliveryContract.View _view, GoodDealManager _goodDealManager, boolean _isReBroadcastFlow) {
        this.mView = _view;
        this.mGoodDealManager = _goodDealManager;
        this.mIsReBroadcastFlow = _isReBroadcastFlow;

        mView.setPresenter(this);
    }

    @Override
    public void subscribe() {
        GoodDealRequest reUseGoodDeal = mGoodDealManager.getGoodDeal();
        if (reUseGoodDeal.getProduct() != null) {
            if (!mIsReBroadcastFlow) {
//                mView.setCloseDate(convertServerDateToString(reUseGoodDeal.getClosingDate()));
//                mView.setDeliveryDate(convertServerDateToString(reUseGoodDeal.getDeliveryStartDate()) + "\n" + convertServerDateToString(reUseGoodDeal.getDeliveryEndDate()));
                mView.setDeliveryPlace(reUseGoodDeal.getDeliveryAddress());
            } else {
                mView.setProductDescription(reUseGoodDeal.getDescription());
            }
        }
    }

    @Override
    public void clickedCloseDate() {
        mView.openDatePicker(mCloseDate == null ? Calendar.getInstance() : mCloseDate);
    }

    @Override
    public void setCloseDate(Calendar _closeDate) {
        mCloseDate = _closeDate;
        mGoodDeal = mGoodDealManager.getGoodDeal();
        mGoodDeal.setClosingDate(getCloseDateForServer(mCloseDate));
        mGoodDealManager.saveGoodDeal(mGoodDeal);
        mView.setCloseDate(getCloseDateInString(mCloseDate));
    }

    @Override
    public void clickedInputField(int _requestCode) {
        mView.openInputActivity(_requestCode);
    }

    @Override
    public void saveProductDescription(String _productDescription) {
        mGoodDeal = mGoodDealManager.getGoodDeal();
        mGoodDeal.setDescription(_productDescription);
        mGoodDealManager.saveGoodDeal(mGoodDeal);
        mView.setProductDescription(_productDescription);
    }

    @Override
    public void setDeliveryDate(String _startDate, String _endDate) {
        mView.setDeliveryDate(_startDate + "\n" + _endDate);
    }

    @Override
    public void setDeliveryPlace(String _selectedPlace) {
        mGoodDeal = mGoodDealManager.getGoodDeal();
        mGoodDeal.setDeliveryAddress(_selectedPlace);
        mGoodDealManager.saveGoodDeal(mGoodDeal);
        mView.setDeliveryPlace(_selectedPlace);
    }

    @Override
    public void clickedDeliveryPlace() {
        mView.openLocationScreen();
    }

    @Override
    public void clickedDeliveryDate() {
        mView.openDateScreen();
    }

    private String getCloseDateInString(Calendar calendar) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM 'at' HH:mm", Locale.getDefault());
        return sdf.format(calendar.getTime());
    }

    @NonNull
    private Long getCloseDateForServer(Calendar calendar) {
        return calendar.getTimeInMillis();
    }

    @Override
    public void unsubscribe() {

    }
}
