package com.ferenc.pamp.data.model.home.orders;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by shonliu on 12/29/17.
 */

public class PDFPreviewResponse implements Parcelable {

    public String file;
    public String template;

    protected PDFPreviewResponse(Parcel in) {
        file = in.readString();
        template = in.readString();
    }

    public static final Creator<PDFPreviewResponse> CREATOR = new Creator<PDFPreviewResponse>() {
        @Override
        public PDFPreviewResponse createFromParcel(Parcel in) {
            return new PDFPreviewResponse(in);
        }

        @Override
        public PDFPreviewResponse[] newArray(int size) {
            return new PDFPreviewResponse[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(file);
        parcel.writeString(template);
    }
}
