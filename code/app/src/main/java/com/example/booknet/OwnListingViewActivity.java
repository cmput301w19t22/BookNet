package com.example.booknet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class OwnListingViewActivity extends AppCompatActivity {

    //Layout Objects
    Button viewRequestsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_own_listing_view);


        viewRequestsButton=findViewById(R.id.viewRequestsButton);
        viewRequestsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewRequests();
            }
        });

    }

    private void viewRequests(){
        Intent intent = new Intent(this, RequestsViewActivity.class);
        startActivity(intent);
    }
}
