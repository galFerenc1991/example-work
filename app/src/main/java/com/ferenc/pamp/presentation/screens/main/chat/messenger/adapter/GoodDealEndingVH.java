package com.ferenc.pamp.presentation.screens.main.chat.messenger.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.ferenc.pamp.R;
import com.ferenc.pamp.presentation.utils.Constants;
import com.michenko.simpleadapter.OnCardClickListener;
import com.michenko.simpleadapter.RecyclerVH;

/**
 * Created by shonliu on 12/13/17.
 */

public class GoodDealEndingVH extends RecyclerVH<MessagesDH> {

    private TextView tvDealEndingState;
    private View viewUnderline;

    GoodDealEndingVH(View itemView) {
        super(itemView);
        tvDealEndingState = itemView.findViewById(R.id.tvDealEndingState_IMGDE);
        viewUnderline = itemView.findViewById(R.id.viewUnderline_IMGDE);

    }

    @Override
    public void setListeners(OnCardClickListener listener) {

    }

    @Override
    public void bindData(MessagesDH data) {

        Context context = data.getContext();
        switch (data.getMessageResponse().code) {
            case Constants.M10_GOOD_DEAL_CLOSING:
                tvDealEndingState.setText(R.string.text_bon_plan_close);
                tvDealEndingState.setTextColor(context.getResources().getColor(R.color.textColorBlack));
                viewUnderline.setBackgroundColor(context.getResources().getColor(R.color.textColorBlack));
                break;
            case Constants.M8_GOOD_DEAL_CANCELLATION:
                tvDealEndingState.setText(R.string.text_bon_plan_annule);
                tvDealEndingState.setTextColor(context.getResources().getColor(R.color.colorRed));
                viewUnderline.setBackgroundColor(context.getResources().getColor(R.color.colorRed));
                break;
        }

    }
}
