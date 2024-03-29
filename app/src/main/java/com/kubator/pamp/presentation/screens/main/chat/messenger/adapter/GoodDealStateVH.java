package com.kubator.pamp.presentation.screens.main.chat.messenger.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kubator.pamp.PampApp_;
import com.kubator.pamp.R;
import com.kubator.pamp.data.model.message.MessageResponse;
import com.kubator.pamp.presentation.utils.Constants;
import com.michenko.simpleadapter.OnCardClickListener;
import com.michenko.simpleadapter.RecyclerVH;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by shonliu on 12/13/17.
 */

public class GoodDealStateVH extends RecyclerVH<MessagesDH> {

    private ImageView ivGoodDealState;
    private TextView tvStateDescription;

    GoodDealStateVH(View itemView) {
        super(itemView);
        ivGoodDealState = itemView.findViewById(R.id.ivGoodDealState_IMGDS);
        tvStateDescription = itemView.findViewById(R.id.tvStateDescription_IMGDS);
    }

    @Override
    public void setListeners(OnCardClickListener listener) {

    }

    @Override
    public void bindData(MessagesDH data) {

        Context context = data.getContext();
        MessageResponse messageResponse = data.getMessageResponse();
        String deliveryClosingDate = messageResponse.description != null
                ? new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.FRANCE).format(new Date(messageResponse.description.closingDate))
                : "";
        String deliveryStartDate = messageResponse.description != null
                ? new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.FRANCE).format(new Date(messageResponse.description.deliveryStartDate))
                : "";
        String deliveryEndDate = messageResponse.description != null
                ? new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.FRANCE).format(new Date(messageResponse.description.deliveryEndDate))
                : "";

        switch (messageResponse.code) {
            case Constants.M5_GOOD_DEAL_DELIVERY_DATE_CHANGED:
                ivGoodDealState.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_date_msg));
//                tvStateDescription.setText(context.getString(R.string.text_change_date) + "\n" + deliveryStartDate + "\n" + deliveryEndDate);
                break;
            case Constants.M12_DELIVERY_DATE:
                ivGoodDealState.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_date_msg));
//                tvStateDescription.setText(context.getString(R.string.text_delivery_date));
                break;
            case Constants.M6_GOOD_DEAL_CLOSING_DATE_CHANGED:
                ivGoodDealState.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_hourglass_msg));
//                tvStateDescription.setText(context.getString(R.string.text_change_closing_date) + "\n" + deliveryClosingDate);
                break;
            case Constants.M9_CLOSING_DATE:
                ivGoodDealState.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_hourglass_msg));
//                tvStateDescription.setText(context.getString(R.string.text_closing_date));
                break;
        }
        tvStateDescription.setText(messageResponse.text);
    }
}
