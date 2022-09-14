package com.JuTemp.Home.util;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.JuTemp.Home.ApplJuTemp;

public class NotificationJuTemp {

    static NotificationManager notifyManager=null;

    public static void sendNotification(Context This, Intent intent, int id, int resSmallIcon, String title, String text) {
        notifyManager = (NotificationManager) This.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel notificationChannel = new NotificationChannel(ApplJuTemp.NOTIFICATION_CHANNEL, ApplJuTemp.NOTIFICATION_CHANNEL, NotificationManager.IMPORTANCE_HIGH);
        notifyManager.createNotificationChannel(notificationChannel);
        PendingIntent contentIntent = PendingIntent.getActivity(This, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        Notification notification = new Notification.Builder(This, ApplJuTemp.NOTIFICATION_CHANNEL)
                .setSmallIcon(resSmallIcon)
                .setContentTitle(text)
                .setContentIntent(contentIntent)
                .build(); // getNotification()
        notification.flags |= Notification.FLAG_NO_CLEAR ;
        notifyManager.notify(id, notification);
    }

    public static void cancelNotification(int id) {
        notifyManager.cancel(id);
    }
}
