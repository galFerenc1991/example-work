package com.kubator.pamp.presentation.screens.main.propose.delivery.delivery_place.delivery_place_adapter;

import android.support.annotation.NonNull;
import android.view.View;

import com.kubator.pamp.R;
import com.michenko.simpleadapter.RecyclerAdapter;
import com.michenko.simpleadapter.RecyclerVH;

import org.androidannotations.annotations.EBean;

/**
 * Created by shonliu on 12/19/17.
 */

@EBean
public class DeliveryPlaceAdapter extends RecyclerAdapter<DeliveryPlaceDH> {

    @NonNull
    @Override
    protected RecyclerVH<DeliveryPlaceDH> createVH(View view, int viewType) {
        return new DeliveryPlaceVH(view);
    }

    @Override
    protected int getLayoutRes(int viewType) {
        return R.layout.item_delivery_place;
    }
}
