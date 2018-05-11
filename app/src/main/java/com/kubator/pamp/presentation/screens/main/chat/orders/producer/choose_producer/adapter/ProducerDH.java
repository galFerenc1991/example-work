package com.kubator.pamp.presentation.screens.main.chat.orders.producer.choose_producer.adapter;

import com.kubator.pamp.data.model.home.orders.Producer;
import com.michenko.simpleadapter.RecyclerDH;

/**
 * Created by shonliu on 1/2/18.
 */

public class ProducerDH implements RecyclerDH {

    private Producer mProducer;
    private boolean mSelected = false;


    public ProducerDH(Producer _producer, boolean _selected) {
        mProducer = _producer;
        mSelected = _selected;
    }

    boolean isSelected() {
        return mSelected;
    }

    public void setPosition(boolean _selected) {
        mSelected = _selected;
    }

    public Producer getProducer() {
        return mProducer;
    }

}
