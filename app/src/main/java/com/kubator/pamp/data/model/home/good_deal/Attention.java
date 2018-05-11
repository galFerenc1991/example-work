package com.kubator.pamp.data.model.home.good_deal;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by
 * Ferenc on 2018.01.29..
 */

public class Attention implements Parcelable {
    private String firstName;
    private long deliveryStartDate;
    private long deliveryEndDate;

    public String getFirstName() {
        return firstName;
    }

    public long getDeliveryStartDate() {
        return deliveryStartDate;
    }

    public long getDeliveryEndDate() {
        return deliveryEndDate;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.firstName);
        dest.writeLong(this.deliveryStartDate);
        dest.writeLong(this.deliveryEndDate);
    }

    public Attention() {
    }

    protected Attention(Parcel in) {
        this.firstName = in.readString();
        this.deliveryStartDate = in.readLong();
        this.deliveryEndDate = in.readLong();
    }

    public static final Creator<Attention> CREATOR = new Creator<Attention>() {
        @Override
        public Attention createFromParcel(Parcel source) {
            return new Attention(source);
        }

        @Override
        public Attention[] newArray(int size) {
            return new Attention[size];
        }
    };
}
