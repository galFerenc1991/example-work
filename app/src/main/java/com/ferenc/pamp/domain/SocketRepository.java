package com.ferenc.pamp.domain;

import android.util.Log;

import com.ferenc.pamp.data.api.RestConst;
import com.ferenc.pamp.data.model.common.User;
import com.ferenc.pamp.data.model.message.MessageResponse;
import com.ferenc.pamp.presentation.screens.main.chat.messenger.MessengerContract;
import com.jakewharton.rxrelay2.PublishRelay;
import com.jakewharton.rxrelay2.Relay;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
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

    @AfterInject
    protected void initSocket() {
        try {
            mSocket = IO.socket(RestConst.BASE_URL);
            Log.d(TAG, "Initialized");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

    }

    @Override
    public Observable<Void> connectSocket(String _userToken, String _roomID) {
        if (mSocket.connected())
            return connectSocketVoidRelay;

        mSocket.connect();
        mSocket.on(Socket.EVENT_CONNECT, args -> {
            Log.d(TAG, "Connected");
            joinRoom(_userToken,_roomID);
        }).on(Socket.EVENT_MESSAGE, args -> {
            JSONObject data = (JSONObject) args[0];

            try {
                MessageResponse messageResponse = new MessageResponse();
                User user = new User();

                if (data.has(valContent)) {
                    messageResponse.content = data.getString(valContent);
                }
                user.setId(data.getJSONObject(valUser).getString(valId));
                user.setFirstName(data.getJSONObject(valUser).getString(valFirstName));
                user.setLastName(data.getJSONObject(valUser).getString(valLastName));
                user.setAvatar(data.getJSONObject(valUser).getString(valAvatar));

                messageResponse.code = data.has(valCode) ? data.getString(valCode) : null;
                messageResponse.user = user;
                messageResponse._id = data.getString(valId);
                messageResponse.type = data.getString(valType);
                messageResponse.text = data.getString(valText);
                messageResponse.createdAt = data.getLong(valCreatedAt);

                Log.d(TAG, "New message:" + data.toString());

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
    public Observable<Void> sendMessage(String _userToken, String _dealId, String _messageText) {

        JSONObject obj = new JSONObject();
        try {
//            if (!imageBase64.equals("")) {
//                obj.put(valContent, imageBase64);
//            }
            obj.put(valToken, _userToken);
            obj.put(valDealID, _dealId);
            obj.put(valText, _messageText);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(TAG, "Emitting: send message" + obj.toString());

        mSocket.emit(Socket.EVENT_MESSAGE, obj);

        return voidRelay;
    }


    public void joinRoom(String _userToken, String _roomID) {

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
            mSocket.disconnect();
            Log.d(TAG, "Emitting: leave room");
            Log.d(TAG, "Disconnect");
        }
        return voidRelay;
    }

    public Relay<MessageResponse> getNewMessage = PublishRelay.create();

    public Relay<Void> voidRelay = PublishRelay.create();

    public Relay<Void> connectSocketVoidRelay = PublishRelay.create();


}
