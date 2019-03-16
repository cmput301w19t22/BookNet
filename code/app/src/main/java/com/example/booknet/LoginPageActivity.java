package com.example.booknet;

import android.app.Activity;
import android.content.Intent;;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

public class LoginPageActivity extends AppCompatActivity implements View.OnClickListener, InitialUserProfileDialog.InitialUserProfileListener {
    private final String TAG = "FB_SIGNIN";

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private EditText etPass;
    private EditText etEmail;
    private DatabaseManager manager = DatabaseManager.getInstance();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        manager.setOnLoginPage(true);

        setContentView(R.layout.activity_log_in);

        findViewById(R.id.btnCreate).setOnClickListener(this);
        findViewById(R.id.btnSignIn).setOnClickListener(this);

        etEmail = findViewById(R.id.etEmailAddr);
        etPass = findViewById(R.id.etPassword);

        // TODO: Get a reference to the Firebase auth object
        mAuth = FirebaseAuth.getInstance();


        // Async database initiation task
        // read all the database data to private attributes.
        // These data are updated in real time once the database changes.


        // TODO: Attach a new AuthListener to detect sign in and out
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "Signed in: " + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "Currently signed out");
                }
            }
        };

    }

    @Override
    public void onStart() {
        super.onStart();
        // TODO: add the AuthListener
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        // TODO: Remove the AuthListener
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSignIn:
                signUserIn();
                break;

            case R.id.btnCreate:
                createUserAccount();
                break;

            /*case R.id.btnSignOut:
                signUserOut();
                break;*/
        }
    }

    private boolean checkFormFields() {
        String email, password;

        email = etEmail.getText().toString();
        password = etPass.getText().toString();

        if (email.isEmpty()) {
            etEmail.setError("Email Required");
            return false;
        }
        if (password.isEmpty()){
            etPass.setError("Password Required");
            return false;
        }

        return true;
    }

    private void updateStatus() {
        TextView tvStat = findViewById(R.id.tvSignInStatus);
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            tvStat.setText("Signed in: " + user.getEmail());
        }
        else {
            tvStat.setText("Signed Out");
        }
    }



    private void updateStatus(String stat) {
        TextView tvStat = findViewById(R.id.tvSignInStatus);
        tvStat.setText(stat);
    }

    public void signUserIn() {
        if (!checkFormFields())
            return;

        final String email = etEmail.getText().toString();
        String password = etPass.getText().toString();

        final Activity loginPageActivity = this;

        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this,
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(LoginPageActivity.this, "Signed in", Toast.LENGTH_SHORT)
                                            .show();
                                    //Intent intent = new Intent(LoginPageActivity.this, UserProfile.class);
                                    //intent.putExtra("emailLabel", email);
                                    //startActivity(intent);

                                    //save current user for future use
                                    CurrentUser.getInstance().updateUser(mAuth.getCurrentUser());

                                    //Shows in-progress dialog during connection
                                    // As user phone/name needs to be checked, connection needs to be established first
                                    //this method also takes care of following intents
                                    manager.connectToDatabase(LoginPageActivity.this);

//                                    Log.d("mattTag",CurrentUser.getInstance().getUsername());
//                                    if (CurrentUser.getInstance().getUsername() == null || CurrentUser.getInstance().getPhone() == null){
//                                        DialogFragment dialog = new InitialUserProfileDialog();
//                                        dialog.show(getSupportFragmentManager(), "InitialUserProfileDialog");
//                                    }
//
//                                    else{
//
//                                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
//                                    }


//                                    AlertDialog.Builder builder = new AlertDialog.Builder(loginPageActivity);
//                                    builder.setMessage(R.string.usernameRequiredMessage)
//                                            .setTitle(R.string.usernameRequiredTitle);
//                                    AlertDialog dialog = builder.create();






                                }
                                else {
                                    Toast.makeText(LoginPageActivity.this, "Sign in failed", Toast.LENGTH_SHORT)
                                            .show();
                                }

                                updateStatus();
                            }
                        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (e instanceof FirebaseAuthInvalidCredentialsException) {
                            updateStatus("Invalid password.");
                        }
                        else if (e instanceof FirebaseAuthInvalidUserException) {
                            updateStatus("No account with this email.");
                        }
                        else {
                            updateStatus(e.getLocalizedMessage());
                        }
                    }
                });
    }

    /*private void signUserOut() {
        // TODO: sign the user out
        mAuth.signOut();
        updateStatus();
    }*/

    private void createUserAccount() {
        if (!checkFormFields())
            return;

        String email = etEmail.getText().toString();
        String password = etPass.getText().toString();

        // TODO: Create the user account
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this,
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(LoginPageActivity.this, "User created", Toast.LENGTH_SHORT)
                                            .show();
                                } else {
                                    Toast.makeText(LoginPageActivity.this, "Account creation failed", Toast.LENGTH_SHORT)
                                            .show();
                                }
                            }
                        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, e.toString());
                        if (e instanceof FirebaseAuthUserCollisionException) {
                            updateStatus("This email address is already in use.");
                        }
                        else {
                            updateStatus(e.getLocalizedMessage());
                        }
                    }
                });
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog, String phonenumber, String username) {
        Log.d("mattTag", "yonk");


        //todo: validate phonenumber
        boolean takenUsername = manager.isUsernameTaken(username);
        Log.d("mattTag", phonenumber);
        Log.d("mattTag", username);
        if (takenUsername){
            Toast.makeText(LoginPageActivity.this, "Username taken!", Toast.LENGTH_SHORT)
                    .show();
        }
        else{

            Log.d("mattTag", "yeas");
            manager.writeUsername(username);
            manager.writeUserPhone(phonenumber);
            CurrentUser.getInstance().setUsername(username);
            CurrentUser.getInstance().setAccountPhone(phonenumber);
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();

        }





    }

    public void onDestroy() {
        Log.d("mattTag", "destroyed haha");
        manager.setOnLoginPage(false);
        super.onDestroy();
    }


    public void promptInitialProfile(){
        DialogFragment dialog = new InitialUserProfileDialog();
        dialog.show(getSupportFragmentManager(), "InitialUserProfileDialog");

    }


    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        Log.d("mattTag", "yonkkk");
    }

    public void goToMainPage() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }
}

