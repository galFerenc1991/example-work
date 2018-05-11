package com.kubator.pamp.data.model.common;

import android.os.Parcel;
import android.os.Parcelable;

import com.kubator.pamp.data.api.RestConst;
import com.google.gson.annotations.SerializedName;

/**
 * Created by
 * Ferenc on 2017.12.06..
 */

public class Contributor implements Parcelable{

    public String avatar;
    public String lastName;
    public String firstName;
    @SerializedName("_id")
    public String id;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.avatar);
        dest.writeString(this.lastName);
        dest.writeString(this.firstName);
        dest.writeString(this.id);
    }

    public Contributor() {
    }

    public String getAvatar() {
        return RestConst.BASE_URL + "/" + avatar;
    }

    protected Contributor(Parcel in) {
        this.avatar = in.readString();
        this.lastName = in.readString();
        this.firstName = in.readString();
        this.id = in.readString();
    }

    public static final Creator<Contributor> CREATOR = new Creator<Contributor>() {
        @Override
        public Contributor createFromParcel(Parcel source) {
            return new Contributor(source);
        }

        @Override
        public Contributor[] newArray(int size) {
            return new Contributor[size];
        }
    };
}
