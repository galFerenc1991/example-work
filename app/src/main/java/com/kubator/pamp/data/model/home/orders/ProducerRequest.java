package com.kubator.pamp.data.model.home.orders;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by shonliu on 1/3/18.
 */

public class ProducerRequest implements Parcelable{
    public String name;
    public String email;
    public String phone;
    public String address;
    public String description;

    protected ProducerRequest(Parcel in) {
        name = in.readString();
        email = in.readString();
        phone = in.readString();
        address = in.readString();
        description = in.readString();
    }

    public static final Creator<ProducerRequest> CREATOR = new Creator<ProducerRequest>() {
        @Override
        public ProducerRequest createFromParcel(Parcel in) {
            return new ProducerRequest(in);
        }

        @Override
        public ProducerRequest[] newArray(int size) {
            return new ProducerRequest[size];
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
