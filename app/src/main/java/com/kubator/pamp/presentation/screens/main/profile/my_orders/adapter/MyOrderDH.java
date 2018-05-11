package com.kubator.pamp.presentation.screens.main.profile.my_orders.adapter;

import com.michenko.simpleadapter.RecyclerDH;

/**
 * Created by shonliu on 1/10/18.
 */

public class MyOrderDH implements RecyclerDH {

    private String mOrderId;
    private String mProductName;
    private String mOrderTitle;
    private long mCreatedAt;

    public MyOrderDH(String _orderId, String _productName, String _orderTitle, long _createdAt) {
        mOrderId = _orderId;
        mProductName = _productName;
        mOrderTitle = _orderTitle;
        mCreatedAt = _createdAt;
    }

    public String getOrderId() {
        return mOrderId;
    }

    public String getProductName() {
        return mProductName;
    }

    public String getOrderTitle() {
        return mOrderTitle;
    }

    public long getCreatedAt() {
        return mCreatedAt;
    }
}
