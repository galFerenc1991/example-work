package com.ferenc.pamp.presentation.screens.main.profile.my_orders.adapter;

import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.ferenc.pamp.R;
import com.michenko.simpleadapter.OnCardClickListener;
import com.michenko.simpleadapter.RecyclerVH;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by shonliu on 1/10/18.
 */

public class MyOrdersVH extends RecyclerVH<MyOrderDH> {

    private TextView tvOrderName;
    private TextView tvOrderDate;

    public MyOrdersVH(View itemView) {
        super(itemView);
        tvOrderName = itemView.findViewById(R.id.tvOrderName_IMO);
        tvOrderDate = itemView.findViewById(R.id.tvOrderDate_IMO);

    }

    @Override
    public void setListeners(OnCardClickListener listener) {
        itemView.setOnClickListener(view -> listener.onClick(itemView, getAdapterPosition(), getItemViewType()));
    }

    @Override
    public void bindData(MyOrderDH data) {
        String deliveryStartDate = new SimpleDateFormat("MM/dd/yyyy", Locale.FRANCE).format(new Date(data.getCreatedAt()));
        String orderTitle = "<b>" + data.getProductName() + "</b> " + "/" + data.getOrderTitle();
        String orderDate = itemView.getContext().getString(R.string.text_my_order_list_date) + deliveryStartDate;



        tvOrderName.setText(Html.fromHtml(orderTitle));

        tvOrderDate.setText(orderDate);
    }
}
