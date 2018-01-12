package com.ferenc.pamp.data.model.message;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by shonliu on 12/15/17.
 */

public class Description implements Parcelable {
    public String firstName;
    public int quantity;
    public long deliveryStartDate;
    public long deliveryEndDate;
    public long closingDate;

    protected Description(Parcel in) {
        firstName = in.readString();
        quantity = in.readInt();
        deliveryStartDate = in.readLong();
        deliveryEndDate = in.readLong();
        closingDate = in.readLong();
    }
    public Description(){

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(firstName);
        dest.writeInt(quantity);
        dest.writeLong(deliveryStartDate);
        dest.writeLong(deliveryEndDate);
        dest.writeLong(closingDate);
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
