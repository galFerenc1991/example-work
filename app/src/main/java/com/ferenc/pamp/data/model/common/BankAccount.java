package com.ferenc.pamp.data.model.common;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by
 * Ferenc on 2018.01.05..
 */

public class BankAccount implements Parcelable {

    private String country;
    private String last4;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLast4() {
        return last4;
    }

    public void setLast4(String last4) {
        this.last4 = last4;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.country);
        dest.writeString(this.last4);
    }

    public BankAccount() {
    }

    protected BankAccount(Parcel in) {
        this.country = in.readString();
        this.last4 = in.readString();
    }

    public static final Parcelable.Creator<BankAccount> CREATOR = new Parcelable.Creator<BankAccount>() {
        @Override
        public BankAccount createFromParcel(Parcel source) {
            return new BankAccount(source);
        }

        @Override
        public BankAccount[] newArray(int size) {
            return new BankAccount[size];
        }
    };
}
