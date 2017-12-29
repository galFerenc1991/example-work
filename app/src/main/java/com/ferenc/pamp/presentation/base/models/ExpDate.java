package com.ferenc.pamp.presentation.base.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by
 * Ferenc on 2017.12.28..
 */

public class ExpDate implements Parcelable {
    private int cardExpMonth;
    private int cardExpYear;

    public ExpDate(int cardExpMonth, int getCardYear) {
        this.cardExpMonth = cardExpMonth;
        this.cardExpYear = getCardYear;
    }

    public int getCardExpMonth() {
        return cardExpMonth;
    }

    public int getCardExpYear() {
        return cardExpYear;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.cardExpMonth);
        dest.writeInt(this.cardExpYear);
    }

    protected ExpDate(Parcel in) {
        this.cardExpMonth = in.readInt();
        this.cardExpYear = in.readInt();
    }

    public static final Creator<ExpDate> CREATOR = new Creator<ExpDate>() {
        @Override
        public ExpDate createFromParcel(Parcel source) {
            return new ExpDate(source);
        }

        @Override
        public ExpDate[] newArray(int size) {
            return new ExpDate[size];
        }
    };
}
