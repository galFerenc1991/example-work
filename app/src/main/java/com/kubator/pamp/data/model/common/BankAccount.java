package com.kubator.pamp.data.model.common;

import android.os.Parcel;
import android.os.Parcelable;

import com.kubator.pamp.data.model.home.bank_account.Address;

/**
 * Created by
 * Ferenc on 2018.01.05..
 */

public class BankAccount implements Parcelable {

    private String country;
    private String last4;
    private long dateOfBirthday;
    private Address address;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLast4() {
        return last4;
    }

    public long getDateOfBirthday() {
        return dateOfBirthday;
    }

    public Address getAddress() {
        return address;
    }

    public void setLast4(String last4) {
        this.last4 = last4;
    }


    public BankAccount() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.country);
        dest.writeString(this.last4);
        dest.writeLong(this.dateOfBirthday);
        dest.writeParcelable(this.address, flags);
    }

    protected BankAccount(Parcel in) {
        this.country = in.readString();
        this.last4 = in.readString();
        this.dateOfBirthday = in.readLong();
        this.address = in.readParcelable(Address.class.getClassLoader());
    }

    public static final Creator<BankAccount> CREATOR = new Creator<BankAccount>() {
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
