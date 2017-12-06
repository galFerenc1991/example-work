package com.ferenc.pamp.data.model.home.good_deal;

import java.util.List;

/**
 * Created by
 * Ferenc on 2017.12.01..
 */

public class GoodDealRequest {

    public String product;
    public String description;
    public int price;
    public String unit;
    public int quantity;
    public long closingDate;
    public long deliveryStartDate;
    public long deliveryEndDate;
    public String deliveryAddress;
    public List<String> contacts;
}
