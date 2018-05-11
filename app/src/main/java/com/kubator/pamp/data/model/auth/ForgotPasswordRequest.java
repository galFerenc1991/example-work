package com.kubator.pamp.data.model.auth;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by
 * Ferenc on 2017.11.17..
 */

public class ForgotPasswordRequest implements Parcelable {

    private String email;

    public ForgotPasswordRequest(String email) {
        this.email = email;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.email);
    }

    protected ForgotPasswordRequest(Parcel in) {
        this.email = in.readString();
    }

    public static final Creator<ForgotPasswordRequest> CREATOR = new Creator<ForgotPasswordRequest>() {
        @Override
        public ForgotPasswordRequest createFromParcel(Parcel source) {
            return new ForgotPasswordRequest(source);
        }

        @Override
        public ForgotPasswordRequest[] newArray(int size) {
            return new ForgotPasswordRequest[size];
        }
    };
}
