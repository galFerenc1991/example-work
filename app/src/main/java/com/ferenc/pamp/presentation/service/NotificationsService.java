package com.ferenc.pamp.presentation.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.ferenc.pamp.R;
import com.ferenc.pamp.presentation.base.models.Notif;
import com.ferenc.pamp.presentation.screens.main.MainActivity_;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

/**
 * Created by
 * Ferenc on 2018.01.17..
 */

public class NotificationsService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        Gson gson = new Gson();
        Notif _data = gson.fromJson(remoteMessage.getData().get("description"), Notif.class);

        if (Build.VERSION.SDK_INT >= 26) {
            String id = "id_product";
            // The user-visible name of the channel.
            CharSequence name = "Product";
            // The user-visible description of the channel.
            String description = "Notifications regarding our products";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(id, name, importance);
            // Configure the notification channel.
            mChannel.setDescription(description);
            mChannel.enableLights(true);
            // Sets the notification light color for notifications posted to this
            // channel, if the device supports this feature.
            mChannel.setLightColor(Color.RED);
            notificationManager.createNotificationChannel(mChannel);

            Intent intent = new Intent(this, MainActivity_.class);
            intent.putExtra("mDealId", _data.getDealId());

            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "id_product")
                    .setSmallIcon(R.drawable.ic_pamp_logo_notif)
                    .setAutoCancel(true)
                    .setContentTitle(_data.getTitle())
                    .setContentText(_data.getBody())
                    .setContentIntent(pendingIntent);


            notificationManager.notify(0, builder.build());

        } else {
            Intent intent = new Intent(this, MainActivity_.class);
            intent.putExtra("mDealId", _data.getDealId());

            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default")
                    .setSmallIcon(R.drawable.ic_pamp_logo_notif)
                    .setAutoCancel(true)
                    .setContentTitle(_data.getTitle())
                    .setContentText(_data.getBody())
                    .setContentIntent(pendingIntent);


            notificationManager.notify(0, builder.build());
        }

    }
}
