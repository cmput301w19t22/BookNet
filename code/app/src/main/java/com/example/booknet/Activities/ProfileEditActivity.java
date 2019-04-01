package com.example.booknet.Activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.booknet.Model.CurrentUser;
import com.example.booknet.DatabaseManager;
import com.example.booknet.R;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

/**
 * Activity to edit your user profile.
 *
 * @author Jamie
 * @version 1.0
 */
public class ProfileEditActivity extends AppCompatActivity {

    //Layout Objects
    private EditText usernameLabel;
    private EditText emailField;
    private EditText phoneField;
    private ImageButton applyButton;
    private ImageButton cancelButton;

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
        manager.writeUserProfile(newEmail, newPhone,
                new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {

                    }
                },

                new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {

                    }
                });

        finish();

    }
}
