package com.ferenc.pamp.presentation.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.ferenc.pamp.R;
import com.ferenc.pamp.presentation.screens.main.MainActivity_;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by
 * Ferenc on 2018.01.17..
 */

public class NotificationsService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
//        RxBus.getInstance().post(new DownloadNewNotificationEvent());
        Log.d("push", "push");

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity_.class),
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "channelId")
                .setSmallIcon(R.drawable.pamp_logo_smaller)
                .setContentTitle(remoteMessage.getNotification() != null ? remoteMessage.getNotification().getTitle() : "")
                .setContentText(remoteMessage.getNotification() != null ? remoteMessage.getNotification().getBody() : "")
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, builder.build());
    }
}
