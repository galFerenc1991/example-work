package com.kubator.pamp.data.model.auth;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by
 * Ferenc on 2017.11.20..
 */

public class CountryList implements Parcelable{

    private String[] countries;

    public CountryList() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(this.countries);
    }

    protected CountryList(Parcel in) {
        this.countries = in.createStringArray();
    }

    public static final Creator<CountryList> CREATOR = new Creator<CountryList>() {
        @Override
        public CountryList createFromParcel(Parcel source) {
            return new CountryList(source);
        }

        @Override
        public CountryList[] newArray(int size) {
            return new CountryList[size];
        }
    };

    public String[] getCountry() {
        return countries;
    }
}
