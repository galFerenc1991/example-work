package com.kubator.pamp.data.model.home.good_deal;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by
 * Ferenc on 2017.12.01..
 */

public class GoodDealRequest implements Parcelable {

    private String id;
    private String product;
    private String description;
    private double price;
    private String unit;
    private double quantity;
    private long closingDate;
    private long deliveryStartDate;
    private long deliveryEndDate;
    private String deliveryAddress;
    public List<String> contacts;

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public long getClosingDate() {
        return closingDate;
    }

    public void setClosingDate(long closingDate) {
        this.closingDate = closingDate;
    }

    public long getDeliveryStartDate() {
        return deliveryStartDate;
    }

    public void setDeliveryStartDate(long deliveryStartDate) {
        this.deliveryStartDate = deliveryStartDate;
    }

    public long getDeliveryEndDate() {
        return deliveryEndDate;
    }

    public void setDeliveryEndDate(long deliveryEndDate) {
        this.deliveryEndDate = deliveryEndDate;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public List<String> getContacts() {
        return contacts;
    }

    public void setContacts(List<String> contacts) {
        this.contacts = contacts;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public static class Builder {

        protected GoodDealRequest goodDealRequest;

        public Builder() {
            goodDealRequest = new GoodDealRequest();
        }

        public Builder setProduct(String _product) {
            goodDealRequest.product = _product;
            return this;
        }

        public Builder setDescription(String _description) {
            goodDealRequest.description = _description;
            return this;
        }

        public Builder setUnit(String _unit) {
            goodDealRequest.unit = _unit;
            return this;
        }

        public Builder setPrice(double _price) {
            goodDealRequest.price = _price;
            return this;
        }

        public Builder setQuantity(double _quantity) {
            goodDealRequest.quantity = _quantity;
            return this;
        }

        public Builder setClosingDate(long _closingDate) {
            goodDealRequest.closingDate = _closingDate;
            return this;
        }

        public Builder setDeliveryAddress(String _deliveryAddress) {
            goodDealRequest.deliveryAddress = _deliveryAddress;
            return this;
        }

        public Builder setDeliveryStartDate(long _deliveryStartDate) {
            goodDealRequest.deliveryStartDate = _deliveryStartDate;
            return this;
        }

        public Builder setDeliveryEndDate(long _deliveryEndDate) {
            goodDealRequest.deliveryEndDate = _deliveryEndDate;
            return this;
        }
        public Builder setID(String _id) {
            goodDealRequest.id = _id;
            return this;
        }

        public GoodDealRequest build() {
            return goodDealRequest;
        }

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.product);
        dest.writeString(this.description);
        dest.writeDouble(this.price);
        dest.writeString(this.unit);
        dest.writeDouble(this.quantity);
        dest.writeLong(this.closingDate);
        dest.writeLong(this.deliveryStartDate);
        dest.writeLong(this.deliveryEndDate);
        dest.writeString(this.deliveryAddress);
        dest.writeStringList(this.contacts);
    }

    public GoodDealRequest() {
    }

    protected GoodDealRequest(Parcel in) {
        this.id = in.readString();
        this.product = in.readString();
        this.description = in.readString();
        this.price = in.readDouble();
        this.unit = in.readString();
        this.quantity = in.readDouble();
        this.closingDate = in.readLong();
        this.deliveryStartDate = in.readLong();
        this.deliveryEndDate = in.readLong();
        this.deliveryAddress = in.readString();
        this.contacts = in.createStringArrayList();
    }

    public static final Creator<GoodDealRequest> CREATOR = new Creator<GoodDealRequest>() {
        @Override
        public GoodDealRequest createFromParcel(Parcel source) {
            return new GoodDealRequest(source);
        }

        @Override
        public GoodDealRequest[] newArray(int size) {
            return new GoodDealRequest[size];
        }
    };
}
