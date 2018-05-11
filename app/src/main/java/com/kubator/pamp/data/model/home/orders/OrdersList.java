package com.kubator.pamp.data.model.home.orders;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by
 * Ferenc on 2018.01.03..
 */

public class OrdersList implements Parcelable {

    private List<String> orders;

    public void setOrders(List<String> orders) {
        this.orders = orders;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(this.orders);
    }

    public OrdersList(List<String> _orders) {
        orders = _orders;
    }

    protected OrdersList(Parcel in) {
        this.orders = in.createStringArrayList();
    }

    public static final Parcelable.Creator<OrdersList> CREATOR = new Parcelable.Creator<OrdersList>() {
        @Override
        public OrdersList createFromParcel(Parcel source) {
            return new OrdersList(source);
        }

        @Override
        public OrdersList[] newArray(int size) {
            return new OrdersList[size];
        }
    };
}
