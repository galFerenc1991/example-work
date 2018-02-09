package com.ferenc.pamp.data.model.home.bank_account;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by
 * Ferenc on 2018.02.02..
 */

public class BankAccountRequest implements Parcelable {
    private String token;
    private long dateOfBirthday;
    private Address address;

    public BankAccountRequest(String token, long dateOfBirthday, Address address) {
        this.token = token;
        this.dateOfBirthday = dateOfBirthday;
        this.address = address;
    }

    public BankAccountRequest() {
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setDateOfBirthday(long dateOfBirthday) {
        this.dateOfBirthday = dateOfBirthday;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.token);
        dest.writeLong(this.dateOfBirthday);
        dest.writeParcelable(this.address, flags);
    }

    protected BankAccountRequest(Parcel in) {
        this.token = in.readString();
        this.dateOfBirthday = in.readLong();
        this.address = in.readParcelable(Address.class.getClassLoader());
    }

    public static final Parcelable.Creator<BankAccountRequest> CREATOR = new Parcelable.Creator<BankAccountRequest>() {
        @Override
        public BankAccountRequest createFromParcel(Parcel source) {
            return new BankAccountRequest(source);
        }

        @Override
        public BankAccountRequest[] newArray(int size) {
            return new BankAccountRequest[size];
        }
    };

}
