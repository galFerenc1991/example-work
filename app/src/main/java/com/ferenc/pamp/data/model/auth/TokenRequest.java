package com.ferenc.pamp.data.model.auth;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by
 * Ferenc on 2017.11.16..
 */

public class TokenRequest implements Parcelable {

    private String token;

    public TokenRequest(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.token);
    }

    protected TokenRequest(Parcel in) {
        this.token = in.readString();
    }

    public static final Creator<TokenRequest> CREATOR = new Creator<TokenRequest>() {
        @Override
        public TokenRequest createFromParcel(Parcel source) {
            return new TokenRequest(source);
        }

        @Override
        public TokenRequest[] newArray(int size) {
            return new TokenRequest[size];
        }
    };
}
