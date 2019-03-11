package com.example.booknet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class UserProfileViewActivity extends AppCompatActivity {

    //Layout Objects
    TextView usernameLabel;
    TextView phoneLabel;
    TextView emailLabel;
    TextView ratingLabel;
    ImageView star1;
    ImageView star2;
    ImageView star3;
    ImageView star4;
    ImageView star5;
    Button reviewsButton;
    Button booksButton;

    //Data
    UserAccount userAccount;
    String username = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_view);

        //Obtain References To Layout Objects
        usernameLabel = findViewById(R.id.userNameLabel);
        phoneLabel = findViewById(R.id.phoneNumberLabel);
        emailLabel = findViewById(R.id.emailLabel);
        ratingLabel = findViewById(R.id.ratingTextLabel);
        star1 = findViewById(R.id.ratingStar1);
        star2 = findViewById(R.id.ratingStar2);
        star3 = findViewById(R.id.ratingStar3);
        star4 = findViewById(R.id.ratingStar4);
        star5 = findViewById(R.id.ratingStar5);
        reviewsButton = findViewById(R.id.reviewsButton);
        booksButton = findViewById(R.id.libraryButton);

        //Get Profile
        Intent intent = getIntent();
        //Check that we have a username to check
        if (!intent.hasExtra("username")) {
            Toast.makeText(this, "View Not Passed With Username", Toast.LENGTH_LONG).show();
            //finish();
        } else {
            //Obtain the UserAccount for the username
            username = intent.getStringExtra("username");
            if (intent.hasExtra("isMe")) {
                //Get local copy if viewing own account
                userAccount = CurrentUser.getInstance().getUserAccount();
            } else {
                userAccount = fetchUser(username);
            }
            fillLayout();
        }


        booksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewUserBooks(userAccount);
            }
        });
    }

    /**
     * Fills the layout with info from the user account
     */
    private void fillLayout() {
        if (userAccount == null) {
            Toast.makeText(this, "User Not Found: " + username, Toast.LENGTH_LONG).show();
            //finish();
        } else {
            //Fill Layout With Info
            usernameLabel.setText(userAccount.getUsername());
            phoneLabel.setText(userAccount.getProfile().getPhoneNumber());
            emailLabel.setText(userAccount.getProfile().getEmail());
            ratingLabel.setText(String.format("%1.1f", userAccount.getRatingScore()));
        }
    }

    /**
     * Obtains the UserAccount from the database
     *
     * @param username
     * @return
     */
    private UserAccount fetchUser(String username) {
        return MockDatabase.getInstance().readUserAccount(username);
    }


    private void viewUserBooks(UserAccount user) {
        //todo: implement
    }
}
