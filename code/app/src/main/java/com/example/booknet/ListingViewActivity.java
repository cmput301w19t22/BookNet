package com.example.booknet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class ListingViewActivity extends AppCompatActivity {

    //Layout Objects
    Button ownerProfileButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing_view);

        ownerProfileButton = findViewById(R.id.ownerProfileButton);
        ownerProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewOwnerProfile();
            }
        });
    }


    public void viewOwnerProfile() {
        Intent intent = new Intent(this, UserProfileViewActivity.class);
        startActivity(intent);
    }
}
