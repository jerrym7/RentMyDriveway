package com.challenge.danny.rentmydriveway;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.challenge.danny.rentmydriveway.Utilities.BottomNavigationViewHelper;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class HostMenuActivity extends AppCompatActivity {

    private static final String TAG = "HostMenuActivity";
    private static final int BOTTOM_NAVIGATION_POSITION = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_menu);
        setBottomNavigationView();
    }

    private void setBottomNavigationView() {
        Log.d(TAG, "setBottomNavigationView: Setting up Bottom Navigation View");
        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.bottomNavigationView);
        BottomNavigationViewHelper.setUpBottomNavigationViewAttributes(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(HostMenuActivity.this, bottomNavigationViewEx);

        MenuItem menuItem = bottomNavigationViewEx.getMenu().getItem(BOTTOM_NAVIGATION_POSITION);
        menuItem.setChecked(true);
    }
}
