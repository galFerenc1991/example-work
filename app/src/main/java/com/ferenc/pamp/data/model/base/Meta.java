package com.ferenc.pamp.data.model.base;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by
 * Ferenc on 2017.12.06..
 */

public class Meta implements Parcelable {

    public int page;
    public int pages;
    public int limit;
    public int total;
    public boolean attention;


    public Meta() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.page);
        dest.writeInt(this.pages);
        dest.writeInt(this.limit);
        dest.writeInt(this.total);
        dest.writeByte(this.attention ? (byte) 1 : (byte) 0);
    }

    protected Meta(Parcel in) {
        this.page = in.readInt();
        this.pages = in.readInt();
        this.limit = in.readInt();
        this.total = in.readInt();
        this.attention = in.readByte() != 0;
    }

    public static final Creator<Meta> CREATOR = new Creator<Meta>() {
        @Override
        public Meta createFromParcel(Parcel source) {
            return new Meta(source);
        }

        @Override
        public Meta[] newArray(int size) {
            return new Meta[size];
        }
    };
}