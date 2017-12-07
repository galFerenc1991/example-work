package com.ferenc.pamp.presentation.screens.main.propose.delivery;

import com.ferenc.pamp.presentation.base.models.GoodDeal;
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
    private GoodDeal mGoodDeal;

    public DeliveryPresenter(DeliveryContract.View _view, GoodDealManager _goodDealManager) {
        this.mView = _view;
        this.mGoodDealManager = _goodDealManager;

        mView.setPresenter(this);
    }

    @Override
    public void clickedCloseDate() {
        mView.openDatePicker(mCloseDate == null ? Calendar.getInstance() : mCloseDate);
    }

    @Override
    public void setCloseDate(Calendar _closeDate) {
        mCloseDate = _closeDate;
        mGoodDeal = mGoodDealManager.getGoodDeal();
        mGoodDeal.setCloseDate(getCloseDateForServer(mCloseDate));
        mGoodDealManager.saveGoodDeal(mGoodDeal);
        mView.setCloseDate(getCloseDateString(mCloseDate));
    }

    @Override
    public void setDeliveryDate(String _startDate, String _endDate) {
//        mGoodDeal = mGoodDealManager.getGoodDeal();
//        mGoodDeal.setStartDelivery(_startDate);
//        mGoodDeal.setEndDelivery(_endDate);
//        mGoodDealManager.saveGoodDeal(mGoodDeal);
        mView.setDeliveryDate(_startDate + "\n" + _endDate);
    }

    @Override
    public void setDeliveryPlace(String _selectedPlace) {
        mGoodDeal = mGoodDealManager.getGoodDeal();
        mGoodDeal.setDeliveryPlace(_selectedPlace);
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

    private String getCloseDateString(Calendar calendar) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM 'at' HH:mm", Locale.getDefault());
        return sdf.format(calendar.getTime());
    }

    private Long getCloseDateForServer(Calendar calendar) {
//        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss zzz", Locale.getDefault());
//        return sdf.format(calendar.getTime());
        return calendar.getTimeInMillis();
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {

    }
}
