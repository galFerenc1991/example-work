package com.ferenc.pamp.presentation.screens.main.chat.orders.producer.choose_producer.adapter;

import com.michenko.simpleadapter.RecyclerDH;

/**
 * Created by shonliu on 1/2/18.
 */

public class ProducerDH implements RecyclerDH {

    private String mProducerName;
    private String mProducerId;
    private boolean mSelected = false;


    public ProducerDH(String _producerId, String _producerName) {
        mProducerId = _producerId;
        mProducerName = _producerName;
    }

    public ProducerDH(String _producerId, String _producerName, boolean _selected) {
        mProducerId = _producerId;
        mProducerName = _producerName;
        mSelected = _selected;
    }

    public String getProducer() {
        return mProducerName;
    }

    public String getProducerId() {
        return mProducerId;
    }

    public boolean isSelected() {
        return mSelected;
    }

    public void setPosition(boolean _selected) {
        mSelected = _selected;
    }

}
