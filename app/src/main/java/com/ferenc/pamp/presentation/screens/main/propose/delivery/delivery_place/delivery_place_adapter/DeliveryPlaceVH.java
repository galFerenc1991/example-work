package com.ferenc.pamp.presentation.screens.main.propose.delivery.delivery_place.delivery_place_adapter;

import android.view.View;
import android.widget.TextView;

import com.ferenc.pamp.R;
import com.michenko.simpleadapter.OnCardClickListener;
import com.michenko.simpleadapter.RecyclerVH;

/**
 * Created by shonliu on 12/19/17.
 */

public class DeliveryPlaceVH extends RecyclerVH<DeliveryPlaceDH> {


    private TextView country;

    public DeliveryPlaceVH(View itemView) {
        super(itemView);
        country = itemView.findViewById(R.id.tvAddress_IDP);
    }

    @Override
    public void setListeners(OnCardClickListener listener) {
        itemView.setOnClickListener(view -> listener.onClick(itemView, getAdapterPosition(), getItemViewType()));
    }

    @Override
    public void bindData(DeliveryPlaceDH data) {
        country.setText(data.getCountry());
    }
}
