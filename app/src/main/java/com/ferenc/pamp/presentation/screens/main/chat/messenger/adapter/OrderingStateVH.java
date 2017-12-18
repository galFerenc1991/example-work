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
        switch (data.getMessageResponse().code) {
            case Constants.M2_PRODUCT_ORDERING:
                ivOrderState.setImageDrawable(data.getContext().getResources().getDrawable(R.drawable.ic_check_msg));
                rlBackground.setBackground(data.getContext().getResources().getDrawable(R.drawable.bg_msg_product_ordering));
                tvOrderOwnerName.setText(data.getMessageResponse().user != null ? data.getMessageResponse().user.getFirstName() : "user == null");

                break;
            case Constants.M3_ORDER_CHANGING:
                ivOrderState.setImageDrawable(data.getContext().getResources().getDrawable(R.drawable.ic_check_msg));
                rlBackground.setBackground(data.getContext().getResources().getDrawable(R.drawable.bg_msg_product_ordering));
                tvOrderOwnerName.setText(data.getMessageResponse().user != null ? data.getMessageResponse().user.getFirstName() : "user == null" + " " + PampApp_.getInstance().getString(R.string.text_name_a_change));
                break;
            case Constants.M4_ORDER_CANCELLATION:
                ivOrderState.setImageDrawable(data.getContext().getResources().getDrawable(R.drawable.ic_close_msg));
                rlBackground.setBackground(data.getContext().getResources().getDrawable(R.drawable.bg_msg_cancel_ordering));
                tvOrderOwnerName.setText(data.getMessageResponse().user != null ? data.getMessageResponse().user.getFirstName() : "user == null");
                break;
        }
        tvPriceDescription.setText(data.getGoodDealResponse().unit);
    }
}
