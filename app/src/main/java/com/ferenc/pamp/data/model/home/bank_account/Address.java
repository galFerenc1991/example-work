package com.ferenc.pamp.data.model.home.bank_account;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by
 * Ferenc on 2018.02.02..
 */

public class Address implements Parcelable {
    private String country;
    private String city;
    private String line1;
    @SerializedName("postal_code")
    private String postalCode;

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public String getLine1() {
        return line1;
    }

    public String getPostalCode() {
        return postalCode;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.country);
        dest.writeString(this.city);
        dest.writeString(this.line1);
        dest.writeString(this.postalCode);
    }

    public Address() {
    }

    protected Address(Parcel in) {
        this.country = in.readString();
        this.city = in.readString();
        this.line1 = in.readString();
        this.postalCode = in.readString();
    }

    public static final Parcelable.Creator<Address> CREATOR = new Parcelable.Creator<Address>() {
        @Override
        public Address createFromParcel(Parcel source) {
            return new Address(source);
        }

        @Override
        public Address[] newArray(int size) {
            return new Address[size];
        }
    };

    public static class Builder {

        protected Address address;

        public Builder() {
            address = new Address();
        }

        public Builder setCountry(String _country) {
            address.country = _country;
            return this;
        }

        public Builder setCity(String _city) {
            address.city = _city;
            return this;
        }

        public Builder setStreet(String _street) {
            address.line1 = _street;
            return this;
        }


        public Builder setPostalCode(String _postalCode) {
            address.postalCode = _postalCode;
            return this;
        }

        public Address build() {
            return address;
        }

    }
}
