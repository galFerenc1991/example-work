package com.ferenc.pamp.presentation.screens.main.chat.messenger.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ferenc.pamp.PampApp_;
import com.ferenc.pamp.R;
import com.ferenc.pamp.presentation.utils.Constants;
import com.michenko.simpleadapter.OnCardClickListener;
import com.michenko.simpleadapter.RecyclerVH;

/**
 * Created by shonliu on 12/13/17.
 */

public class GoodDealConfirmationVH extends RecyclerVH<MessagesDH> {


    private RelativeLayout rlBackground;
    private ImageView ivGoodDealConfirmationState;
    private TextView tvStateConfirmationDescription;

    public GoodDealConfirmationVH(View itemView) {
        super(itemView);
        rlBackground = (RelativeLayout) itemView.findViewById(R.id.rlBackground_IMGDC);
        ivGoodDealConfirmationState = (ImageView) itemView.findViewById(R.id.ivGoodDealConfirmationState_IMGDC);
        tvStateConfirmationDescription = (TextView) itemView.findViewById(R.id.tvStateConfirmationDescription_IMGDC);
    }

    @Override
    public void setListeners(OnCardClickListener listener) {

    }

    @Override
    public void bindData(MessagesDH data) {

        if (data.getMessageResponse().code.equals(Constants.M11_1_GOOD_DEAL_CONFIRMATION)) {
            rlBackground.setBackground(PampApp_.getInstance().getResources().getDrawable(R.drawable.bg_msg_good_deal_confirmation_green));
            ivGoodDealConfirmationState.setImageDrawable(PampApp_.getInstance().getResources().getDrawable(R.drawable.ic_gift_msg));
        } else if (data.getMessageResponse().code.equals(Constants.M11_2_GOOD_DEAL_CONFIRMATION_REJECTED)) {
            rlBackground.setBackground(PampApp_.getInstance().getResources().getDrawable(R.drawable.bg_msg_good_deal_confirmation_red));
            ivGoodDealConfirmationState.setImageDrawable(PampApp_.getInstance().getResources().getDrawable(R.drawable.ic_finger_down_msg));
        } else if (data.getMessageResponse().code.equals(Constants.M11_3_GOOD_DEAL_CONFIRMATION_APPLYED)) {
            rlBackground.setBackground(PampApp_.getInstance().getResources().getDrawable(R.drawable.bg_msg_good_deal_confirmation_green));
            ivGoodDealConfirmationState.setImageDrawable(PampApp_.getInstance().getResources().getDrawable(R.drawable.ic_finger_up_msg));
        }
        tvStateConfirmationDescription.setText(data.getMessageResponse().text);

    }
}
