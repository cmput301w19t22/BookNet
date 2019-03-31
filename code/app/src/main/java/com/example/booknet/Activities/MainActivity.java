package com.example.booknet.Activities;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.booknet.DatabaseManager;
import com.example.booknet.Fragments.BookSearchFragment;
import com.example.booknet.Model.CurrentUser;
import com.example.booknet.Fragments.NotificationFragment;
import com.example.booknet.Fragments.OwnedLibraryFragment;
import com.example.booknet.Model.GlobalNotificationBuilder;
import com.example.booknet.Model.InAppNotification;
import com.example.booknet.Model.NotificationData;
import com.example.booknet.Model.NotificationUtil;
import com.example.booknet.R;
import com.example.booknet.Fragments.RequestLibraryFragment;
import com.example.booknet.Fragments.UserProfileViewFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

/**
 * Activity for the app's homepage.
 *
 * @version 0.1
 */
public class MainActivity extends FragmentActivity {

    public static final int NOTIFICATION_ID = 888;

    private NotificationManagerCompat mNotificationManagerCompat;

    DatabaseManager manager = DatabaseManager.getInstance();
    private ValueEventListener listener;

    public static class MyAdapter extends FragmentPagerAdapter {
        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public Fragment getItem(int position) {
            Log.d("mattTag", "position " + String.valueOf(position));
            if (position == 0) {
                return BookSearchFragment.newInstance();
            } else if (position == 1) {
                return OwnedLibraryFragment.newInstance();
            } else if (position == 2) {
                return RequestLibraryFragment.newInstance();
            } else if (position == 3) {
                return UserProfileViewFragment.newInstance();
            } else if (position == 4) {
                return NotificationFragment.newInstance();
            }
            return BookSearchFragment.newInstance();
        }
    }

    //Layout Objects
    private TextView mTextMessage;
    private SearchView searchBar;
    MyAdapter mAdapter;
    ViewPager mPager;

    //Set Click Listener for Navigation Bar
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_search:
                    onSearchClicked();
                    return true;
                case R.id.navigation_mybooks:
                    myBooksClicked();
                    return true;
                case R.id.navigation_myrequests:
                    myRequestsClicked();
                    return true;
                case R.id.navigation_myaccount:
                    myAccountClicked();
                    return true;
                case R.id.navigation_notifications:
                    myNotificationsClicked();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("mattTag", "freaking on Main");

        setContentView(R.layout.activity_main);

