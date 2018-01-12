package com.ferenc.pamp.data.model.base;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by
 * Ferenc on 2018.01.10..
 */

public class GeneralMessageResponse implements Parcelable{

    private String message;

    public String getMessage() {
        return message;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.message);
    }

    public GeneralMessageResponse() {
    }

    protected GeneralMessageResponse(Parcel in) {
        this.message = in.readString();
    }

    public static final Creator<GeneralMessageResponse> CREATOR = new Creator<GeneralMessageResponse>() {
        @Override
        public GeneralMessageResponse createFromParcel(Parcel source) {
            return new GeneralMessageResponse(source);
        }

        @Override
        public GeneralMessageResponse[] newArray(int size) {
            return new GeneralMessageResponse[size];
        }
    };
}
