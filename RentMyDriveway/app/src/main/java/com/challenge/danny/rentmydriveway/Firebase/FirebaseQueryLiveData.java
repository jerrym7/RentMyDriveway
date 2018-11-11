package com.challenge.danny.rentmydriveway.Firebase;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Danny on 11/2/18.
 */

public class FirebaseQueryLiveData extends LiveData<DataSnapshot> {

    private static final String TAG = "FirebaseQueryLiveData";

    private Query query;
    private MyValueEventListener listener = new MyValueEventListener();

    public FirebaseQueryLiveData(DatabaseReference ref) {
        this.query = ref;
    }

    @Override
    protected void onActive() {
        super.onActive();
        Log.d(TAG, "onActive");
        query.addValueEventListener(listener);

    }

    @Override
    protected void onInactive() {
        super.onInactive();
        Log.d(TAG, "onInactive");
        query.removeEventListener(listener);
    }

    private class MyValueEventListener implements ValueEventListener {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            setValue(dataSnapshot);
            Log.d(TAG, "DataSnapshot onDataChange: " + dataSnapshot);

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.e(TAG, "DataSnapshot onCancelled: " + databaseError);

        }
    }
}
