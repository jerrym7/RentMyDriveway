package com.challenge.danny.rentmydriveway.Utilities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;

import com.challenge.danny.rentmydriveway.Firebase.FirebaseHelper;
import com.challenge.danny.rentmydriveway.LoginActivity;
import com.challenge.danny.rentmydriveway.R;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

/**
 * Created by Danny on 10/28/18.
 */

public class BottomNavigationViewHelper {

    private static final String TAG = "BottomNavigationViewHelper";
    private static FirebaseHelper firebaseHelper;

    public static void setUpBottomNavigationViewAttributes(BottomNavigationViewEx bottomNavigationViewEx) {
        bottomNavigationViewEx.enableAnimation(true);
        bottomNavigationViewEx.enableItemShiftingMode(false);
        bottomNavigationViewEx.enableShiftingMode(false);
    }

    public static void enableNavigation(final Context context, BottomNavigationViewEx view) {
        view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.informationItem:
                        break;
                    case R.id.settingsItem:
                        break;
                    case R.id.signOutItem:
                        firebaseHelper = new FirebaseHelper();
                        firebaseHelper.firebaseAuthentication();
                        firebaseHelper.currentUser();
                        firebaseHelper.getFirebaseAuth().signOut();
                        Intent intent = new Intent(context, LoginActivity.class);
                        (context).startActivity(intent);
                        break;
                }
                return false;
            }
        });
    }
}
