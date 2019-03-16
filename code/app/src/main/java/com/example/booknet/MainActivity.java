package com.example.booknet;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.TextView;

/**
 * Activity for the app's homepage.
 *
 * @version 0.1
 */
public class MainActivity extends AppCompatActivity {

    //Layout Objects
    private TextView mTextMessage;
    private SearchView searchBar;

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
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("mattTag", "phew, now in main activity");

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        searchBar = findViewById(R.id.mainSearch);

        Log.d("mattTag", "leaving main activity onCreate");
    }


    private void myBooksClicked() {
        Intent intent = new Intent(this, OwnedLibraryActivity.class);
        startActivity(intent);
    }

    private void myAccountClicked() {
        Intent intent = new Intent(this, UserProfileViewActivity.class);
        String username = CurrentUser.getInstance().getUserAccount().getUsername();
        intent.putExtra("username", username);
        intent.putExtra("isMe", true);
        startActivity(intent);

    }

    private void onSearchClicked() {
        Intent intent = new Intent(this, BookSearchActivity.class);
        startActivity(intent);

    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Are you sure?")
                .setMessage("Log out and close app?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }


}
