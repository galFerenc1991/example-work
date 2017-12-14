package com.ferenc.pamp.presentation.screens.main.chat.messenger.adapter;

import android.media.Image;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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

public class OrderingStateVH extends RecyclerVH<MessagesDH> {

    private TextView tvOrderOwnerName;
    private TextView tvPriceDescription;
    private ImageView ivOrderState;
    private RelativeLayout rlBackground;

    public OrderingStateVH(View itemView) {
        super(itemView);
        tvOrderOwnerName = (TextView) itemView.findViewById(R.id.tvOrderOwnerName_IMOS);
        tvPriceDescription = (TextView) itemView.findViewById(R.id.tvPriceDescription_IMOS);
        ivOrderState = (ImageView) itemView.findViewById(R.id. ivOrderState_IMOS);
        rlBackground = (RelativeLayout) itemView.findViewById(R.id.rlBackground_IMOS);
    }

    @Override
    public void setListeners(OnCardClickListener listener) {

    }

    @Override
    public void bindData(MessagesDH data) {
        if (data.getMessageResponse().code.equals(Constants.M2_PRODUCT_ORDERING) || data.getMessageResponse().code.equals(Constants.M3_ORDER_CHANGING)) {
            ivOrderState.setImageDrawable(PampApp_.getInstance().getResources().getDrawable(R.drawable.ic_check_msg));
            rlBackground.setBackground(PampApp_.getInstance().getResources().getDrawable(R.drawable.bg_msg_product_ordering));
        } else if (data.getMessageResponse().code.equals(Constants.M4_ORDER_CANCELLATION)) {
            ivOrderState.setImageDrawable(PampApp_.getInstance().getResources().getDrawable(R.drawable.ic_close_msg));
            rlBackground.setBackground(PampApp_.getInstance().getResources().getDrawable(R.drawable.bg_msg_cancel_ordering));
        }
        tvOrderOwnerName.setText(data.getMessageResponse().user.getFirstName());
        tvPriceDescription.setText(data.getMessageResponse().text);
    }
}
