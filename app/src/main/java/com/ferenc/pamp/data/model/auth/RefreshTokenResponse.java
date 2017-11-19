package com.ferenc.pamp.data.model.auth;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by
 * Ferenc on 2017.11.09..
 */

public class RefreshTokenResponse implements Parcelable {

    public String token;
    @SerializedName("Expired")
    public String expired;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.token);
        dest.writeString(this.expired);
    }

    public RefreshTokenResponse() {
    }

    protected RefreshTokenResponse(Parcel in) {
        this.token = in.readString();
        this.expired = in.readString();
    }

    public static final Parcelable.Creator<RefreshTokenResponse> CREATOR = new Parcelable.Creator<RefreshTokenResponse>() {
        @Override
        public RefreshTokenResponse createFromParcel(Parcel source) {
            return new RefreshTokenResponse(source);
        }

        @Override
        public RefreshTokenResponse[] newArray(int size) {
            return new RefreshTokenResponse[size];
        }
    };
}

