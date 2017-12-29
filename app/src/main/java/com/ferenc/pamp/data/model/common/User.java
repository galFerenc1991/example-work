package com.ferenc.pamp.data.model.common;

import android.os.Parcel;
import android.os.Parcelable;

import com.ferenc.pamp.data.api.RestConst;
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
    private Card card;

    public User() {
    }


    public String getId() {
        return id;
    }


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

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getToken() {
        return token;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getAvatarUrl() {
        return RestConst.BASE_URL + "/" + avatar;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public void setId(String _id) {
        this.id = _id;
    }

    public void setFirstName(String _firstName) {
        this.firstName = _firstName;
    }

    public void setLastName(String _lastName) {
        this.lastName = _lastName;
    }

    public void setAvatar(String _avatar) {
        this.avatar = _avatar;
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
        dest.writeParcelable(this.card, flags);
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
        this.card = in.readParcelable(Card.class.getClassLoader());
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
}
