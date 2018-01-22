package com.ferenc.pamp.presentation.screens.main.chat.messenger.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ferenc.pamp.PampApp_;
import com.ferenc.pamp.R;
import com.ferenc.pamp.data.model.home.good_deal.GoodDealResponse;
import com.ferenc.pamp.data.model.message.MessageResponse;
import com.ferenc.pamp.presentation.utils.Constants;
import com.michenko.simpleadapter.OnCardClickListener;
import com.michenko.simpleadapter.RecyclerVH;

/**
 * Created by shonliu on 12/13/17.
 */

public class OrderingStateVH extends RecyclerVH<MessagesDH> {

    private TextView tvOrderOwnerName;
    private TextView tvPriceDescription;
    private ImageView ivOrderState;
    private RelativeLayout rlBackground;

    public OrderingStateVH(View itemView) {
        super(itemView);
        tvOrderOwnerName = itemView.findViewById(R.id.tvOrderOwnerName_IMOS);
        tvPriceDescription = itemView.findViewById(R.id.tvPriceDescription_IMOS);
        ivOrderState = itemView.findViewById(R.id.ivOrderState_IMOS);
        rlBackground = itemView.findViewById(R.id.rlBackground_IMOS);
    }

    @Override
    public void setListeners(OnCardClickListener listener) {

    }


    @Override
    public void bindData(MessagesDH data) {

        Context context = data.getContext();
        MessageResponse messageResponse = data.getMessageResponse();
        GoodDealResponse goodDealResponse = data.getGoodDealResponse();

        switch (data.getMessageResponse().code) {
            case Constants.M2_PRODUCT_ORDERING:
                ivOrderState.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_check_msg));
                rlBackground.setBackground(context.getResources().getDrawable(R.drawable.bg_msg_product_ordering));
                tvOrderOwnerName.setText(messageResponse.description != null ? messageResponse.description.firstName : "user == null");


                tvPriceDescription.setText(messageResponse.description.getStringQuantity() + "/" + goodDealResponse.unit);
                break;
            case Constants.M3_ORDER_CHANGING:
                ivOrderState.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_check_msg));
                rlBackground.setBackground(context.getResources().getDrawable(R.drawable.bg_msg_product_ordering));
                tvOrderOwnerName.setText(messageResponse.description != null ? messageResponse.description.firstName + " " + context.getString(R.string.text_name_a_change) : "user == null");
                tvPriceDescription.setText(messageResponse.description.getStringQuantity() + "/" + goodDealResponse.unit);
                break;
            case Constants.M4_ORDER_CANCELLATION:
                ivOrderState.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_close_msg));
                rlBackground.setBackground(context.getResources().getDrawable(R.drawable.bg_msg_cancel_ordering));
                tvOrderOwnerName.setText(messageResponse.description != null ? messageResponse.description.firstName + ":" : "user == null" );
                tvPriceDescription.setText(context.getString(R.string.text_commande_annule).toLowerCase());
                break;
        }

    }
}
