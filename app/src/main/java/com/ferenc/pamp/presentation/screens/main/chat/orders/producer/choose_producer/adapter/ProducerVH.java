package com.ferenc.pamp.presentation.screens.main.chat.orders.producer.choose_producer.adapter;

import android.view.View;
import android.widget.TextView;

import com.ferenc.pamp.R;
import com.ferenc.pamp.presentation.utils.Constants;
import com.jakewharton.rxbinding2.view.RxView;
import com.michenko.simpleadapter.OnCardClickListener;
import com.michenko.simpleadapter.RecyclerVH;

import java.util.concurrent.TimeUnit;

/**
 * Created by shonliu on 1/2/18.
 */

public class ProducerVH extends RecyclerVH<ProducerDH> {

    private TextView tvProducer;

    public ProducerVH(View itemView) {
        super(itemView);
        tvProducer = itemView.findViewById(R.id.tvProducer_IP);
    }

    @Override
    public void setListeners(OnCardClickListener listener) {

    }

    @Override
    public void bindData(ProducerDH data) {
        tvProducer.setText(data.getProducer());

        RxView.clicks(tvProducer)
                .throttleFirst(Constants.CLICK_DELAY, TimeUnit.MILLISECONDS)
                .subscribe(o -> {

                });
    }
}
