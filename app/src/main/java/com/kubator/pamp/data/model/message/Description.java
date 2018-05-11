package com.kubator.pamp.data.model.message;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by shonliu on 12/15/17.
 */

public class Description implements Parcelable {
    public String firstName;
    public float quantity;
    public long deliveryStartDate;
    public long deliveryEndDate;
    public long closingDate;

    protected Description(Parcel in) {
        firstName = in.readString();
        quantity = in.readFloat();
        deliveryStartDate = in.readLong();
        deliveryEndDate = in.readLong();
        closingDate = in.readLong();
    }
    public Description(){

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(firstName);
        dest.writeFloat(quantity);
        dest.writeLong(deliveryStartDate);
        dest.writeLong(deliveryEndDate);
        dest.writeLong(closingDate);
    }

    public String getStringQuantity() {

        return quantity > 0.9 ? String.valueOf(Math.round(quantity)) : String.valueOf(quantity);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Description> CREATOR = new Creator<Description>() {
        @Override
        public Description createFromParcel(Parcel in) {
            return new Description(in);
        }

        @Override
        public Description[] newArray(int size) {
            return new Description[size];
        }
    };
}
