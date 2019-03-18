package com.example.booknet;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.TextView;

/**
 * Activity for the app's homepage.
 *
 * @version 0.1
 */
public class MainActivity extends FragmentActivity {

    public static class MyAdapter extends FragmentPagerAdapter {
        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public Fragment getItem(int position) {
            Log.d("mattTag", "position "+String.valueOf(position));
            if (position == 0) {
                return BookSearchFragment.newInstance();
            }
            else if (position == 1) {
                return OwnedLibraryFragment.newInstance();
            }
            else if (position == 2) {
                return UserProfileViewFragment.newInstance();
            }
            else if (position == 3) {
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
                case R.id.navigation_myaccount:
                    myAccountClicked();
                    return true;
                case R.id.navigation_notifications:
                    myNotificationsClicked();
                    //mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mAdapter = new MyAdapter(getSupportFragmentManager());

        mPager = (ViewPager)findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);

        final BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        /*mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

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
                    navigation.setSelectedItemId(R.id.navigation_myaccount);
                }
                else if (i == 3) {
                    navigation.setSelectedItemId(R.id.navigation_notifications);
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        searchBar = findViewById(R.id.mainSearch);*/

        Log.d("mattTag", "leaving main activity onCreate");
    }

    private void onSearchClicked() { mPager.setCurrentItem(0); }

    private void myBooksClicked() { mPager.setCurrentItem(1); }

    private void myAccountClicked() { mPager.setCurrentItem(2); }

    private void myNotificationsClicked() { mPager.setCurrentItem(3); }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Are you sure?")
                .setMessage("Log out?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CurrentUser.getInstance().logout();

                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();

    }


}
