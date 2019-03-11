package com.example.booknet;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class ProfileEditActivity extends AppCompatActivity {

    //Layout Objects
    TextView usernameLabel;
    EditText emailField;
    EditText phoneField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);
    }
}
