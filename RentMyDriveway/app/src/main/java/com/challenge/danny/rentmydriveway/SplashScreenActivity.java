package com.challenge.danny.rentmydriveway;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class SplashScreenActivity extends AppCompatActivity {

    private static final String TAG = "SplashScreenActivity";

    private static final int ERROR_DIALOG_REQUEST = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        TextView logoTextView = findViewById(R.id.logoTextView);
        FrameLayout whiteFrameLayout =  findViewById(R.id.whiteBackground);
        FrameLayout blueFrameLayout = findViewById(R.id.blueBackground);
        Animation splash_animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        whiteFrameLayout.setVisibility(View.INVISIBLE);
        blueFrameLayout.startAnimation(splash_animation);
        addFadeInAnimation(whiteFrameLayout, splash_animation);

        isGooglePlayServicesOkay();
    }

    public boolean isGooglePlayServicesOkay() {
        Log.d(TAG, "isGooglePlayServicesOkay: Checking Google Play Services");
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(SplashScreenActivity.this);
        if (available == ConnectionResult.SUCCESS) {
            // user can make map request
            Log.d(TAG, "isGooglePlayServicesOkay: Google Play Services is working");
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            // an error occured but we can resolve it
            Log.d(TAG, "isGooglePlayServicesOkay: An error occured but we can solve it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(SplashScreenActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        } else {
            Toast.makeText(SplashScreenActivity.this, "You cannot make a map request", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    public void addFadeInAnimation(final FrameLayout whiteFrameLayout, Animation splash_animation) {
        splash_animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                whiteFrameLayout.setVisibility(View.VISIBLE);
                Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
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
