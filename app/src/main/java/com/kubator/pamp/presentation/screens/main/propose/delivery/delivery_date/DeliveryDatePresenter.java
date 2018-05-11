package com.kubator.pamp.presentation.screens.main.propose.delivery.delivery_date;

import android.widget.Toast;

import com.kubator.pamp.PampApp_;
import com.kubator.pamp.data.model.home.good_deal.GoodDealRequest;
import com.kubator.pamp.presentation.utils.DateManager;
import com.kubator.pamp.presentation.utils.GoodDealManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by
 * Ferenc on 2017.11.29..
 */

public class DeliveryDatePresenter implements DeliveryDateContract.Presenter {

    private DeliveryDateContract.View mView;
    private Calendar mStartDate;
    private Calendar mEndDate;
    private GoodDealManager mGoodDealManager;
    private GoodDealRequest mGoodDeal;
    private boolean mIsRebroadcast;
    private long mStartDeliveryDate;
    private long mEndDeliveryDate;

    public DeliveryDatePresenter(DeliveryDateContract.View _view,
                                 GoodDealManager _goodDealManager,
                                 boolean _isRebroadcast,
                                 long _startDeliveryDate,
                                 long _endDeliveryDate) {
        this.mView = _view;
        this.mGoodDealManager = _goodDealManager;
        this.mIsRebroadcast = _isRebroadcast;
        this.mStartDeliveryDate = _startDeliveryDate;
        this.mEndDeliveryDate = _endDeliveryDate;

        mView.setPresenter(this);
    }

    @Override
    public void subscribe() {
        mGoodDeal = mGoodDealManager.getGoodDeal();
        if (mGoodDeal.getDeliveryStartDate() != 0) {
            mView.setStartDate(convertServerDateToString(mGoodDeal.getDeliveryStartDate()));
            mView.setEndDate(convertServerDateToString(mGoodDeal.getDeliveryEndDate()));
        }
        if (mEndDeliveryDate != 0) {
            mView.setStartDate(convertServerDateToString(mStartDeliveryDate));
            mView.setEndDate(convertServerDateToString(mEndDeliveryDate));
        }
    }

    @Override
    public void clickedBack() {
        mView.closeScreen();
    }

    @Override
    public void clickedStartDate() {
        mView.selectStartDate(mStartDate == null ? Calendar.getInstance() : mStartDate);
    }

    @Override
    public void clickedEndDate() {
        mView.selectEndDate(mEndDate == null ? Calendar.getInstance() : mEndDate);
    }

    @Override
    public void setStartDate(Calendar _startDate) {
        mStartDate = _startDate;
        mGoodDeal = mGoodDealManager.getGoodDeal();

        if (mGoodDeal.getClosingDate() < _startDate.getTimeInMillis()) {
            mGoodDeal.setDeliveryStartDate(getDateForServer(_startDate));
            mGoodDealManager.saveGoodDeal(mGoodDeal);
            mView.setStartDate(getDateTOString(_startDate));
        } else {
            Toast.makeText(PampApp_.getInstance(), "Please select Start Delivery Date later Close Date", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void setEndDate(Calendar _endDate) {
        mEndDate = _endDate;
        mGoodDeal = mGoodDealManager.getGoodDeal();
        mGoodDeal.setDeliveryEndDate(getDateForServer(_endDate));
        mGoodDealManager.saveGoodDeal(mGoodDeal);
        mView.setEndDate(getDateTOString(_endDate));
    }

    @Override
    public void clickedValidate() {
        if (mStartDate != null && mEndDate != null) {
            if (DateManager.startDateIsBeforeEndDate(mStartDate, mEndDate)) {
                mView.setDeliveryDate(getDateTOString(mStartDate), getDateTOString(mEndDate));
            } else {
                Toast.makeText(PampApp_.getInstance(), "Sélectionnez la date dans la futur", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(PampApp_.getInstance(), "S'il vous plaît, sélectionnez les deux dates", Toast.LENGTH_SHORT).show();
        }
    }

    private String getDateTOString(Calendar calendar) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM 'at' HH:mm", Locale.FRANCE);
        return sdf.format(calendar.getTime());
    }

    private String convertServerDateToString(long _dateInMillis) {
        Calendar date = Calendar.getInstance();
        date.setTimeInMillis(_dateInMillis);
        return getDateTOString(date);
    }

    private Long getDateForServer(Calendar calendar) {
        return calendar.getTimeInMillis();
    }

    @Override
    public void unsubscribe() {

    }
}
