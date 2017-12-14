package com.ferenc.pamp.data.model.common;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by
 * Ferenc on 2017.11.16..
 */

public class User implements Parcelable {
    @SerializedName("_id")
    private String id;
    @SerializedName("updatedAt")
    private String updatedTime;
    @SerializedName("createdAt")
    private String createdTime;
    private String email;
    private String country;
    private String lastName;
    private String firstName;
    private String token;
    private String avatar;
    private String type;

    public User() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.updatedTime);
        dest.writeString(this.createdTime);
        dest.writeString(this.email);
        dest.writeString(this.country);
        dest.writeString(this.lastName);
        dest.writeString(this.firstName);
        dest.writeString(this.token);
        dest.writeString(this.avatar);
        dest.writeString(this.type);
    }

    protected User(Parcel in) {
        this.id = in.readString();
        this.updatedTime = in.readString();
        this.createdTime = in.readString();
        this.email = in.readString();
        this.country = in.readString();
        this.lastName = in.readString();
        this.firstName = in.readString();
        this.token = in.readString();
        this.avatar = in.readString();
        this.type = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {this.id = id;}

    public String getUpdatedTime() {
        return updatedTime;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public String getEmail() {
        return email;
    }

    public String getCountry() {
        return country;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getToken() {
        return token;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getType() {
        return type;
    }
}
