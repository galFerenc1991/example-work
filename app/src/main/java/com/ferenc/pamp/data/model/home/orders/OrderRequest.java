package com.ferenc.pamp.data.model.home.orders;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by
 * Ferenc on 2017.12.22..
 */

public class OrderRequest implements Parcelable{

    private String dealId;
    private int quantity;

    public static class Builder {

        protected OrderRequest orderRequest;

        public Builder() {
            orderRequest = new OrderRequest();
        }

        public Builder setDealId(String _dealId) {
            orderRequest.dealId = _dealId;
            return this;
        }

        public Builder setQuantity(int _quantity) {
            orderRequest.quantity = _quantity;
            return this;
        }

        public OrderRequest build() {
            return orderRequest;
        }

    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.dealId);
        dest.writeInt(this.quantity);
    }

    public OrderRequest() {
    }

    protected OrderRequest(Parcel in) {
        this.dealId = in.readString();
        this.quantity = in.readInt();
    }

    public static final Creator<OrderRequest> CREATOR = new Creator<OrderRequest>() {
        @Override
        public OrderRequest createFromParcel(Parcel source) {
            return new OrderRequest(source);
        }

        @Override
        public OrderRequest[] newArray(int size) {
            return new OrderRequest[size];
        }
    };
}
