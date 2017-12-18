package com.ferenc.pamp.presentation.screens.main.chat.messenger.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ferenc.pamp.PampApp_;
import com.ferenc.pamp.R;
import com.ferenc.pamp.presentation.utils.Constants;
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

        switch (data.getMessageResponse().code) {
            case Constants.M5_GOOD_DEAL_DELIVERY_DATE_CHANGED:
                ivGoodDealState.setImageDrawable(data.getContext().getResources().getDrawable(R.drawable.ic_date_msg));
                tvStateDescription.setText(
                        data.getMessageResponse().description != null
                                ? PampApp_.getInstance().getString(R.string.text_change_date)
                                + "\n"
                                + new SimpleDateFormat("MM/dd/yyyy hh:mm", Locale.FRANCE).format(new Date(data.getMessageResponse().description.deliveryStartDate))
                                : PampApp_.getInstance().getString(R.string.text_change_date)
                );
                break;
            case Constants.M12_DELIVERY_DATE:
                ivGoodDealState.setImageDrawable(data.getContext().getResources().getDrawable(R.drawable.ic_date_msg));
                tvStateDescription.setText(
                        data.getMessageResponse().description != null
                                ? PampApp_.getInstance().getString(R.string.text_delivery_date)
                                + "\n"
                                + new SimpleDateFormat("MM/dd/yyyy hh:mm", Locale.FRANCE).format(new Date(data.getMessageResponse().description.deliveryStartDate))
                                : PampApp_.getInstance().getString(R.string.text_delivery_date)
                );
                break;
            case Constants.M6_GOOD_DEAL_CLOSING_DATE_CHANGED:
                ivGoodDealState.setImageDrawable(data.getContext().getResources().getDrawable(R.drawable.ic_hourglass_msg));
                tvStateDescription.setText(
                        data.getMessageResponse().description != null
                                ? PampApp_.getInstance().getString(R.string.text_change_closing_date)
                                + "\n"
                                + new SimpleDateFormat("MM/dd/yyyy hh:mm", Locale.FRANCE).format(new Date(data.getMessageResponse().description.deliveryStartDate))
                                : PampApp_.getInstance().getString(R.string.text_change_closing_date)
                );
                break;
            case Constants.M9_CLOSING_DATE:
                ivGoodDealState.setImageDrawable(data.getContext().getResources().getDrawable(R.drawable.ic_hourglass_msg));
                tvStateDescription.setText(
                        data.getMessageResponse().description != null
                                ? PampApp_.getInstance().getString(R.string.text_closing_date)
                                + "\n"
                                + new SimpleDateFormat("MM/dd/yyyy hh:mm", Locale.FRANCE).format(new Date(data.getMessageResponse().description.deliveryStartDate))
                                : PampApp_.getInstance().getString(R.string.text_closing_date)
                );
                break;
        }
    }
}
