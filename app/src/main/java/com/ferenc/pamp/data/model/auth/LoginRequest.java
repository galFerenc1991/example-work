package com.ferenc.pamp.data.model.auth;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by
 * Ferenc on 2017.11.16..
 */

public class LoginRequest  implements Parcelable{

    public String email;
    public String password;

    public LoginRequest(String _email, String _password) {
        this.email = _email;
        this.password = _password;
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

    protected LoginRequest(Parcel in) {
        this.email = in.readString();
        this.password = in.readString();
    }

    public static final Parcelable.Creator<LoginRequest> CREATOR = new Parcelable.Creator<LoginRequest>() {
        @Override
        public LoginRequest createFromParcel(Parcel source) {
            return new LoginRequest(source);
        }

        @Override
        public LoginRequest[] newArray(int size) {
            return new LoginRequest[size];
        }
    };
}
