package com.ferenc.pamp.data.model.common;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by
 * Ferenc on 2017.12.28..
 */

public class Card implements Parcelable {
    private String last4;
    private String brand;

    public String getLast4() {
        return last4;
    }

    public String getBrand() {
        return brand;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.last4);
        dest.writeString(this.brand);
    }

    public Card() {
    }

    protected Card(Parcel in) {
        this.last4 = in.readString();
        this.brand = in.readString();
    }

    public static final Creator<Card> CREATOR = new Creator<Card>() {
        @Override
        public Card createFromParcel(Parcel source) {
            return new Card(source);
        }

        @Override
        public Card[] newArray(int size) {
            return new Card[size];
        }
    };
}
