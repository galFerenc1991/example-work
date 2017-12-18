package com.ferenc.pamp.presentation.utils;

import android.util.Log;

import com.ferenc.pamp.data.api.RestConst;
import com.ferenc.pamp.data.model.common.Contributor;
import com.ferenc.pamp.data.model.common.User;
import com.ferenc.pamp.data.model.message.Description;
import com.ferenc.pamp.data.model.message.MessageResponse;
import com.jakewharton.rxrelay2.PublishRelay;
import com.jakewharton.rxrelay2.Relay;

import org.androidannotations.annotations.EBean;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

/**
 * Created by shonliu on 12/18/17.
 */
@EBean(scope = EBean.Scope.Singleton)
public class SocketUtil {

    private Socket mSocket;

    private String TAG = "SocketIO";

    private String eventNewMessage = "message";
    private String emitNewMessage = "message";
    private String emitJoinRoom = "join";

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


    public void initSocket() {
        try {
            mSocket = IO.socket(RestConst.BASE_URL);
            Log.d(TAG, "Initialized");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void connectSocket() {
        mSocket.connect();
        mSocket.on(Socket.EVENT_CONNECT, args -> {
            Log.d(TAG, "Connected");
            changeConnectionObservable.accept(mSocket.connected());
        }).on(eventNewMessage, args -> {
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

                messageResponse.user = user;
                messageResponse._id = data.getString(valId);
                messageResponse.type = data.getString(valType);
                messageResponse.text = data.getString(valText);
                messageResponse.createdAt = data.getLong(valCreatedAt);


                Log.d(TAG, "new message:" + data.toString());

                onNewMessage.accept(messageResponse);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

    }


    public void joinRoom(String _userToken, String _roomID) {
        JSONObject obj = new JSONObject();
        try {
            obj.put(valToken, _userToken);
            obj.put(valDealID, _roomID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "Emitting: " + obj.toString());
        mSocket.emit(emitJoinRoom, obj);
        Log.d(TAG, "Joined to room");
    }

    public void sendMessage(String _userToken, String dealId, String _messageText) {
        JSONObject obj = new JSONObject();


        try {
//            if (!imageBase64.equals("")) {
//                obj.put(valContent, imageBase64);
//            }
            obj.put(valToken, _userToken);
            obj.put(valDealID, dealId);
            obj.put(valText, _messageText);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(TAG, "emitting: " + obj.toString());

        mSocket.emit(emitNewMessage, obj);
    }

    public void socketDisconnect() {
        if (mSocket != null) {
            mSocket.disconnect();
        }
    }

    public Relay<Boolean> changeConnectionObservable = PublishRelay.create();

    public Relay<MessageResponse> onNewMessage = PublishRelay.create();


}
