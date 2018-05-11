package com.kubator.pamp.data.model.common;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.File;

/**
 * Created by
 * Ferenc on 2017.12.13..
 */

public class UserUpdateRequest implements Parcelable{
    private String firstName;
    private String lastname;
    private String country;
    private File avatar;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.firstName);
        dest.writeString(this.lastname);
        dest.writeString(this.country);
        dest.writeSerializable(this.avatar);
    }

    public UserUpdateRequest() {
    }

    public UserUpdateRequest(String firstName, String lastname, String country, File avatar) {
        this.firstName = firstName;
        this.lastname = lastname;
        this.country = country;
        this.avatar = avatar;
    }

    protected UserUpdateRequest(Parcel in) {
        this.firstName = in.readString();
        this.lastname = in.readString();
        this.country = in.readString();
        this.avatar = (File) in.readSerializable();
    }

    public static final Creator<UserUpdateRequest> CREATOR = new Creator<UserUpdateRequest>() {
        @Override
        public UserUpdateRequest createFromParcel(Parcel source) {
            return new UserUpdateRequest(source);
        }

        @Override
        public UserUpdateRequest[] newArray(int size) {
            return new UserUpdateRequest[size];
        }
    };
}
