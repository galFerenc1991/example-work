package com.ferenc.pamp.presentation.screens.main.propose.delivery.delivery_place.delivery_place_adapter;

import com.michenko.simpleadapter.RecyclerDH;

/**
 * Created by shonliu on 12/19/17.
 */

public class DeliveryPlaceDH implements RecyclerDH {

    private String mCountry;
    private boolean isReBroadcast;

    public DeliveryPlaceDH(String _country, boolean _isRebroadcast) {
        mCountry = _country;
        isReBroadcast = _isRebroadcast;
    }

    public String getCountry() {
        return mCountry;
    }

    public boolean isReBroadcast() {
        return isReBroadcast;
    }
}
