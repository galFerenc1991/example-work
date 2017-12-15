package com.ferenc.pamp.presentation.screens.main.chat.messenger.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ferenc.pamp.PampApp;
import com.ferenc.pamp.PampApp_;
import com.ferenc.pamp.R;
import com.ferenc.pamp.presentation.utils.Constants;
import com.michenko.simpleadapter.OnCardClickListener;
import com.michenko.simpleadapter.RecyclerVH;

/**
 * Created by shonliu on 12/13/17.
 */

public class GoodDealEndingVH extends RecyclerVH<MessagesDH> {

    protected TextView tvDealEndingState;
    protected View viewUnderline;

    public GoodDealEndingVH(View itemView) {
        super(itemView);
        tvDealEndingState = (TextView) itemView.findViewById(R.id.tvDealEndingState_IMGDE);
        viewUnderline = (View) itemView.findViewById(R.id.viewUnderline_IMGDE);

    }

    @Override
    public void setListeners(OnCardClickListener listener) {

    }

    @Override
    public void bindData(MessagesDH data) {

        if (data.getMessageResponse().code.equals(Constants.M10_GOOD_DEAL_CLOSING)) {
            tvDealEndingState.setText(R.string.bon_plan_close);
            tvDealEndingState.setTextColor(PampApp_.getInstance().getResources().getColor(R.color.textColorBlack));
            viewUnderline.setBackgroundColor(PampApp_.getInstance().getResources().getColor(R.color.textColorBlack));
        } else if(data.getMessageResponse().code.equals(Constants.M8_GOOD_DEAL_CANCELLATION)){
            tvDealEndingState.setText(R.string.bon_plan_annule);
            tvDealEndingState.setTextColor(PampApp_.getInstance().getResources().getColor(R.color.colorRed));
            viewUnderline.setBackgroundColor(PampApp_.getInstance().getResources().getColor(R.color.colorRed));
        }

    }
}
