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

public class GoodDealStateVH extends RecyclerVH<MessagesDH> {

    private ImageView ivGoodDealState;
    private TextView tvStateDescription;

    public GoodDealStateVH(View itemView) {
        super(itemView);
        ivGoodDealState = (ImageView) itemView.findViewById(R.id.ivGoodDealState_IMGDS);
        tvStateDescription = (TextView) itemView.findViewById(R.id.tvStateDescription_IMGDS);
    }

    @Override
    public void setListeners(OnCardClickListener listener) {

    }

    @Override
    public void bindData(MessagesDH data) {

        if (data.getMessageResponse().code.equals(Constants.M5_GOOD_DEAL_DELIVERY_DATE_CHANGED) || data.getMessageResponse().code.equals(Constants.M12_DELIVERY_DATE)) {
            ivGoodDealState.setImageDrawable(PampApp_.getInstance().getResources().getDrawable(R.drawable.ic_date_msg));
        } else if (data.getMessageResponse().code.equals(Constants.M6_GOOD_DEAL_CLOSING_DATE_CHANGED) || data.getMessageResponse().code.equals(Constants.M9_CLOSING_DATE)) {
            ivGoodDealState.setImageDrawable(PampApp_.getInstance().getResources().getDrawable(R.drawable.ic_hourglass_msg));
        }
        tvStateDescription.setText(data.getMessageResponse().text);

    }
}
