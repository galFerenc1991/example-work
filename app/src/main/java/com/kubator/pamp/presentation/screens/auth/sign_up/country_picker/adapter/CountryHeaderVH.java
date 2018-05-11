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

public class CountryHeaderVH extends RecyclerVH<CountryDH> {

    private TextView tvHeader;

    public CountryHeaderVH(View itemView) {
        super(itemView);
        tvHeader = findView(R.id.tvHeader_ICH);
    }

    @Override
    public void setListeners(OnCardClickListener listener) {

    }

    @Override
    public void bindData(CountryDH data) {
        tvHeader.setText(data.header);
    }
}
