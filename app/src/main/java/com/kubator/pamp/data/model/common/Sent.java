package com.kubator.pamp.data.model.common;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by shonliu on 1/29/18.
 */

public class Sent implements Parcelable {

    private long sentAt;
    private String name;

    protected Sent(Parcel in) {
        sentAt = in.readLong();
        name = in.readString();
    }

    public static final Creator<Sent> CREATOR = new Creator<Sent>() {
        @Override
        public Sent createFromParcel(Parcel in) {
            return new Sent(in);
        }

        @Override
        public Sent[] newArray(int size) {
            return new Sent[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(sentAt);
        dest.writeString(name);
    }

    public long getSentAt(){
        return sentAt;
    }

    public String getName() {
        return name;
    }

}

