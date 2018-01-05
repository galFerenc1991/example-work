package com.ferenc.pamp.presentation.screens.main.chat.orders.order_adapter;

import android.support.annotation.NonNull;
import android.view.View;

import com.ferenc.pamp.R;
import com.michenko.simpleadapter.RecyclerAdapter;
import com.michenko.simpleadapter.RecyclerVH;

import org.androidannotations.annotations.EBean;

/**
 * Created by
 * Ferenc on 2018.01.02..
 */

@EBean
public class OrderAdapter extends RecyclerAdapter<OrderDH> {

    @NonNull
    @Override
    protected RecyclerVH<OrderDH> createVH(View view, int viewType) {
        return new OrderVH(view);
    }

    @Override
    protected int getLayoutRes(int viewType) {
        return R.layout.item_order;
    }
}
