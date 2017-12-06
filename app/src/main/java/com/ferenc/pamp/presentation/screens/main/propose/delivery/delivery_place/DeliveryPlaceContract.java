package com.ferenc.pamp.presentation.screens.main.propose.delivery.delivery_place;

import com.ferenc.pamp.presentation.base.BaseModel;
import com.ferenc.pamp.presentation.base.BasePresenter;
import com.ferenc.pamp.presentation.base.BaseView;

/**
 * Created by
 * Ferenc on 2017.11.29..
 */

public interface DeliveryPlaceContract {
    interface View extends BaseView<Presenter> {
        void openAutocompletePlaceScreen();

        void closeScreen();

        void returnPlace(String _selectedPlace);
    }

    interface Presenter extends BasePresenter {
        void clickedBack();

        void clickedSelectPlace();

        void placeSelected(String _selectedPlace);
    }

    interface Model extends BaseModel {

    }
}
