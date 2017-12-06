package com.ferenc.pamp.presentation.screens.main.propose.delivery.delivery_date;

import com.ferenc.pamp.presentation.base.BasePresenter;
import com.ferenc.pamp.presentation.base.BaseView;

import java.util.Calendar;

/**
 * Created by
 * Ferenc on 2017.11.29..
 */

public interface DeliveryDateContract {

    interface View extends BaseView<Presenter> {

        void closeScreen();

        void selectStartDate(Calendar _startDate);

        void selectEndDate(Calendar _endDate);

        void setDeliveryDate(String _startDate, String _endDate);

        void setStartDate(String _startDate);

        void setEndDate(String _endDate);
    }

    interface Presenter extends BasePresenter {
        void clickedBack();

        void clickedStartDate();

        void clickedEndDate();

        void setStartDate(Calendar _startDate);

        void setEndDate(Calendar _endDate);

        void clickedValidate();
    }
}
