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

    GoodDealConfirmationVH(View itemView) {
        super(itemView);
        rlBackground                    = itemView.findViewById(R.id.rlBackground_IMGDC);
        ivGoodDealConfirmationState     = itemView.findViewById(R.id.ivGoodDealConfirmationState_IMGDC);
        tvStateConfirmationDescription  = itemView.findViewById(R.id.tvStateConfirmationDescription_IMGDC);
    }

    @Override
    public void setListeners(OnCardClickListener listener) {

    }

    @Override
    public void bindData(MessagesDH data) {

        switch (data.getMessageResponse().code) {
            case Constants.M11_1_GOOD_DEAL_CONFIRMATION:
                rlBackground.setBackground(data.getContext().getResources().getDrawable(R.drawable.bg_msg_good_deal_confirmation_green));
                ivGoodDealConfirmationState.setImageDrawable(data.getContext().getResources().getDrawable(R.drawable.ic_gift_msg));
                tvStateConfirmationDescription.setText(PampApp_.getInstance().getString(R.string.text_bon_plan_confirme));
                break;
            case Constants.M11_2_GOOD_DEAL_CONFIRMATION_REJECTED:
                rlBackground.setBackground(data.getContext().getResources().getDrawable(R.drawable.bg_msg_good_deal_confirmation_red));
                ivGoodDealConfirmationState.setImageDrawable(data.getContext().getResources().getDrawable(R.drawable.ic_finger_down_msg));
                tvStateConfirmationDescription.setText(
                        data.getMessageResponse().description != null
                                ? data.getMessageResponse().description.firstName
                                + " : "
                                + PampApp_.getInstance().getString(R.string.text_commande_annule)
                                : " : "
                                + PampApp_.getInstance().getString(R.string.text_commande_annule)
                );
                break;
            case Constants.M11_3_GOOD_DEAL_CONFIRMATION_APPLYED:
                rlBackground.setBackground(data.getContext().getResources().getDrawable(R.drawable.bg_msg_good_deal_confirmation_green));
                ivGoodDealConfirmationState.setImageDrawable(data.getContext().getResources().getDrawable(R.drawable.ic_finger_up_msg));
                tvStateConfirmationDescription.setText(
                        data.getMessageResponse().description != null
                                ? data.getMessageResponse().description.firstName
                                + " : "
                                + PampApp_.getInstance().getString(R.string.text_commande_confirmee)
                                : " : "
                                + PampApp_.getInstance().getString(R.string.text_commande_confirmee)
                );
                break;
        }


    }
}
