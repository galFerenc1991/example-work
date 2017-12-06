package com.ferenc.pamp.presentation.base.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by
 * Ferenc on 2017.11.22..
 */

public class GoodDeal implements Parcelable {

    private String name;
    private String description;
    private int price;
    private String priceDescription;
    private int quantity;
    private long closeDate;
    private String deliveryPlace;
    private long startDelivery;
    private long endDelivery;

    public GoodDeal() {
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getPriceDescription() {
        return priceDescription;
    }

    public String getDeliveryPlace() {
        return deliveryPlace;
    }

    public void setDeliveryPlace(String deliveryPlace) {
        this.deliveryPlace = deliveryPlace;
    }

    public void setPriceDescription(String priceDescription) {
        this.priceDescription = priceDescription;
    }

    public Long getCloseDate() {
        return closeDate;
    }

    public void setCloseDate(Long closeDate) {
        this.closeDate = closeDate;
    }

    public Long getStartDelivery() {
        return startDelivery;
    }

    public void setStartDelivery(Long startDelivery) {
        this.startDelivery = startDelivery;
    }

    public Long getEndDelivery() {
        return endDelivery;
    }

    public void setEndDelivery(Long endDelivery) {
        this.endDelivery = endDelivery;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.description);
        dest.writeInt(this.price);
        dest.writeString(this.priceDescription);
        dest.writeInt(this.quantity);
        dest.writeValue(this.closeDate);
        dest.writeString(this.deliveryPlace);
        dest.writeValue(this.startDelivery);
        dest.writeValue(this.endDelivery);
    }

    protected GoodDeal(Parcel in) {
        this.name = in.readString();
        this.description = in.readString();
        this.price = in.readInt();
        this.priceDescription = in.readString();
        this.quantity = in.readInt();
        this.closeDate = (Long) in.readValue(Long.class.getClassLoader());
        this.deliveryPlace = in.readString();
        this.startDelivery = (Long) in.readValue(Long.class.getClassLoader());
        this.endDelivery = (Long) in.readValue(Long.class.getClassLoader());
    }

    public static final Creator<GoodDeal> CREATOR = new Creator<GoodDeal>() {
        @Override
        public GoodDeal createFromParcel(Parcel source) {
            return new GoodDeal(source);
        }

        @Override
        public GoodDeal[] newArray(int size) {
            return new GoodDeal[size];
        }
    };
}
