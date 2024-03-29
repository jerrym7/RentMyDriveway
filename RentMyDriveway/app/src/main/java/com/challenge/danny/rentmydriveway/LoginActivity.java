package com.challenge.danny.rentmydriveway;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.challenge.danny.rentmydriveway.Firebase.FirebaseHelper;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private EditText emailEditText;
    private EditText passwordEditText;
    private TextView registerTextView;
    private Button loginButton;
    private FirebaseHelper firebaseHelper;
    FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        registerTextView = findViewById(R.id.register_textView);
        FirebaseApp.initializeApp(this); //Initialize the firebase app
        checkFirebaseAuthenticationState();
        buttonClicked();
        //auto log in
        auth = FirebaseAuth.getInstance();
        if(auth.getCurrentUser() == null){
        }
        else{
            emailEditText.setText(auth.getCurrentUser().getEmail());
        }
    }

    private void checkFirebaseAuthenticationState() {
        firebaseHelper = new FirebaseHelper();
        firebaseHelper.firebaseAuthentication();
        firebaseHelper.getFirebaseAuth();
        //checkFirebaseCurrentUser();
    }

    private void checkFirebaseCurrentUser() {
        firebaseHelper.currentUser();
        if (firebaseHelper.getFirebaseUser() != null) {
            startActivity(new Intent(this,ChooseServiceActivity.class));    //if it does exits go to the nexxt activity
        } else {
            Toast.makeText(LoginActivity.this, "Unable to retrieve current user", Toast.LENGTH_SHORT).show();
        }
    }

    public void buttonClicked() {
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = emailEditText.getText().toString().trim();
                final String password = passwordEditText.getText().toString().trim();

                if (email.equals("") || email.isEmpty()) {
                    Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
                    emailEditText.startAnimation(shake);
                    Toast.makeText(LoginActivity.this, "Email is empty. Try again", Toast.LENGTH_SHORT).show();
                }
                if (password.equals("") || password.isEmpty()) {
                    Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
                    passwordEditText.startAnimation(shake);
                    Toast.makeText(LoginActivity.this, "Password is empty. Try again", Toast.LENGTH_SHORT).show();
                }
                if (!email.isEmpty() && !password.isEmpty()) {
                    loginUser(email,password);
                }
            }
        });

        //when they click the text view riderect user to register layout
        registerTextView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });
    }

    private void loginUser(final String email, String password){
       FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
           @Override
           public void onSuccess(AuthResult authResult) {
               startActivity(new Intent(LoginActivity.this,ChooseServiceActivity.class));
           }
       }).addOnFailureListener(new OnFailureListener() {
           @Override
           public void onFailure(@NonNull Exception e) {
               Toast.makeText(LoginActivity.this,"Something went wrong... :(",Toast.LENGTH_SHORT).show();
           }
       });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
