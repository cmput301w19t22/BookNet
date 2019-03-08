package com.example.booknet;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private SearchView searchBar;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_search:
                    //mTextMessage.setText(R.string.title_home);
                    searchClicked();
                    return true;
                case R.id.navigation_mybooks:
                    //mTextMessage.setText(R.string.title_dashboard);
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

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        searchBar = findViewById(R.id.mainSearch);

    }


    private void myBooksClicked() {
        Intent intent = new Intent(this, OwnedLibraryActivity.class);
        startActivity(intent);
    }

    private void myAccountClicked() {
        Intent intent = new Intent(this, UserProfileViewActivity.class);
        startActivity(intent);
    }

    private void searchClicked() {
        Intent intent = new Intent(this, BookSearchActivity.class);
        startActivity(intent);
    }
}
