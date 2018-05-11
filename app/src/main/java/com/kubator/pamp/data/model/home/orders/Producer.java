package com.kubator.pamp.data.model.home.orders;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by shonliu on 12/29/17.
 */

public class Producer implements Parcelable {
    @SerializedName("_id")
    public String producerId;
    public String name;
    public String email;
    public String phone;
    public String address;
    public String description;

    public Producer(String _id, String _name, String _email, String _phone, String _address, String _description) {
        this.producerId = _id;
        this.name = _name;
        this.email = _email;
        this.phone = _phone;
        this.address = _address;
        this.description = _description;
    }

    public Producer(String _name, String _email, String _phone, String _address, String _description) {
        this.name = _name;
        this.email = _email;
        this.phone = _phone;
        this.address = _address;
        this.description = _description;
    }

    public Producer(String _name, String _producerId, String _producerEmail) {
        this.name = _name;
        this.producerId = _producerId;
        this.email = _producerEmail;
    }

    public Producer() {

    }

    protected Producer(Parcel in) {
        producerId = in.readString();
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
        dest.writeString(producerId);
        dest.writeString(name);
        dest.writeString(email);
        dest.writeString(phone);
        dest.writeString(address);
        dest.writeString(description);
    }


}
