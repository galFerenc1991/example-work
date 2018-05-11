package com.kubator.pamp.data.model.home.orders;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by
 * Ferenc on 2018.01.30..
 */

public class ChangeOrderDeliveryStateRequest implements Parcelable {

    private String dealId;
    private boolean delivered;

    public void setDealId(String dealId) {
        this.dealId = dealId;
    }

    public void setDelivered(boolean delivered) {
        this.delivered = delivered;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.dealId);
        dest.writeByte(this.delivered ? (byte) 1 : (byte) 0);
    }

    public ChangeOrderDeliveryStateRequest() {
    }

    protected ChangeOrderDeliveryStateRequest(Parcel in) {
        this.dealId = in.readString();
        this.delivered = in.readByte() != 0;
    }

    public static final Creator<ChangeOrderDeliveryStateRequest> CREATOR = new Creator<ChangeOrderDeliveryStateRequest>() {
        @Override
        public ChangeOrderDeliveryStateRequest createFromParcel(Parcel source) {
            return new ChangeOrderDeliveryStateRequest(source);
        }

        @Override
        public ChangeOrderDeliveryStateRequest[] newArray(int size) {
            return new ChangeOrderDeliveryStateRequest[size];
        }
    };
}
