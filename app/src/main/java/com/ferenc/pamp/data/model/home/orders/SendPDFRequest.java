package com.ferenc.pamp.data.model.home.orders;

import java.io.Serializable;

/**
 * Created by shonliu on 1/29/18.
 */

public class SendPDFRequest implements Serializable {

    public String dealId;
    public String producerId;

    public SendPDFRequest(String dealId, String producerId) {
        this.dealId = dealId;
        this.producerId = producerId;
    }
}
