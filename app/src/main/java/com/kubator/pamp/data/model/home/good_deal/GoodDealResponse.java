package com.kubator.pamp.data.model.home.good_deal;

import android.os.Parcel;
import android.os.Parcelable;

import com.kubator.pamp.data.model.common.Sent;

import com.kubator.pamp.data.model.common.User;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by
 * Ferenc on 2017.12.01..
 */

public class GoodDealResponse implements Parcelable {

    public double price;
    public String product;
    public double quantity;
    public long closingDate;
    public String description;
    public long deliveryStartDate;
    public long deliveryEndDate;
    public String deliveryAddress;
    public String title;
    public long createdAt;
    public boolean isResend;
    public boolean hasOrders;
    public int rank;
    public String unit;
    @SerializedName("contributor")
    public User owner;
    public Sent sent;
    public List<User> recipients;
    private Attention attention;
    public String state;
    @SerializedName("_id")
    public String id;
    public String originalTitle;
    public boolean reviewed;

    public Attention getAttention() {
        return attention;
    }

    public GoodDealResponse() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.price);
        dest.writeString(this.product);
        dest.writeDouble(this.quantity);
        dest.writeLong(this.closingDate);
        dest.writeString(this.description);
        dest.writeLong(this.deliveryStartDate);
        dest.writeLong(this.deliveryEndDate);
        dest.writeString(this.deliveryAddress);
        dest.writeString(this.title);
        dest.writeLong(this.createdAt);
        dest.writeByte(this.isResend ? (byte) 1 : (byte) 0);
        dest.writeByte(this.hasOrders ? (byte) 1 : (byte) 0);
        dest.writeInt(this.rank);
        dest.writeString(this.unit);
        dest.writeParcelable(this.owner, flags);
        dest.writeParcelable(this.sent, flags);
        dest.writeTypedList(this.recipients);
        dest.writeParcelable(this.attention, flags);
        dest.writeString(this.state);
        dest.writeString(this.id);
        dest.writeString(this.originalTitle);
        dest.writeByte(this.reviewed ? (byte) 1 : (byte) 0);
    }

    protected GoodDealResponse(Parcel in) {
        this.price = in.readDouble();
        this.product = in.readString();
        this.quantity = in.readDouble();
        this.closingDate = in.readLong();
        this.description = in.readString();
        this.deliveryStartDate = in.readLong();
        this.deliveryEndDate = in.readLong();
        this.deliveryAddress = in.readString();
        this.title = in.readString();
        this.createdAt = in.readLong();
        this.isResend = in.readByte() != 0;
        this.hasOrders = in.readByte() != 0;
        this.rank = in.readInt();
        this.unit = in.readString();
        this.owner = in.readParcelable(User.class.getClassLoader());
        this.sent = in.readParcelable(Sent.class.getClassLoader());
        this.recipients = in.createTypedArrayList(User.CREATOR);
        this.attention = in.readParcelable(Attention.class.getClassLoader());
        this.state = in.readString();
        this.id = in.readString();
        this.originalTitle = in.readString();
        this.reviewed = in.readByte() != 0;
    }

    public static final Creator<GoodDealResponse> CREATOR = new Creator<GoodDealResponse>() {
        @Override
        public GoodDealResponse createFromParcel(Parcel source) {
            return new GoodDealResponse(source);
        }

        @Override
        public GoodDealResponse[] newArray(int size) {
            return new GoodDealResponse[size];
        }
    };
}
