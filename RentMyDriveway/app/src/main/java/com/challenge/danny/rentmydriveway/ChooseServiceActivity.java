package com.challenge.danny.rentmydriveway;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.challenge.danny.rentmydriveway.Firebase.FirebaseHelper;
import com.challenge.danny.rentmydriveway.Service.ServiceEntity;
import com.google.firebase.auth.FirebaseAuth;

public class ChooseServiceActivity extends AppCompatActivity {

    private static final String TAG = "ChooseServiceActivity";

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private Toolbar cToolBar;
    private FirebaseHelper firebaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_service);
        setToggleBar(); //set the three dots button(toggle button) and side bar
        Button lookingForParkingButton = (Button) findViewById(R.id.lookingForParkingButton);
        Snackbar.make((View)findViewById(android.R.id.content),"Welcome "+                  //welcome the user!
                        FirebaseAuth.getInstance().getCurrentUser().getEmail()+"!",
                Snackbar.LENGTH_SHORT).show();
        Button rentingOutYourDrivewayButton = (Button) findViewById(R.id.rentingOutYourDrivewayButton);
        checkFirebaseAuthenticationState();
        onButtonsClicked(lookingForParkingButton, rentingOutYourDrivewayButton);

    }

    private void checkFirebaseAuthenticationState() {
        this.firebaseHelper = new FirebaseHelper();
        this.firebaseHelper.firebaseAuthentication();
        Log.d(TAG, "checkFirebaseAuthenticationState: " + firebaseHelper.getFirebaseAuth());
        checkFirebaseCurrentUser();
    }

    private void checkFirebaseCurrentUser() {
        firebaseHelper.currentUser();
        addServiceDatabase();
    }

    private void addServiceDatabase() {
        this.firebaseHelper.addDatabaseInstance();
        this.firebaseHelper.addServiceReferenceTable();
        this.firebaseHelper.readDatabase();
        Log.d(TAG, "addDatabase: Firebase Database : " + firebaseHelper.getFirebaseDatabase());
        Log.d(TAG, "addDatabase: Firebase Database Reference : " + firebaseHelper.getDatabaseReference());
    }

    private void storeServiceData(String buttonText) {
        String userID = firebaseHelper.getFirebaseAuth().getInstance().getCurrentUser().getUid();//fix for crashing in API 28 emulator (please don't remove)
        if (buttonText.equalsIgnoreCase("Looking for Parking")) {
            ServiceEntity serviceEntity = new ServiceEntity(true, false);
            this.firebaseHelper.getDatabaseReference().child(userID).setValue(serviceEntity);
        } else {
            ServiceEntity serviceEntity = new ServiceEntity(false, true);
            this.firebaseHelper.getDatabaseReference().child(userID).setValue(serviceEntity);
        }
    }

    public void onButtonsClicked(final Button lookingForParkingButton, final Button rentingOutYourDrivewayButton) {
        lookingForParkingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storeServiceData(lookingForParkingButton.getText().toString().trim());
                Intent intent = new Intent(ChooseServiceActivity.this, GoogleMapActivity.class);
                startActivity(intent);

            }
        });

        rentingOutYourDrivewayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storeServiceData(rentingOutYourDrivewayButton.getText().toString().trim());
                Intent intent = new Intent(ChooseServiceActivity.this, RentingOutActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    /*
        Set side bar navigation, and have them each item have an action to do.
    */
    private void setToggleBar() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(this,mDrawerLayout,R.string.open,R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //add the navigation(side bar) and have a swtitch statement to do each thing on it.
        NavigationView mNavigationView = findViewById(R.id.nav_bar);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem){
                Intent i;
                switch (menuItem.getItemId()){
                    case(R.id.my_account_mipmap):
                        Toast.makeText(ChooseServiceActivity.this, "Account Selected", Toast.LENGTH_SHORT).show();
                        return true;
                    case(R.id.my_orders_mipmap):
                        Toast.makeText(ChooseServiceActivity.this, "Orders", Toast.LENGTH_SHORT).show();
                        return true;
                    case(R.id.map_mipmap):
                        Toast.makeText(ChooseServiceActivity.this, "Maps", Toast.LENGTH_SHORT).show();
                        i = new Intent(ChooseServiceActivity.this,GoogleMapActivity.class);
                        startActivity(i);
                        return true;
                    case(R.id.parking_mipmap):
                        Toast.makeText(ChooseServiceActivity.this, "Request", Toast.LENGTH_SHORT).show();
                        return true;
                    case(R.id.qr_scan_mipmap):
                        Toast.makeText(ChooseServiceActivity.this, "Scan QR", Toast.LENGTH_SHORT).show();
                        i = new Intent(ChooseServiceActivity.this,ScannerActivity.class);
                        startActivity(i);
                        return true;
                    case(R.id.qr_generate_mipmap):
                        Toast.makeText(ChooseServiceActivity.this, "Generate QR", Toast.LENGTH_SHORT).show();
                        i = new Intent(ChooseServiceActivity.this,QRActivity.class);
                        startActivity(i);
                        return true;
                    case(R.id.settings_mipmap):
                        Toast.makeText(ChooseServiceActivity.this, "Settings", Toast.LENGTH_SHORT).show();
                        return true;
                    case(R.id.about_mipmap):
                        startActivity(new Intent(ChooseServiceActivity.this,DisplayHostActivity.class));
                        Toast.makeText(ChooseServiceActivity.this, DisplayHostActivity.class.getName(), Toast.LENGTH_SHORT).show();
                        return true;
                    default:return true;
                }
            }
        });
    }


    /*
        When the three dots button has been touch open
    */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
