package com.ferenc.pamp.presentation.screens.main.propose.description;

import com.ferenc.pamp.presentation.base.BasePresenter;
import com.ferenc.pamp.presentation.base.BaseView;
import com.ferenc.pamp.presentation.base.content.ContentView;

/**
 * Created by
 * Ferenc on 2017.11.22..
 */

public interface DescriptionContract {

    interface View extends ContentView, BaseView<Presenter> {
        void openInputActivity(int _requestCode);

        void setName(String _name);

        void setDescription(String _description);

        void setPrice(String _price);

        void setUnit(String _priceDescription);

        void setQuantity(String _quantity);
    }

    interface Presenter extends BasePresenter {
        void clickedInputField(int _requestCode);

        void saveName(String _name);

        void saveDescription(String _description);

        void savePrice(String _price);

        void savePriceDescription(String _priceDescription);

        void saveQuantity(String _quantity);
    }
}
