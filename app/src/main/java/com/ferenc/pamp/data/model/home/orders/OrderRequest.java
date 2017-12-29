package com.ferenc.pamp.data.model.home.orders;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by
 * Ferenc on 2017.12.22..
 */

public class OrderRequest implements Parcelable {

    private String dealId;
    private int quantity;
    private boolean cardSave;
    private String cardToken;

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

        public Builder setCardSave(boolean _cardSave) {
            orderRequest.cardSave = _cardSave;
            return this;
        }

        public Builder setCardToken(String _cardToken) {
            orderRequest.cardToken = _cardToken;
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
        dest.writeByte(this.cardSave ? (byte) 1 : (byte) 0);
        dest.writeString(this.cardToken);
    }

    public OrderRequest() {
    }

    protected OrderRequest(Parcel in) {
        this.dealId = in.readString();
        this.quantity = in.readInt();
        this.cardSave = in.readByte() != 0;
        this.cardToken = in.readString();
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
