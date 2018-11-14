package com.challenge.danny.rentmydriveway;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.challenge.danny.rentmydriveway.Firebase.FirebaseHelper;
import com.challenge.danny.rentmydriveway.Host.Host;
import com.challenge.danny.rentmydriveway.UserInformation.UserLocation;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GoogleMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = "GoogleMapActivity";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 30;
    private static final float DEFAULT_ZOOM = 75f;

    private boolean locationPermissionGranted = false;
    private GoogleMap googleMap;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private FirebaseHelper firebaseHelper;
    private MarkerOptions markerOptions;
    private ArrayList<LatLng> latLngs;
    private int clickCounter;
    private ArrayList<Host> hostArrayList;
    private Marker previousMarker = null; //for clicking twice a marker

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_map);
        clickCounter = 0;
        /*
        HostViewModel hostViewModel = ViewModelProviders.of(this).get(HostViewModel.class);
        LiveData<DataSnapshot> liveData = hostViewModel.getDataSnapshotLiveData();
        liveData.observe(this, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(@Nullable DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    // Update UI when data is changing
                }
            }
        });
        */

        getLocationPermission();
        checkFirebaseAuthenticationState();

    }

    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /*
        Function gets called when map is ready to be displayed
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        if (locationPermissionGranted) {
            renderDeviceLocation();
            if (ContextCompat.checkSelfPermission(GoogleMapActivity.this, FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(GoogleMapActivity.this, COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            fetchHostDataFromFirebase();
            this.googleMap.setMyLocationEnabled(true);
        }
    }

    /*
        We first ask user to accept their location permission
    */
    private void getLocationPermission() {
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        if (ContextCompat.checkSelfPermission(GoogleMapActivity.this, FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(GoogleMapActivity.this, COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
            initMap();
        } else {
            ActivityCompat.requestPermissions(GoogleMapActivity.this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    private void renderDeviceLocation() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try {
            if (locationPermissionGranted) {
                Task location = fusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Location currentLocation = (Location) task.getResult();
                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), DEFAULT_ZOOM);
                            storeUserLocation(currentLocation);

                        } else {
                            Toast.makeText(GoogleMapActivity.this, "Unable to retrieve current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        } catch (SecurityException e) {
            Log.e(TAG, "getDeviceLocation: SecurityException " + e.getMessage());
        }
    }

    private void moveCamera(LatLng latLng, float zoom) {
        this.googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    /*
        Callback to requesting permissions when user denies.
        This method is invoked for every call on requestPermissions(String[], int).
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        locationPermissionGranted = false;
        switch(requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int i=0; i<grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            locationPermissionGranted = false;
                            getLocationPermission();
                            return;
                        }
                    }
                    locationPermissionGranted = true;
                    initMap();
                }
            }
        }
    }

    private void fetchHostDataFromFirebase() {
        this.firebaseHelper.addHostReferenceTable();
        getListOfHosts();
    }

    private void getListOfHosts() {
        hostArrayList = new ArrayList<>();
        firebaseHelper.getHostReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();

                for(DataSnapshot child: children) {
                    Host host = child.getValue(Host.class);
                    hostArrayList.add(host);
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        displayMarkerToGoogleMapUI(hostArrayList);
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void displayMarkerToGoogleMapUI(ArrayList<Host> host) {
        this.latLngs = new ArrayList<>();
        if(host == null || host.size() == 0){
            Toast.makeText(GoogleMapActivity.this,"Error loading...",Toast.LENGTH_SHORT).show();
            return;
        }
        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.parking_map_marker);
        this.markerOptions = new MarkerOptions();
        for (Host hosts: host) {
            Log.d(TAG, "displayMarkerToGoogleMapUI: Longitude" + hosts.getLongitude() + " Latitude " + hosts.getLatitude());
            this.latLngs.add(new LatLng(hosts.getLatitude(), hosts.getLongitude()));
            markerOptions.position(new LatLng(hosts.getLatitude(), hosts.getLongitude()));
            markerOptions.title(hosts.getFullAddress());
            markerOptions.icon(icon);
            googleMap.addMarker(markerOptions);
        }
       /* for (LatLng coordinates: latLngs) {
            markerOptions.position(coordinates);
            markerOptions.title("Lat/Lng " + coordinates);
            googleMap.addMarker(markerOptions);
            //moveCamera(coordinates, 120);
        }*/
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if(previousMarker == null){
                    previousMarker = marker;
                    clickCounter = clickCounter+1;
                    marker.showInfoWindow();
                    return  false;
                }
                if(!previousMarker.getPosition().toString().equals(marker.getPosition().toString())){
                    clickCounter = 0;
                    marker.showInfoWindow();
                    clickCounter = clickCounter +1;
                    previousMarker = marker;
                    return false;
                }
                else{
                    if(clickCounter >= 0){//check if double clicked
                        marker.hideInfoWindow();
                        clickCounter = 0;
                        Intent i = new Intent(GoogleMapActivity.this,DisplayHostActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("mLatLng",marker.getPosition());
                        i.putExtra("bundle",bundle);
                        startActivity(i);
                    }

                    marker.showInfoWindow();
                    clickCounter +=1;
                }


                return false;
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        clickCounter = 0;//restart the counter
    }

    @Override
    protected void onResume() {
        super.onResume();
        clickCounter = 0;//restart the counter
    }

    private void checkFirebaseAuthenticationState() {
        this.firebaseHelper = new FirebaseHelper();
        this.firebaseHelper.firebaseAuthentication();
        Log.d(TAG, "checkFirebaseAuthenticationState: " + firebaseHelper.getFirebaseAuth());
        checkFirebaseCurrentUser();
    }

    private void checkFirebaseCurrentUser() {
        firebaseHelper.currentUser();
        addLocationDatabase();
    }

    private void addLocationDatabase() {
        this.firebaseHelper.addDatabaseInstance();
        this.firebaseHelper.addLocationsReferenceTable();
        this.firebaseHelper.readDatabase();
        Log.d(TAG, "addDatabase: Firebase Database : " + firebaseHelper.getFirebaseDatabase());
        Log.d(TAG, "addDatabase: Firebase Database Reference : " + firebaseHelper.getDatabaseReference());
    }

    private void storeUserLocation(Location location) {
        String userID = firebaseHelper.getFirebaseUser().getUid().trim();
        UserLocation userLocation = new UserLocation(userID, location.getLatitude(), location.getLongitude());
        this.firebaseHelper.getDatabaseReference().child(userID).setValue(userLocation);
        Log.d(TAG, "User Location Firebase Database :" + firebaseHelper.getDatabaseReference().getDatabase());
    }
}
