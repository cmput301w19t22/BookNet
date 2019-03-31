package com.example.booknet.Activities;

import android.app.Notification;
import android.app.NotificationManager;
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

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.cancel(NOTIFICATION_ID);

        listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot user : dataSnapshot.getChildren()) {
                    for (DataSnapshot data : user.getChildren()) {
                        InAppNotification inAppNotification = data.getValue(InAppNotification.class);
                        if (inAppNotification.getUserReceivingNotification().equals(CurrentUser.getInstance().getUsername()) && !inAppNotification.getPushNotificationSent()) {
                            Log.d("seanTag", "send push notification");
                            generateNotification(inAppNotification.getUserMakingNotification().toString() + inAppNotification.getNotificationType().toString(), "BookNet Notification", "test");
                            inAppNotification.setPushNotificationSent(true);
                            manager.writeNotification(inAppNotification);
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

    public void generateNotification(String text, String title, String description) {
        Log.d("seanTag", "generateBigTextStyleNotification()");
        NotificationData notificationData = new NotificationData(text, title, description);

        String notificationChannelId =
                NotificationUtil.createNotificationChannel(this, notificationData);

        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle()
                .bigText(notificationData.getContentText())
                .setBigContentTitle(notificationData.getContentTitle())
                .setSummaryText(notificationData.getChannelDescription());


        Intent notifyIntent = new Intent(this, MainActivity.class);

        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent notifyPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        notifyIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        NotificationCompat.Builder notificationCompatBuilder =
                new NotificationCompat.Builder(
                        getApplicationContext(), notificationChannelId);

        GlobalNotificationBuilder.setNotificationCompatBuilderInstance(notificationCompatBuilder);

        Notification notification = notificationCompatBuilder
                .setStyle(bigTextStyle)
                .setContentTitle(notificationData.getContentTitle())
                .setContentText(notificationData.getContentText())
                .setSmallIcon(R.drawable.ic_star_24dp)
                .setLargeIcon(BitmapFactory.decodeResource(
                        getResources(),
                        R.drawable.bg_round_rectangle_24))
                .setContentIntent(notifyPendingIntent)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary))
                .setCategory(Notification.CATEGORY_REMINDER)
                .setPriority(notificationData.getPriority())
                .setVisibility(notificationData.getChannelLockscreenVisibility())
                .build();

        mNotificationManagerCompat.notify(NOTIFICATION_ID, notification);
    }
}
