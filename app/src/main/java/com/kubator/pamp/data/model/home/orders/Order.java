package com.kubator.pamp.data.model.home.orders;

import android.os.Parcel;
import android.os.Parcelable;

import com.kubator.pamp.data.model.common.User;
import com.kubator.pamp.data.model.home.good_deal.GoodDealResponse;
import com.google.gson.annotations.SerializedName;

/**
 * Created by
 * Ferenc on 2017.12.20..
 */

public class Order implements Parcelable {
    @SerializedName("_id")
    private String id;
    private String state;
    private double price;
    private boolean delivered;
    private double quantity;
    private int rank;
    private User user;
    private long createdAt;
    private GoodDealResponse deal;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isDelivered() {
        return delivered;
    }

    public void setDelivered(boolean delivered) {
        this.delivered = delivered;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public GoodDealResponse getDeal() {
        return deal;
    }

    public int getRank() {
        return rank;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public Order() {
    }

    public Order(double price, double quantity) {
        this.price = price;
        this.quantity = quantity;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.state);
        dest.writeDouble(this.price);
        dest.writeByte(this.delivered ? (byte) 1 : (byte) 0);
        dest.writeDouble(this.quantity);
        dest.writeInt(this.rank);
        dest.writeParcelable(this.user, flags);
        dest.writeParcelable(this.deal, flags);
    }

    protected Order(Parcel in) {
        this.id = in.readString();
        this.state = in.readString();
        this.price = in.readDouble();
        this.delivered = in.readByte() != 0;
        this.quantity = in.readDouble();
        this.rank = in.readInt();
        this.user = in.readParcelable(User.class.getClassLoader());
        this.deal = in.readParcelable(GoodDealResponse.class.getClassLoader());
    }

    public static final Creator<Order> CREATOR = new Creator<Order>() {
        @Override
        public Order createFromParcel(Parcel source) {
            return new Order(source);
        }

        @Override
        public Order[] newArray(int size) {
            return new Order[size];
        }
    };
}
