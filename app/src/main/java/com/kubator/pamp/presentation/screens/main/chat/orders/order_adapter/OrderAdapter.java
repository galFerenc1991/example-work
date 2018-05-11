package com.kubator.pamp.presentation.screens.main.chat.orders.order_adapter;

import android.support.annotation.NonNull;
import android.view.View;

import com.kubator.pamp.R;
import com.michenko.simpleadapter.RecyclerAdapter;
import com.michenko.simpleadapter.RecyclerVH;

import org.androidannotations.annotations.EBean;

/**
 * Created by
 * Ferenc on 2018.01.02..
 */

@EBean
public class OrderAdapter extends RecyclerAdapter<OrderDH> {

    public static final int TYPE_ORDER = 1;
    public static final int TYPE_TOTAL = 2;

    @NonNull
    @Override
    protected RecyclerVH<OrderDH> createVH(View view, int viewType) {
        switch (viewType) {
            case TYPE_ORDER:
                return new OrderVH(view);
            case TYPE_TOTAL:
                return new TotalVH(view);
            default:
                throw new RuntimeException("OrderAdapter :: createVH [Can find such view type]");
        }
    }

    @Override
    protected int getLayoutRes(int viewType) {
        switch (viewType) {
            case TYPE_ORDER:
                return R.layout.item_order;
            case TYPE_TOTAL:
                return R.layout.item_total;
            default:
                throw new RuntimeException("OrderAdapter :: getLayoutRes [Can find such view type]");
        }
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).getItemType();
    }
}
