package com.ferenc.pamp.data.model.home.good_deal;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by
 * Ferenc on 2017.12.01..
 */

public class GoodDealResponse implements Parcelable {

    public int price;
    public String product;
    public int quantity;
    public long closingDate;
    public String description;
    public long deliveryStartDate;
    public long deliveryEndDate;
    public String deliveryAddress;
    public String title;
    public int rank;
    public String unit;
    public String contributor;
    public List<String> recipients;
    public String state;
    @SerializedName("_id")
    public String id;

    public GoodDealResponse() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.price);
        dest.writeString(this.product);
        dest.writeInt(this.quantity);
        dest.writeLong(this.closingDate);
        dest.writeString(this.description);
        dest.writeLong(this.deliveryStartDate);
        dest.writeLong(this.deliveryEndDate);
        dest.writeString(this.deliveryAddress);
        dest.writeString(this.title);
        dest.writeInt(this.rank);
        dest.writeString(this.unit);
        dest.writeString(this.contributor);
        dest.writeStringList(this.recipients);
        dest.writeString(this.state);
        dest.writeString(this.id);
    }

    protected GoodDealResponse(Parcel in) {
        this.price = in.readInt();
        this.product = in.readString();
        this.quantity = in.readInt();
        this.closingDate = in.readLong();
        this.description = in.readString();
        this.deliveryStartDate = in.readLong();
        this.deliveryEndDate = in.readLong();
        this.deliveryAddress = in.readString();
        this.title = in.readString();
        this.rank = in.readInt();
        this.unit = in.readString();
        this.contributor = in.readString();
        this.recipients = in.createStringArrayList();
        this.state = in.readString();
        this.id = in.readString();
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
