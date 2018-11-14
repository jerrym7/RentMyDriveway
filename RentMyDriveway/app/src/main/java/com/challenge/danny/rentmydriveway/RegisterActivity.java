package com.challenge.danny.rentmydriveway;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.challenge.danny.rentmydriveway.Firebase.FirebaseHelper;
import com.challenge.danny.rentmydriveway.UserInformation.User;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "RegisterActivity";
    private EditText currentFullNameEditText;
    private EditText currentEmailEditText;
    private EditText currentpasswordEditText;
    private Button registerButton;
    private TextView logInTextView;
    private ScrollView registerScrollView;
    private FirebaseHelper firebaseHelper;
    private ProgressBar registerProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        registerScrollView = findViewById(R.id.register_scrollView);
        currentFullNameEditText = findViewById(R.id.input_name);
        currentEmailEditText = findViewById(R.id.input_email);
        currentpasswordEditText = findViewById(R.id.input_password);
        logInTextView = findViewById(R.id.login_textView);
        registerButton = findViewById(R.id.registerButton);

        FirebaseApp.initializeApp(this);//Initialize the firebase app
        checkFirebaseAuthenticationState();
        registerClicked();
    }

    private void checkFirebaseAuthenticationState() {
        this.firebaseHelper = new FirebaseHelper();
        this.firebaseHelper.firebaseAuthentication();
        Log.d(TAG, "checkFirebaseAuthenticationState: " + firebaseHelper.getFirebaseAuth());
        checkFirebaseCurrentUser();
    }

    private void checkFirebaseCurrentUser() {
        firebaseHelper.currentUser();
        Log.d(TAG, "checkFirebaseCurrentUser: " + firebaseHelper.getFirebaseUser());
        addDatabase();
//        if (firebaseHelper.getFirebaseUser() != null) {
//            startActivity(new Intent(this,ChooseServiceActivity.class));    //if it does exits go to the nexxt activity
//        } else {
//            Toast.makeText(RegisterActivity.this, "Unable to retrieve current user", Toast.LENGTH_SHORT).show();
//        }
    }

    private void addDatabase() {
        this.firebaseHelper.addDatabaseInstance();
        this.firebaseHelper.addUsersReferenceTable();
        this.firebaseHelper.readDatabase();
        Log.d(TAG, "addDatabase: Firebase Database : " + firebaseHelper.getFirebaseDatabase());
        Log.d(TAG, "addDatabase: Firebase Database Reference : " + firebaseHelper.getDatabaseReference());
    }

    public void registerClicked() {
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser(currentFullNameEditText.getText().toString().trim(),
                        currentEmailEditText.getText().toString().trim(),
                        currentpasswordEditText.getText().toString().trim());
            }
        });
        logInTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
            }
        });

    }

    private void registerUser(final String currentName, final String currentEmail, String currentPassword) {
        if (currentEmail.isEmpty() || currentEmail.equals("")) {//the current email doesn't have anything
            Toast.makeText(this, "Enter an email", Toast.LENGTH_SHORT).show();
        } else if (currentPassword.isEmpty() || currentPassword.equals("")) {
            Toast.makeText(this, "Enter a password", Toast.LENGTH_SHORT).show();
        } else if (currentName.equals("") || currentName.isEmpty()) {
            Toast.makeText(this, "Enter your full name", Toast.LENGTH_SHORT).show();
        } else {
            firebaseHelper.createUserWithEmailAndPassword(currentEmail, currentPassword);
            registerProgressBar = findViewById(R.id.register_progress_bar);
            registerProgressBar.setVisibility(View.VISIBLE);
            Thread thread = new Thread() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(3000);//wait three seconds
                    } catch (InterruptedException e) {
                    }

                    runOnUiThread(new Runnable() {//run on main thread
                        @Override
                        public void run() {
                            // Do some stuff
                            registerProgressBar.setVisibility(View.GONE);
                            sendToFireBase(currentName,currentEmail,firebaseHelper.getIsRegistered());
                        }
                    });
                }
            };
            thread.start(); //start the thread

        }
    }
    private void sendToFireBase(String currentName,String currentEmail,boolean isRegister){
        if(isRegister == true) {
            User currentUser = new User(currentName, currentEmail);
            String userID = firebaseHelper.getFirebaseAuth().getCurrentUser().getUid().trim();
            Log.d(TAG, "registerUser: User ID: " + userID);
            firebaseHelper.getDatabaseReference().child(userID).setValue(currentUser);
            Log.d(TAG, "registerUser: Firebase Database :" + firebaseHelper.getDatabaseReference().getDatabase());
            Snackbar.make(registerScrollView,"Welcome "+currentEmail +"!",Snackbar.LENGTH_SHORT).show();
            Intent intent = new Intent(RegisterActivity.this, ChooseServiceActivity.class);
            startActivity(intent);
            RegisterActivity.this.finish();
        }
        //it's not registred
        else{
            Toast.makeText(this,"Something went wrong",Toast.LENGTH_SHORT).show();
        }
    }

    private void updateUI(FirebaseUser currentUser){
        if(currentUser!=null) {
            Snackbar.make(registerScrollView,"Welcome "+currentUser.getEmail(),Snackbar.LENGTH_SHORT).show();
            startActivity(new Intent(this,ChooseServiceActivity.class));
        }
        else
            return;
    }
}
