package com.kubator.pamp.data.model.auth;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by
 * Ferenc on 2017.11.16..
 */

public class SignInRequest implements Parcelable{

    private String email;
    private String password;

    public SignInRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.email);
        dest.writeString(this.password);
    }

    protected SignInRequest(Parcel in) {
        this.email = in.readString();
        this.password = in.readString();
    }

    public static final Creator<SignInRequest> CREATOR = new Creator<SignInRequest>() {
        @Override
        public SignInRequest createFromParcel(Parcel source) {
            return new SignInRequest(source);
        }

        @Override
        public SignInRequest[] newArray(int size) {
            return new SignInRequest[size];
        }
    };
}
