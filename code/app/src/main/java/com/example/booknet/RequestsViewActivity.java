package com.example.booknet;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class RequestsViewActivity extends AppCompatActivity {

    //Layout Objects
    private RecyclerView requestsList;
    private UserRequestAdapter requestAdapter;

    //App Data
    ArrayList<UserAccount> requests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests_view);

        //todo get real data
        //temp dummy data
        requests = new ArrayList<>();
        requests.add(new UserAccount("User1","debug"));
        requests.add(new UserAccount("User2","debug"));
        requests.add(new UserAccount("User3","debug"));

        //Setup RecyclerView
        requestsList = findViewById(R.id.requestList);
        requestsList.setLayoutManager(new LinearLayoutManager(this));
        requestAdapter = new UserRequestAdapter(requests,this);
        requestsList.setAdapter(requestAdapter);

    }
}
