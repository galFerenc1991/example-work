package com.ferenc.pamp.data.model.auth;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by
 * Ferenc on 2017.11.16..
 */

public class SignUpRequest implements Parcelable {

    private String firstName;
    private String lastName;
    private String email;
    private String country;
    private String password;
    private String confirmPassword;

    public SignUpRequest(String _firstName, String _lastName, String _email, String _country, String _password, String _confirmPassword) {
        this.firstName = _firstName;
        this.lastName = _lastName;
        this.email = _email;
        this.country = _country;
        this.password = _password;
        this.confirmPassword = _confirmPassword;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.firstName);
        dest.writeString(this.lastName);
        dest.writeString(this.email);
        dest.writeString(this.country);
        dest.writeString(this.password);
        dest.writeString(this.confirmPassword);
    }

    protected SignUpRequest(Parcel in) {
        this.firstName = in.readString();
        this.lastName = in.readString();
        this.email = in.readString();
        this.country = in.readString();
        this.password = in.readString();
        this.confirmPassword = in.readString();
    }

    public static final Parcelable.Creator<SignUpRequest> CREATOR = new Parcelable.Creator<SignUpRequest>() {
        @Override
        public SignUpRequest createFromParcel(Parcel source) {
            return new SignUpRequest(source);
        }

        @Override
        public SignUpRequest[] newArray(int size) {
            return new SignUpRequest[size];
        }
    };


}
