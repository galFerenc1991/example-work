package com.ferenc.pamp.data.model.home.orders;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by shonliu on 12/29/17.
 */

public class PDFPreviewRequest implements Parcelable{

    public String id;
    public String dealId;
    public double quantity;

    protected PDFPreviewRequest(Parcel in) {
        id = in.readString();
        dealId = in.readString();
        quantity = in.readDouble();
    }

    public PDFPreviewRequest(String _id, String _dealId, double _quantity) {
        id = _id;
        dealId = _dealId;
        quantity = _quantity;
    }

    public static final Creator<PDFPreviewRequest> CREATOR = new Creator<PDFPreviewRequest>() {
        @Override
        public PDFPreviewRequest createFromParcel(Parcel in) {
            return new PDFPreviewRequest(in);
        }

        @Override
        public PDFPreviewRequest[] newArray(int size) {
            return new PDFPreviewRequest[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(dealId);
        parcel.writeDouble(quantity);
    }
}
