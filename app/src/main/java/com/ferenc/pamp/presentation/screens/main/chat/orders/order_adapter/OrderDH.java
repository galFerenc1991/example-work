package com.ferenc.pamp.presentation.screens.main.chat.orders.order_adapter;

import com.ferenc.pamp.data.model.home.orders.Order;
import com.michenko.simpleadapter.RecyclerDH;

/**
 * Created by
 * Ferenc on 2018.01.03..
 */

public class OrderDH implements RecyclerDH {

    private Order mOrder;
    private boolean isOriginal;
    private String dealStatus;
    private boolean isSelected;
    private int mItemType;

    public OrderDH(Order mOrder, boolean isOriginal, String dealStatus, int _itemType) {
        this.mOrder = mOrder;
        this.isOriginal = isOriginal;
        this.dealStatus = dealStatus;
        this.mItemType = _itemType;
    }

    public Order getmOrder() {
        return mOrder;
    }

    public boolean isOriginal() {
        return isOriginal;
    }

    public String getDealStatus() {
        return dealStatus;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getItemType() {
        return mItemType;
    }
}
