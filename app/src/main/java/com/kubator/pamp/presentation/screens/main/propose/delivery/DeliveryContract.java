package com.kubator.pamp.presentation.screens.main.propose.delivery;

import com.kubator.pamp.presentation.base.BasePresenter;
import com.kubator.pamp.presentation.base.BaseView;
import com.kubator.pamp.presentation.base.content.ContentView;

import java.util.Calendar;

/**
 * Created by
 * Ferenc on 2017.11.22..
 */

public interface DeliveryContract {

    interface View extends ContentView, BaseView<Presenter> {
        void openDatePicker(Calendar _calendar);

        void openInputActivity(int _requestCode);

        void openLocationScreen();

        void openDateScreen();

        void setCloseDate(String _closeDate);

        void setProductDescription(String _nameDescription);

        void setHelpToDeliveryDateText(String _help);

        void setDeliveryDate(String _deliveryDate);

        void setDeliveryPlace(String _deliveryPlace);

    }

    interface Presenter extends BasePresenter {
        void clickedCloseDate();

        void clickedInputField(int _requestCode);

        void clickedDeliveryPlace();

        void clickedDeliveryDate();

        void setCloseDate(Calendar _closeDate);

        void saveProductDescription(String _productDescription);


        void setDeliveryDate(String _startDate, String _endDate);

        void setDeliveryPlace(String _selectedPlace);
    }
}
