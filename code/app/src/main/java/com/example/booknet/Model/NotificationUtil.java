package com.example.booknet.Model;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

/**
 * Simplifies common {@link Notification} tasks.
 */
public class NotificationUtil {

    public static String createNotificationChannel(Context context, NotificationData notificationData) {

        // NotificationChannels are required for InAppNotifications on O (API 26) and above.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            // The id of the channel.
            String channelId = notificationData.getChannelId();

            // The user-visible name of the channel.
            CharSequence channelName = notificationData.getChannelName();
            // The user-visible description of the channel.
            String channelDescription = notificationData.getChannelDescription();
            int channelImportance = notificationData.getChannelImportance();
            boolean channelEnableVibrate = notificationData.isChannelEnableVibrate();
            int channelLockscreenVisibility = notificationData.getChannelLockscreenVisibility();

            // Initializes NotificationChannel.
            NotificationChannel notificationChannel =
                    new NotificationChannel(channelId, channelName, channelImportance);
            //notificationChannel.setDescription(channelDescription);
            //notificationChannel.enableVibration(channelEnableVibrate);
            //notificationChannel.setLockscreenVisibility(channelLockscreenVisibility);

            // Adds NotificationChannel to system. Attempting to create an existing notification
            // channel with its original values performs no operation, so it's safe to perform the
            // below sequence.
            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);

            return channelId;
        } else {
            // Returns null for pre-O (26) devices.
            return null;
        }
    }
}
