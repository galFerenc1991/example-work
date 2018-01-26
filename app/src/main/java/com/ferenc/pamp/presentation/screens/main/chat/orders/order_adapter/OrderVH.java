package com.ferenc.pamp.presentation.screens.main.chat.orders.order_adapter;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.ferenc.pamp.R;
import com.ferenc.pamp.data.model.home.orders.Order;
import com.ferenc.pamp.presentation.utils.Constants;
import com.michenko.simpleadapter.OnCardClickListener;
import com.michenko.simpleadapter.RecyclerVH;

/**
 * Created by
 * Ferenc on 2018.01.03..
 */

public class OrderVH extends RecyclerVH<OrderDH> {

    private TextView tvName;
    private TextView tvQuantity;
    private TextView tvPrice;
    private RelativeLayout rlConfirmationContainer;
    private TextView tvStatus;
    private Switch swDelivery;

    public OrderVH(View itemView) {
        super(itemView);
        tvName = findView(R.id.tvName_IO);
        tvQuantity = findView(R.id.tvQuantity_IO);
        tvPrice = findView(R.id.tvPrice_IO);
        rlConfirmationContainer = findView(R.id.rlConfirmation_IO);
        tvStatus = findView(R.id.tvConfirmStatus_IO);
        swDelivery = findView(R.id.switchDelivery_IO);
    }

    @Override
    public void setListeners(OnCardClickListener listener) {

    }

    @Override
    public void bindData(OrderDH data) {
        Order order = data.getmOrder();
        if (order.getRank() > 1)
            tvName.setText(order.getUser().getFirstName() + "(" + order.getRank() + ")");
        else tvName.setText(order.getUser().getFirstName());
        tvQuantity.setText(String.valueOf(order.getQuantity()));
        tvPrice.setText(String.valueOf(order.getPrice()) + " €");
        if (data.isOriginal()) {
            switch (data.getDealStatus()) {
                case Constants.STATE_CLOSED:
                    rlConfirmationContainer.setVisibility(View.VISIBLE);
                    swDelivery.setChecked(data.isSelected());
                    if (swDelivery.isChecked()) {
                        tvStatus.setTextColor(tvStatus.getContext().getResources().getColor(R.color.textColorGreen));
                        tvStatus.setText(Constants.STATUS_CONFIRMED_TEXT);//Confirmé
                    } else {
                        tvStatus.setText(Constants.STATUS_CANCELED_TEXT);//Annulé
                        tvStatus.setTextColor(tvStatus.getContext().getResources().getColor(R.color.textColorGray));
                    }
                    swDelivery.setOnClickListener(view -> {
                        data.setSelected(swDelivery.isChecked());
                        if (swDelivery.isChecked()) {
                            tvStatus.setTextColor(tvStatus.getContext().getResources().getColor(R.color.textColorGreen));
                            tvStatus.setText(Constants.STATUS_CONFIRMED_TEXT);//Confirmé
                        } else {
                            tvStatus.setText(Constants.STATUS_CANCELED_TEXT);//Annulé
                            tvStatus.setTextColor(tvStatus.getContext().getResources().getColor(R.color.textColorGray));
                        }
                    });
                    break;
                case Constants.STATE_CONFIRM:
                    if (order.getState().equals(Constants.STATE_CONFIRM)) {
                        rlConfirmationContainer.setVisibility(View.VISIBLE);
                        swDelivery.setChecked(order.isDelivered());
                        swDelivery.setOnClickListener(view -> {
                            data.setSelected(swDelivery.isChecked());
                            if (swDelivery.isChecked()) {
                                tvStatus.setTextColor(tvStatus.getContext().getResources().getColor(R.color.textColorGreen));
                                tvStatus.setText(Constants.STATUS_DELIVER_TEXT);//LIVRER
                            } else {
                                tvStatus.setText(Constants.STATUS_TO_DELIVER_TEXT);//À LIVRER
                                tvStatus.setTextColor(tvStatus.getContext().getResources().getColor(R.color.textColorGray));
                            }
                        });
                    } else {
                        rlConfirmationContainer.setVisibility(View.VISIBLE);
                        swDelivery.setVisibility(View.GONE);
                        tvStatus.setText(Constants.STATUS_CANCEL_TEXT);///ANNULER
                        tvStatus.setTextColor(tvStatus.getContext().getResources().getColor(R.color.textColorGray));
                    }
                    break;
            }
        } else {
            switch (data.getDealStatus()) {
                case Constants.STATE_CLOSED:
                    rlConfirmationContainer.setVisibility(View.VISIBLE);
                    swDelivery.setVisibility(View.GONE);
                    tvStatus.setText(Constants.STATUS_IN_PROGRESS_TEXT);//EN COURS
                    tvStatus.setTextColor(tvStatus.getContext().getResources().getColor(R.color.msgYellowColor));
                    break;
                case Constants.STATE_CONFIRM:
                    if (data.getmOrder().getState().equals(Constants.STATE_CONFIRM)) {
                        rlConfirmationContainer.setVisibility(View.VISIBLE);
                        swDelivery.setOnClickListener(view -> {
                            data.setSelected(swDelivery.isChecked());
                            if (swDelivery.isChecked()) {
                                tvStatus.setTextColor(tvStatus.getContext().getResources().getColor(R.color.textColorGreen));
                                tvStatus.setText(Constants.STATUS_DELIVER_TEXT);//LIVRER
                            } else {
                                tvStatus.setText(Constants.STATUS_TO_DELIVER_TEXT);//À LIVRER
                                tvStatus.setTextColor(tvStatus.getContext().getResources().getColor(R.color.textColorGray));
                            }
                        });
                    } else {
                        rlConfirmationContainer.setVisibility(View.VISIBLE);
                        swDelivery.setVisibility(View.GONE);
                        tvStatus.setText(Constants.STATUS_CANCEL_TEXT);///ANNULER
                        tvStatus.setTextColor(tvStatus.getContext().getResources().getColor(R.color.textColorGray));
                    }
                    break;
            }
        }
    }
}
