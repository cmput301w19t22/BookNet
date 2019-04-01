package com.example.booknet.Activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.booknet.Model.CurrentUser;
import com.example.booknet.DatabaseManager;
import com.example.booknet.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class AccountCreateActivity extends AppCompatActivity {

    //Layout Objects
    private EditText usernameField;
    private EditText emailField;
    private EditText phoneField;
    private EditText passwordField;
    private Button createButton;

    private int drawableCheckId = R.drawable.ic_check_black_24dp;
    private int drawableCrossId = R.drawable.ic_close_black_24dp;

    //Activity Data
    private FirebaseAuth mAuth;
    private DatabaseManager manager = DatabaseManager.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_create);

        //Get Layout Objects


        mAuth = FirebaseAuth.getInstance();

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUserAccount();
            }
        });

    }


    private void createUserAccount() {
        if (!checkFormFields())
            return;

        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();

        // TODO: Create the user account
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this,
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "User created", Toast.LENGTH_SHORT)
                                            .show();
                                    makeAccount();
                                    finish();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Account creation failed", Toast.LENGTH_SHORT)
                                            .show();
                                }
                            }
                        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("LOGIN", e.toString());
                        if (e instanceof FirebaseAuthUserCollisionException) {
                            emailField.setError("Email already in use");
                            //updateStatus("This email address is already in use.");
                        } else {
                            updateStatus(e.getLocalizedMessage());
                        }
                    }
                });
    }

    private void makeAccount() {
        String phonenumber = phoneField.getText().toString();
        String username = usernameField.getText().toString();
        String email = emailField.getText().toString();

        //todo: validate phonenumber
        boolean takenUsername = manager.isUsernameTaken(username);
        if (takenUsername) {
            //Toast.makeText(getApplicationContext(), "Username taken!", Toast.LENGTH_SHORT).show();
            usernameField.setError("Username taken!");
        } else {
            //Valid Account, Now Create
            manager.writeUserProfile(email, phonenumber);
            manager.writeUsername(username);
            manager.writeUserPhone(phonenumber);
            CurrentUser.getInstance().setUsername(username);
            CurrentUser.getInstance().setAccountPhone(phonenumber);
            finish();
        }
    }


    private boolean checkFormFields() {
        String email, password, username;

        email = emailField.getText().toString();
        password = passwordField.getText().toString();
        username = usernameField.getText().toString();

        if (email.isEmpty()) {
            emailField.setError("Email Required");
            return false;
        }
        if (password.isEmpty()) {
            passwordField.setError("Password Required");
            return false;
        }
        if (username.isEmpty()) {
            usernameField.setError("Username Required");
            return false;
        }

        return true;
    }

    private void updateStatus(String stat) {
        TextView tvStat = findViewById(R.id.tvSignInStatus);
        tvStat.setText(stat);
    }
}
