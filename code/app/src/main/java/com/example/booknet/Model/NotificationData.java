package com.example.booknet.Model;

import android.app.NotificationManager;
import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

public class NotificationData {
    /** Represents standard data needed for a InAppNotification. */
    // Standard notification values:
    private String mContentTitle;
    private String mContentText;
    private int mPriority;

    // InAppNotification channel values (O and above):
    private String mChannelId;
    private CharSequence mChannelName;
    private String mChannelDescription;
    private int mChannelImportance;
    private boolean mChannelEnableVibrate;
    private int mChannelLockscreenVisibility;

    public NotificationData() {
        this.mPriority = NotificationCompat.PRIORITY_DEFAULT;
        this.mChannelId = "channel_reminder_1";
        this.mChannelName = "BookNet Notification";

        this.mContentTitle = "A InAppNotification";
        this.mContentText = "To notify you of important";
        this.mChannelDescription = "Test InAppNotification";

        this.mChannelImportance = NotificationManager.IMPORTANCE_DEFAULT;
        this.mChannelEnableVibrate = false;
        this.mChannelLockscreenVisibility = NotificationCompat.VISIBILITY_PUBLIC;
    }

    public NotificationData(String mContentText, String mContentTitle, String mChannelDescription) {
        this.mPriority = NotificationCompat.PRIORITY_DEFAULT;
        this.mChannelId = "channel_reminder_1";
        this.mChannelName = "BookNet Notification";

        this.mContentTitle = mContentTitle;
        this.mContentText = mContentText;
        this.mChannelDescription = mChannelDescription;

        this.mChannelImportance = NotificationManager.IMPORTANCE_DEFAULT;
        this.mChannelEnableVibrate = false;
        this.mChannelLockscreenVisibility = NotificationCompat.VISIBILITY_PUBLIC;
    }

    // InAppNotification Standard notification get methods:
    public String getContentTitle() {
        return mContentTitle;
    }

    public String getContentText() {
        return mContentText;
    }

    public int getPriority() {
        return mPriority;
    }

    // Channel values (O and above) get methods:
    public String getChannelId() {
        return mChannelId;
    }

    public CharSequence getChannelName() {
        return mChannelName;
    }

    public String getChannelDescription() {
        return mChannelDescription;
    }

    public int getChannelImportance() {
        return mChannelImportance;
    }

    public boolean isChannelEnableVibrate() {
        return mChannelEnableVibrate;
    }

    public int getChannelLockscreenVisibility() {
        return mChannelLockscreenVisibility;
    }

    public static Uri resourceToUri(Context context, int resId) {
        return Uri.parse(
                ContentResolver.SCHEME_ANDROID_RESOURCE
                        + "://"
                        + context.getResources().getResourcePackageName(resId)
                        + "/"
                        + context.getResources().getResourceTypeName(resId)
                        + "/"
                        + context.getResources().getResourceEntryName(resId));
    }
}
