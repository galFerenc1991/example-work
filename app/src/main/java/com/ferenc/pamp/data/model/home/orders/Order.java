package com.ferenc.pamp.data.model.home.orders;

import android.os.Parcel;
import android.os.Parcelable;

import com.ferenc.pamp.data.model.common.User;
import com.ferenc.pamp.data.model.home.good_deal.GoodDealResponse;
import com.google.gson.annotations.SerializedName;

/**
 * Created by
 * Ferenc on 2017.12.20..
 */

public class Order implements Parcelable{
    @SerializedName("_id")
    private String id;
    private String state;
    private double price;
    private boolean delivered;
    private int quantity;
    private User user;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
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
        dest.writeInt(this.quantity);
        dest.writeParcelable(this.user, flags);
        dest.writeParcelable(this.deal, flags);
    }

    public Order() {
    }

    public Order(double price, int quantity) {
        this.price = price;
        this.quantity = quantity;
    }

    protected Order(Parcel in) {
        this.id = in.readString();
        this.state = in.readString();
        this.price = in.readDouble();
        this.delivered = in.readByte() != 0;
        this.quantity = in.readInt();
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
