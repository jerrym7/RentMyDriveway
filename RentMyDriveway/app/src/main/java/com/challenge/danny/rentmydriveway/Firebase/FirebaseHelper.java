package com.challenge.danny.rentmydriveway.Firebase;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.challenge.danny.rentmydriveway.Host.Host;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by Danny on 10/1/18.
 * Firebase Helper will be used to get or post data to firebase real time cloud service.
 */

public class FirebaseHelper {
    private static final String TAG = "FirebaseHelper";

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private DatabaseReference hostReference;
    private boolean isRegistered;

    public FirebaseHelper() {}

    /*
        Stores Firebase Authentication
    */
    public void firebaseAuthentication() {
        this.firebaseAuth = FirebaseAuth.getInstance();
    }

    /*
        Stores Current User
    */
    public void currentUser() {
         this.firebaseUser = firebaseAuth.getCurrentUser();
    }

    /*
        Stores Database Instance
    */
    public void addDatabaseInstance() {
        firebaseDatabase = FirebaseDatabase.getInstance();
    }

    /*
        Get USERS Table from Firebase Database
    */
    public void addUsersReferenceTable() {
        databaseReference = firebaseDatabase.getReference("users");
    }

    /*
        Get LOCATIONS Table from Firebase Database
    */
    public void addLocationsReferenceTable() {
        databaseReference = firebaseDatabase.getReference("locations");
    }

    /*
        Get HOST Table from Firebase Database
    */
    public void addHostReferenceTable() {
        hostReference = firebaseDatabase.getReference("host");
    }

    /*
        Get SERVICE Table from Firebase Database
    */
    public void addServiceReferenceTable() {
        databaseReference = firebaseDatabase.getReference("service");
    }
    /*
    *   Get REVIEW table from firebase database
    */
    public void addReviewReferenceTable(){
        databaseReference = firebaseDatabase.getReference("reviews");
    }

    public void readDatabase() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "Value is: " + dataSnapshot.getValue());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "Failed to read value.", databaseError.toException());
            }
        });
    }

    public ArrayList<Host> retrieveHostData() {
        final ArrayList<Host> host = new ArrayList<>();
        //hostReference = FirebaseDatabase.getInstance().getReference().child("host");
        hostReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                for(DataSnapshot ds: dataSnapshot.getChildren()) {
                    Log.d(TAG, "onChildAdded: Values " + ds.toString());
                    Log.d(TAG, "onChildAdded: Host Key : " + ds.getKey());
                    Log.d(TAG, "onChildAdded Host child: " + ds.getChildren());
                    Host hostValue = ds.child("host").getValue(Host.class);
                    host.add(hostValue);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return host;
    }


    /*
    *   Signs in user authentication using Firebase
    *   @param email - User's email
    *   @param password - User's password
    *   @return - if sign in was succesful or not successful
    */
    public boolean signInUserWithEmailAndPassword(String email, String password) {
        final boolean[] isSuccessful = {false};
        this.firebaseAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithEmail: isSuccessful:" + task.isSuccessful());
                            isSuccessful[0] = true;
                        } else {
                            Log.e(TAG, "signInWithEmail: failed", task.getException());
                            isSuccessful[0] = false;
                        }
                    }
                });
        return isSuccessful[0];
    }

    /*
    *   Creates a network call to firebase and create user and password
    *   @parm email - User's email
    *   @param password - User's password
    *   @return - check if account was successfully created
     */
    public void createUserWithEmailAndPassword(String email, String password) {
        this.firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "createUserWithEmailAndPassword: is Successful: " + task.isSuccessful());
                            isRegistered = true;
                        } else {
                            Log.e(TAG, "signInWithEmail: failed", task.getException());
                            isRegistered = false;
                        }
                    }
                });
    }

    public boolean getIsRegistered() {
        return isRegistered;
    }

    public FirebaseAuth getFirebaseAuth() {
        return firebaseAuth;
    }

    public FirebaseUser getFirebaseUser() {
        return firebaseUser;
    }

    public FirebaseDatabase getFirebaseDatabase() {
        return firebaseDatabase;
    }

    public DatabaseReference getDatabaseReference() {
        return databaseReference;
    }

    public DatabaseReference getHostReference() {
        return hostReference;
    }
}
