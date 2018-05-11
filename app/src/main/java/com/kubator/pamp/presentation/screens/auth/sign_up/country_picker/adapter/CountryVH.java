package com.kubator.pamp.presentation.screens.auth.sign_up.country_picker.adapter;

import android.view.View;
import android.widget.TextView;

import com.kubator.pamp.R;
import com.michenko.simpleadapter.OnCardClickListener;
import com.michenko.simpleadapter.RecyclerVH;

/**
 * Created by
 * Ferenc on 2017.11.20..
 */

public class CountryVH extends RecyclerVH<CountryDH> {

    private TextView tvCountryName;

    public CountryVH(View itemView) {
        super(itemView);
        tvCountryName = findView(R.id.tvCountryName_IC);
    }

    @Override
    public void setListeners(OnCardClickListener listener) {
        itemView.setOnClickListener(view -> listener.onClick(itemView, getAdapterPosition(), getItemViewType()));
    }

    @Override
    public void bindData(CountryDH data) {
        itemView.setSelected(data.isSelected);
        tvCountryName.setText(data.country.getName());
    }
}
