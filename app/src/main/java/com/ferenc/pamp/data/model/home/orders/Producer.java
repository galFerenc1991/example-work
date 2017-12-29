package com.ferenc.pamp.data.model.home.orders;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by shonliu on 12/29/17.
 */

public class Producer implements Parcelable {
    public String name;
    public String email;
    public String phone;
    public String address;
    public String description;


    protected Producer(Parcel in) {
        name = in.readString();
        email = in.readString();
        phone = in.readString();
        address = in.readString();
        description = in.readString();
    }

    public static final Creator<Producer> CREATOR = new Creator<Producer>() {
        @Override
        public Producer createFromParcel(Parcel in) {
            return new Producer(in);
        }

        @Override
        public Producer[] newArray(int size) {
            return new Producer[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(email);
        dest.writeString(phone);
        dest.writeString(address);
        dest.writeString(description);
    }
}
