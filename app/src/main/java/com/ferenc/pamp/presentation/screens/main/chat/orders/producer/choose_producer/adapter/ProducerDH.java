package com.ferenc.pamp.presentation.screens.main.chat.orders.producer.choose_producer.adapter;

import com.michenko.simpleadapter.RecyclerDH;

/**
 * Created by shonliu on 1/2/18.
 */

public class ProducerDH implements RecyclerDH {

    private String mProducer;

    public ProducerDH(String _producerName) {
        mProducer = _producerName;
    }
    public String getProducer() {
        return mProducer;
    }
}