        mAdapter = new MyAdapter(getSupportFragmentManager());

        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);

        final BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mNotificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());

        listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    InAppNotification notification = data.getValue(InAppNotification.class);
                    if (notification != null) {
                        if (!notification.getPushNotificationSent()) {
                            Log.d("seanTag", "send push notification");
                            generateNotification();
                            notification.setPushNotificationSent(true);
                            manager.writeNotification(notification);
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        manager.getNotificationsRef().addValueEventListener(listener);


        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int i) {


                if (i == 0) {
                    navigation.setSelectedItemId(R.id.navigation_search);
                }
                else if (i == 1) {
                    navigation.setSelectedItemId(R.id.navigation_mybooks);
                }
                else if (i == 2) {
                    navigation.setSelectedItemId(R.id.navigation_myrequests);
                }
                else if (i == 3) {
                    navigation.setSelectedItemId(R.id.navigation_myaccount);
                }
                else if (i == 4) {
                    navigation.setSelectedItemId(R.id.navigation_notifications);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void onSearchClicked() {
        mPager.setCurrentItem(0);
    }

    private void myBooksClicked() {
        mPager.setCurrentItem(1);
    }

    private void myRequestsClicked() {
        mPager.setCurrentItem(2);
    }

    private void myAccountClicked() {
        mPager.setCurrentItem(3);
    }

    private void myNotificationsClicked() {
        mPager.setCurrentItem(4);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Are you sure?")
                .setMessage("Log out?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CurrentUser.getInstance().logout();

                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();

    }

    /**
     * Gets the result and passes it up so nested fragments can get it.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("mattNeo", "yee I deed it");
    }

    BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("seanTag", "recieve broadcast");
        }
    };

    /*
     * Generates a BIG_TEXT_STYLE InAppNotification that supports both phone/tablet and wear. For devices
     * on API level 16 (4.1.x - Jelly Bean) and after, displays BIG_TEXT_STYLE. Otherwise, displays
     * a basic notification.
     */
    public void generateNotification() {

        Log.d("seanTag", "generateBigTextStyleNotification()");

        // Main steps for building a BIG_TEXT_STYLE notification:
        //      0. Get your data
        //      1. Create/Retrieve InAppNotification Channel for O and beyond devices (26+)
        //      2. Build the BIG_TEXT_STYLE
        //      3. Set up main Intent for notification
        //      4. Create additional Actions for the InAppNotification
        //      5. Build and issue the notification

        // 0. Get your data (everything unique per InAppNotification).
        NotificationData notificationData = new NotificationData();

        // 1. Create/Retrieve InAppNotification Channel for O and beyond devices (26+).
        String notificationChannelId =
                NotificationUtil.createNotificationChannel(this, notificationData);

        // 2. Build the BIG_TEXT_STYLE.
        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle()
                // Overrides ContentText in the big form of the template.
                .bigText(notificationData.getContentText())
                // Overrides ContentTitle in the big form of the template.
                .setBigContentTitle(notificationData.getContentTitle())
                // Summary line after the detail section in the big form of the template.
                // Note: To improve readability, don't overload the user with info. If Summary Text
                // doesn't add critical information, you should skip it.
                .setSummaryText(notificationData.getChannelDescription());


        // 3. Set up main Intent for notification.
        Intent notifyIntent = new Intent(this, MainActivity.class);

        // When creating your Intent, you need to take into account the back state, i.e., what
        // happens after your Activity launches and the user presses the back button.

        // There are two options:
        //      1. Regular activity - You're starting an Activity that's part of the application's
        //      normal workflow.

        //      2. Special activity - The user only sees this Activity if it's started from a
        //      notification. In a sense, the Activity extends the notification by providing
        //      information that would be hard to display in the notification itself.

        // For the BIG_TEXT_STYLE notification, we will consider the activity launched by the main
        // Intent as a special activity, so we will follow option 2.

        // For an example of option 1, check either the MESSAGING_STYLE or BIG_PICTURE_STYLE
        // examples.

        // For more information, check out our dev article:
        // https://developer.android.com/training/notify-user/navigation.html

        // Sets the Activity to start in a new, empty task
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent notifyPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        notifyIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );


        // 4. Create additional Actions (Intents) for the InAppNotification.

        // In our case, we create two additional actions: a Snooze action and a Dismiss action.
        // Snooze Action.
        /*Intent snoozeIntent = new Intent(this, BigTextIntentService.class);
        snoozeIntent.setAction(BigTextIntentService.ACTION_SNOOZE);

        PendingIntent snoozePendingIntent = PendingIntent.getService(this, 0, snoozeIntent, 0);
        NotificationCompat.Action snoozeAction =
                new NotificationCompat.Action.Builder(
                        R.drawable.ic_alarm_white_48dp,
                        "Snooze",
                        snoozePendingIntent)
                        .build();*/


        // Dismiss Action.
        /*Intent dismissIntent = new Intent(this, BigTextIntentService.class);
        dismissIntent.setAction(BigTextIntentService.ACTION_DISMISS);

        PendingIntent dismissPendingIntent = PendingIntent.getService(this, 0, dismissIntent, 0);
        NotificationCompat.Action dismissAction =
                new NotificationCompat.Action.Builder(
                        R.drawable.ic_cancel_white_48dp,
                        "Dismiss",
                        dismissPendingIntent)
                        .build();*/


        // 5. Build and issue the notification.

        // Because we want this to be a new notification (not updating a previous notification), we
        // create a new Builder. Later, we use the same global builder to get back the notification
        // we built here for the snooze action, that is, canceling the notification and relaunching
        // it several seconds later.

        // InAppNotification Channel Id is ignored for Android pre O (26).
        NotificationCompat.Builder notificationCompatBuilder =
                new NotificationCompat.Builder(
                        getApplicationContext(), notificationChannelId);

        GlobalNotificationBuilder.setNotificationCompatBuilderInstance(notificationCompatBuilder);

        Notification notification = notificationCompatBuilder
                // BIG_TEXT_STYLE sets title and content for API 16 (4.1 and after).
                .setStyle(bigTextStyle)
                // Title for API <16 (4.0 and below) devices.
                .setContentTitle(notificationData.getContentTitle())
                // Content for API <24 (7.0 and below) devices.
                .setContentText(notificationData.getContentText())
                .setSmallIcon(R.drawable.ic_star_24dp)
                .setLargeIcon(BitmapFactory.decodeResource(
                        getResources(),
                        R.drawable.bg_round_rectangle_24))
                .setContentIntent(notifyPendingIntent)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                // Set primary color (important for Wear 2.0 InAppNotifications).
                .setColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary))

                // SIDE NOTE: Auto-bundling is enabled for 4 or more notifications on API 24+ (N+)
                // devices and all Wear devices. If you have more than one notification and
                // you prefer a different summary notification, set a group key and create a
                // summary notification via
                // .setGroupSummary(true)
                // .setGroup(GROUP_KEY_YOUR_NAME_HERE)

                .setCategory(Notification.CATEGORY_REMINDER)

                // Sets priority for 25 and below. For 26 and above, 'priority' is deprecated for
                // 'importance' which is set in the NotificationChannel. The integers representing
                // 'priority' are different from 'importance', so make sure you don't mix them.
                .setPriority(notificationData.getPriority())

                // Sets lock-screen visibility for 25 and below. For 26 and above, lock screen
                // visibility is set in the NotificationChannel.
                .setVisibility(notificationData.getChannelLockscreenVisibility())

                // Adds additional actions specified above.
                //.addAction(snoozeAction)
                //.addAction(dismissAction)

                .build();

        mNotificationManagerCompat.notify(NOTIFICATION_ID, notification);
    }
}
