package com.challenge.danny.rentmydriveway.ViewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.challenge.danny.rentmydriveway.Firebase.FirebaseQueryLiveData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Danny on 11/2/18.
 */

public class HostViewModel extends ViewModel {

    private static final DatabaseReference HOST_REFERENCE = FirebaseDatabase.getInstance().getReference("host");

    private final FirebaseQueryLiveData liveData = new FirebaseQueryLiveData(HOST_REFERENCE);

    @NonNull
    public LiveData<DataSnapshot> getDataSnapshotLiveData() {
        return liveData;
    }
}
