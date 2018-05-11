package com.kubator.pamp.data.model.message;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.kubator.pamp.data.api.RestConst;

import com.kubator.pamp.data.model.common.User;

import java.io.File;

/**
 * Created by shonliu on 12/13/17.
 */

public class MessageResponse implements Parcelable {


    public String _id;
    public String type;
    public String code;
    public String text;
    public long createdAt;
    public String content;
    public User user;
    public Description description;
    public File localImage;

    public MessageResponse(Parcel in) {
        _id = in.readString();
        type = in.readString();
        code = in.readString();
        text = in.readString();
        createdAt = in.readInt();
        content = in.readString();
        user = in.readParcelable(User.class.getClassLoader());
    }

    public static final Creator<MessageResponse> CREATOR = new Creator<MessageResponse>() {
        @Override
        public MessageResponse createFromParcel(Parcel in) {
            return new MessageResponse(in);
        }

        @Override
        public MessageResponse[] newArray(int size) {
            return new MessageResponse[size];
        }
    };

    public MessageResponse() {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(_id);
        parcel.writeString(type);
        parcel.writeString(code);
        parcel.writeString(text);
        parcel.writeLong(createdAt);
        parcel.writeString(content);
        parcel.writeParcelable(user, i);
    }

    public String getContentUrl() {
        return RestConst.BASE_URL + "/" + content;
    }

    public boolean isContentPresent() {
        return !TextUtils.isEmpty(content) || localImage != null;
    }
}
