package com.ferenc.pamp.presentation.screens.main.chat.orders.producer.choose_producer.adapter;

import android.support.annotation.NonNull;
import android.view.View;

import com.ferenc.pamp.R;
import com.michenko.simpleadapter.RecyclerAdapter;
import com.michenko.simpleadapter.RecyclerVH;

import org.androidannotations.annotations.EBean;

/**
 * Created by shonliu on 1/2/18.
 */
@EBean
public class ProducerAdapter extends RecyclerAdapter<ProducerDH> {

    @NonNull
    @Override
    protected RecyclerVH<ProducerDH> createVH(View view, int viewType) {
        return new ProducerVH(view);
    }

    @Override
    protected int getLayoutRes(int viewType) {
        return R.layout.item_producer;
    }
}
