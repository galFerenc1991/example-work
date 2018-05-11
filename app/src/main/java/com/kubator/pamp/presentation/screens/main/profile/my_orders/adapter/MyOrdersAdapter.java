package com.kubator.pamp.presentation.screens.main.profile.my_orders.adapter;

import android.support.annotation.NonNull;
import android.view.View;

import com.kubator.pamp.R;
import com.michenko.simpleadapter.RecyclerAdapter;
import com.michenko.simpleadapter.RecyclerVH;

import org.androidannotations.annotations.EBean;

/**
 * Created by shonliu on 1/10/18.
 */
@EBean
public class MyOrdersAdapter extends RecyclerAdapter<MyOrderDH> {

    @NonNull
    @Override
    protected RecyclerVH<MyOrderDH> createVH(View view, int viewType) {
        return new MyOrdersVH(view);
    }

    @Override
    protected int getLayoutRes(int viewType) {
        return R.layout.item_my_order;
    }
}
