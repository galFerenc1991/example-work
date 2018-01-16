package com.ferenc.pamp.presentation.screens.main.chat.orders.producer.choose_producer.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ferenc.pamp.R;
import com.ferenc.pamp.presentation.screens.main.chat.orders.producer.choose_producer.create_update_producer.CreateUpdateProducerActivity_;
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
    private ImageView ivEditProducer;
    private Context context;

    ProducerVH(View itemView) {
        super(itemView);
        tvProducer = itemView.findViewById(R.id.tvProducer_IP);
        ivEditProducer = itemView.findViewById(R.id.ivEditProducer_IP);
    }

    @Override
    public void setListeners(OnCardClickListener listener) {
        itemView.setOnClickListener(view -> listener.onClick(itemView, getAdapterPosition(), getItemViewType()));
    }

    @Override
    public void bindData(ProducerDH data) {

        context = itemView.getContext();
        tvProducer.setText(data.getProducer().name);

        if (data.isSelected()) {
            select();
        } else {
            unselected();
        }

        RxView.clicks(ivEditProducer)
                .throttleFirst(Constants.CLICK_DELAY, TimeUnit.MILLISECONDS)
                .subscribe(o -> CreateUpdateProducerActivity_
                        .intent(context)
                        .isCreate(false)
                        .mProducer(data.getProducer())
                        .startForResult(Constants.REQUEST_CODE_ACTIVITY_UPDATE_PRODUCER));

    }

    private void unselected() {
        tvProducer.setTextColor(context.getResources().getColor(R.color.colorBlackTransparent));
    }

    private void select() {
        tvProducer.setTextColor(context.getResources().getColor(R.color.msgMyGoodDealDiffusionColor));
    }
}
