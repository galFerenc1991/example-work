package com.ferenc.pamp.presentation.screens.main.chat.orders.producer.choose_producer.adapter;

import com.michenko.simpleadapter.RecyclerDH;

/**
 * Created by shonliu on 1/2/18.
 */

public class ProducerDH implements RecyclerDH {

    private String mProducerName;
    private String mProducerId;
    private String mProducerEmail;
    private boolean mSelected = false;


    public ProducerDH(String _producerId, String _producerName, String _producerEmail) {
        mProducerId = _producerId;
        mProducerName = _producerName;
        mProducerEmail = _producerEmail;
    }

    public ProducerDH(String _producerId, String _producerName, String _producerEmail, boolean _selected) {
        mProducerId = _producerId;
        mProducerName = _producerName;
        mProducerEmail = _producerEmail;
        mSelected = _selected;
    }

    public String getProducer() {
        return mProducerName;
    }

    public String getProducerId() {
        return mProducerId;
    }

    public String getProducerEmail() {return mProducerEmail;}

    public boolean isSelected() {
        return mSelected;
    }

    public void setPosition(boolean _selected) {
        mSelected = _selected;
    }

}
