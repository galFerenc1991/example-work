package com.ferenc.pamp.presentation.screens.main.chat.orders.producer.choose_producer.adapter;

import android.view.View;
import android.widget.TextView;

import com.ferenc.pamp.PampApp_;
import com.ferenc.pamp.R;
import com.michenko.simpleadapter.OnCardClickListener;
import com.michenko.simpleadapter.RecyclerVH;

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
        itemView.setOnClickListener(view -> listener.onClick(itemView, getAdapterPosition(), getItemViewType()));
    }

    @Override
    public void bindData(ProducerDH data) {

        tvProducer.setText(data.getProducer());

        if (data.isSelected()) {
            select();
        } else {
            unselected();
        }

    }
    private void unselected() {
        tvProducer.setTextColor(PampApp_.getInstance().getResources().getColor(R.color.colorBlackTransparent));
    }
    private void select() {
        tvProducer.setTextColor(PampApp_.getInstance().getResources().getColor(R.color.textColorBlack));
    }
}
