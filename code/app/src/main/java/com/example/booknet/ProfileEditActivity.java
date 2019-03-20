package com.example.booknet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Activity to edit a user profile. Only actually allows you to edit your own.
 *
 * @author Jamie
 * @version 1.0
 */
public class ProfileEditActivity extends AppCompatActivity {

    //Layout Objects
    private TextView usernameLabel;
    private EditText emailField;
    private EditText phoneField;
    private Button applyButton;
    private Button cancelButton;

    //Activity Data
    private String username;
    private DatabaseManager manager = DatabaseManager.getInstance();
    /**
     * Called when creating the activity
     * Gets the intent and sets listeners
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        //Get Layout Objects
        usernameLabel = findViewById(R.id.userNameLabel);
        emailField = findViewById(R.id.emailField);
        phoneField = findViewById(R.id.phoneField);
        applyButton = findViewById(R.id.applyButton);
        cancelButton = findViewById(R.id.cancelButton);




        //Set Listeners
        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();
                //finish();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        usernameLabel.setText(CurrentUser.getInstance().getUsername());
        phoneField.setText(CurrentUser.getInstance().getProfilePhone());
        emailField.setText(CurrentUser.getInstance().getProfileEmail());

    }


    /**
     * Updates the user profile
     */

    private void updateProfile() {
        String newEmail = emailField.getText().toString();
        String newPhone = phoneField.getText().toString();
        manager.writeUserProfile(newEmail, newPhone);
        finish();

    }
}
