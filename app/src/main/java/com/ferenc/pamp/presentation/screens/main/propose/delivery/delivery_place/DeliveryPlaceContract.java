package com.ferenc.pamp.presentation.screens.main.propose.delivery.delivery_place;


import com.ferenc.pamp.presentation.base.BaseModel;
import com.ferenc.pamp.presentation.base.BasePresenter;
import com.ferenc.pamp.presentation.base.BaseView;
import com.ferenc.pamp.presentation.screens.main.propose.delivery.delivery_place.delivery_place_adapter.DeliveryPlaceDH;
import java.util.List;

import io.reactivex.Observable;

/**
 * Created by
 * Ferenc on 2017.11.29..
 */

public interface DeliveryPlaceContract {
    interface View extends  BaseView<Presenter> {
        void openAutocompletePlaceScreen();

        void closeScreen();

        void returnPlace(String _selectedPlace);

        void setContactAdapterList(List<DeliveryPlaceDH> _list);

        void showProgressBar();

        void hideProgressBar();
    }

    interface Presenter extends BasePresenter {
        void clickedBack();

        void clickedSelectPlace();

        void placeSelected(String _selectedPlace);

        void selectItem(DeliveryPlaceDH item, int position);
    }

    interface Model extends BaseModel {
        Observable<List<String>> getUsedUserAddresses();
    }
}
