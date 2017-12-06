package com.ferenc.pamp.presentation.screens.main.propose.delivery;

import com.ferenc.pamp.presentation.base.BasePresenter;
import com.ferenc.pamp.presentation.base.BaseView;
import com.ferenc.pamp.presentation.base.content.ContentView;

import java.util.Calendar;

/**
 * Created by
 * Ferenc on 2017.11.22..
 */

public interface DeliveryContract {

    interface View extends ContentView, BaseView<Presenter> {
        void openDatePicker(Calendar _calendar);

        void openLocationScreen();

        void openDateScreen();

        void setCloseDate(String _closeDate);

        void setDeliveryDate(String _deliveryDate);

        void setDeliveryPlace(String _deliveryPlace);

    }

    interface Presenter extends BasePresenter {
        void clickedCloseDate();

        void clickedDeliveryPlace();

        void clickedDeliveryDate();

        void setCloseDate(Calendar _closeDate);

        void setDeliveryDate(String _startDate, String _endDate);

        void setDeliveryPlace(String _selectedPlace);
    }
}
