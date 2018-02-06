package com.ferenc.pamp.presentation.screens.main.chat.orders.order_adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.ferenc.pamp.R;
import com.ferenc.pamp.data.model.home.orders.Order;
import com.michenko.simpleadapter.OnCardClickListener;
import com.michenko.simpleadapter.RecyclerVH;

/**
 * Created by
 * Ferenc on 2018.01.11..
 */

public class TotalVH extends RecyclerVH<OrderDH> {
    private TextView tvName;
    private TextView tvQuantity;
    private TextView tvPrice;

    public TotalVH(View itemView) {
        super(itemView);
        tvName = findView(R.id.tvName_IT);
        tvQuantity = findView(R.id.tvQuantity_IT);
        tvPrice = findView(R.id.tvPrice_IT);
    }

    @Override
    public void setListeners(OnCardClickListener listener) {

    }

    @Override
    public void bindData(OrderDH data) {
        Context context = tvName.getContext();
        Order order = data.getmOrder();
        tvName.setText("Total");
        tvQuantity.setText(String.valueOf(order.getQuantity()));
        tvPrice.setText(String.valueOf(order.getPrice()) + " €");
    }
}
