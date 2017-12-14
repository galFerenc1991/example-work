package com.ferenc.pamp.data.model.base;

import android.os.Parcel;
import android.os.Parcelable;

import com.ferenc.pamp.data.model.common.User;

import java.util.ArrayList;

/**
 * Created by
 * Ferenc on 2017.12.06..
 */

public class ListResponse<T extends Parcelable> implements Parcelable {

    public ArrayList<T> data;
    public Meta meta;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (data == null || data.size() == 0)
            dest.writeInt(0);
        else {
            dest.writeInt(data.size());
            final Class<?> objectsType = data.get(0).getClass();
            dest.writeSerializable(objectsType);
            dest.writeList(data);
        }
        dest.writeParcelable(this.meta, flags);
    }

    public ListResponse() {
    }

    protected ListResponse(Parcel in) {
        this.meta = in.readParcelable(Meta.class.getClassLoader());

        int size = in.readInt();
        if (size == 0) {
            data = null;
        } else {
            Class<?> type = (Class<?>) in.readSerializable();
            data = new ArrayList<>(size);
            in.readList(data, type.getClassLoader());
        }
    }

    public static final Parcelable.Creator<ListResponse> CREATOR = new Parcelable.Creator<ListResponse>() {
        @Override
        public ListResponse createFromParcel(Parcel source) {
            return new ListResponse(source);
        }

        @Override
        public ListResponse[] newArray(int size) {
            return new ListResponse[size];
        }
    };
}

