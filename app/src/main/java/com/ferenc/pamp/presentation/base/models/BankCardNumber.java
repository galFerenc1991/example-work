package com.ferenc.pamp.presentation.base.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by
 * Ferenc on 2018.02.08..
 */

public class BankCardNumber implements Parcelable {

    private String first4;
    private String second4;
    private String third4;
    private String fourth4;

    public String getFirst4() {
        return first4;
    }

    public String getSecond4() {
        return second4;
    }

    public String getThird4() {
        return third4;
    }

    public String getFourth4() {
        return fourth4;
    }


    public static class Builder {
        protected BankCardNumber mBankCardNumber;

        public Builder() {
            mBankCardNumber = new BankCardNumber();
        }

        public Builder setFirst4(String _first4) {
            mBankCardNumber.first4 = _first4;
            return this;
        }

        public Builder setSecond4(String _second4) {
            mBankCardNumber.second4 = _second4;
            return this;
        }

        public Builder setThird4(String _third4) {
            mBankCardNumber.third4 = _third4;
            return this;
        }

        public Builder setFourth4(String _fourth4) {
            mBankCardNumber.fourth4 = _fourth4;
            return this;
        }

        public BankCardNumber build() {
            return mBankCardNumber;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.first4);
        dest.writeString(this.second4);
        dest.writeString(this.third4);
        dest.writeString(this.fourth4);
    }

    public BankCardNumber() {
    }

    protected BankCardNumber(Parcel in) {
        this.first4 = in.readString();
        this.second4 = in.readString();
        this.third4 = in.readString();
        this.fourth4 = in.readString();
    }

    public static final Creator<BankCardNumber> CREATOR = new Creator<BankCardNumber>() {
        @Override
        public BankCardNumber createFromParcel(Parcel source) {
            return new BankCardNumber(source);
        }

        @Override
        public BankCardNumber[] newArray(int size) {
            return new BankCardNumber[size];
        }
    };
}
