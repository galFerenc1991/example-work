package com.ferenc.pamp.domain;

import android.util.Log;

import com.ferenc.pamp.data.api.RestConst;
import com.ferenc.pamp.data.model.common.User;
import com.ferenc.pamp.data.model.message.Description;
import com.ferenc.pamp.data.model.message.MessageResponse;
import com.ferenc.pamp.presentation.screens.main.chat.messenger.MessengerContract;
import com.ferenc.pamp.presentation.utils.SharedPrefManager_;
import com.jakewharton.rxrelay2.BehaviorRelay;
import com.jakewharton.rxrelay2.PublishRelay;
import com.jakewharton.rxrelay2.Relay;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import io.reactivex.Observable;
import io.socket.client.IO;
import io.socket.client.Socket;

/**
 * Created by shonliu on 12/19/17.
 */
@EBean(scope = EBean.Scope.Singleton)
public class SocketRepository implements MessengerContract.SocketModel {

    private Socket mSocket;

    private String TAG = "SocketIO";
    
    private String emitJoinRoom = "join";
    private String emitLeaveRoom = "leave";

    private String valContent = "content";
    private String valUser = "user";
    private String valId = "_id";
    private String valFirstName = "firstName";
    private String valLastName = "lastName";
    private String valAvatar = "avatar";
    private String valType = "type";
    private String valText = "text";
    private String valCreatedAt = "createdAt";
    private String valToken = "token";
    private String valDealID = "dealId";
    private String valCode = "code";
    private String valDescription = "description";
    private String valQuantity = "quantity";
    private String valDeliveryEndDate = "deliveryEndDate";
    private String valDeliveryStartDate = "deliveryStartDate";
    private String valClosingDate = "closingDate";
    private String mUserToken;

    @Pref
    protected SharedPrefManager_ mSharedPrefManager;

    @AfterInject
    void initSocket() {
        try {
            mSocket = IO.socket(RestConst.BASE_URL);
            Log.d(TAG, "Initialized");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

    }

    @Override
    public Observable<Void> connectSocket(String _roomID) {
        mUserToken = mSharedPrefManager.getAccessToken().get().replaceAll("Bearer ","");
        if (mSocket.connected())
            return connectSocketVoidRelay;

        mSocket.connect();
        mSocket.on(Socket.EVENT_CONNECT, args -> {
            Log.d(TAG, "Connected");
            joinRoom(mUserToken, _roomID);
        }).on(Socket.EVENT_MESSAGE, args -> {
            JSONObject data = (JSONObject) args[0];

            try {
                MessageResponse messageResponse = new MessageResponse();
                User user = new User();
                Description description = new Description();


                if (!data.getString(valType).equals("AUTO")) {
                    user.setId(data.getJSONObject(valUser).getString(valId));
                    user.setFirstName(data.getJSONObject(valUser).getString(valFirstName));
                    user.setLastName(data.getJSONObject(valUser).getString(valLastName));
                    user.setAvatar(data.getJSONObject(valUser).getString(valAvatar));

                    messageResponse.text = data.has(valText) ? data.getString(valText) : "";
                    messageResponse.user = user;
                }
                messageResponse.content = data.has(valContent) ? data.getString(valContent) : "";
                messageResponse.code = data.has(valCode) ? data.getString(valCode) : null;
                messageResponse._id = data.getString(valId);
                messageResponse.type = data.getString(valType);
                messageResponse.createdAt = data.getLong(valCreatedAt);


                description.quantity = data.has(valDescription) ? (float) (data.getJSONObject(valDescription).has(valQuantity) ? data.getJSONObject(valDescription).getDouble(valQuantity) : 0) : 0;
                description.firstName = (data.has(valDescription) ? data.getJSONObject(valDescription).has(valFirstName) ? data.getJSONObject(valDescription).getString(valFirstName) : "" : "");
                description.deliveryEndDate = data.has(valDescription) ? data.getJSONObject(valDescription).has(valDeliveryEndDate) ? data.getJSONObject(valDescription).getLong(valDeliveryEndDate) : 0 : 0;
                description.deliveryStartDate = data.has(valDescription) ? data.getJSONObject(valDescription).has(valDeliveryStartDate) ? data.getJSONObject(valDescription).getLong(valDeliveryStartDate) : 0 : 0;
                description.closingDate = data.has(valDescription) ? data.getJSONObject(valDescription).has(valClosingDate) ? data.getJSONObject(valDescription).getLong(valClosingDate) : 0 : 0;

                messageResponse.description = description;

                Log.d(TAG, "New message: " + data.toString());

                getNewMessage.accept(messageResponse);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        return connectSocketVoidRelay;
    }

    @Override
    public Observable<MessageResponse> getNewMessage() {

        return getNewMessage;
    }

    @Override
    public Observable<Void> sendMessage(String _dealId, String _messageText) {

        if (mSocket.connected()) {
            JSONObject obj = new JSONObject();
            try {
                obj.put(valToken, mUserToken);
                obj.put(valDealID, _dealId);
                obj.put(valText, _messageText);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            mSocket.emit(Socket.EVENT_MESSAGE, obj);
           Log.d(TAG, "Emitting: send message" + obj.toString());

        } else Log.d(TAG, "Emitting error, not connected to socket");

        return voidRelay;
    }

    @Override
    public Observable<Void> sendImage(String _dealId, String _imageBase64) {

        if (mSocket.connected()) {

            JSONObject obj = new JSONObject();
            try {
                obj.put(valToken, mUserToken);
                obj.put(valDealID, _dealId);
                obj.put(valContent, _imageBase64);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            mSocket.emit(Socket.EVENT_MESSAGE, obj);
            Log.d(TAG, "Emitting: send image" + obj.toString());

        } else Log.d(TAG, "Emitting error, not connected to socket");

        return voidRelay;
    }


    private void joinRoom(String _userToken, String _roomID) {

        JSONObject obj = new JSONObject();
        try {
            obj.put(valToken, _userToken);
            obj.put(valDealID, _roomID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mSocket.emit(emitJoinRoom, obj);
        Log.d(TAG, "Emitting: join to room " + obj.toString());
    }


    @Override
    public Observable<Void> disconnectSocket() {
        if (mSocket !=null) {
            mSocket.emit(emitLeaveRoom);
            mSocket.off(Socket.EVENT_CONNECT);
            mSocket.off(Socket.EVENT_MESSAGE);
            mSocket.disconnect();

            Log.d(TAG, "Emitting: leave room");
            Log.d(TAG, "Disconnect");
        }
        return voidRelay;
    }

    private Relay<MessageResponse> getNewMessage = PublishRelay.create();

    private Relay<Void> voidRelay = PublishRelay.create();

    private Relay<Void> connectSocketVoidRelay = PublishRelay.create();


}
