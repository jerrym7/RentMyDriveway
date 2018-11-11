package com.challenge.danny.rentmydriveway;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.challenge.danny.rentmydriveway.Firebase.FirebaseHelper;
import com.challenge.danny.rentmydriveway.Host.Host;
import com.challenge.danny.rentmydriveway.UserInformation.User;
import com.challenge.danny.rentmydriveway.UserInformation.UserRating;
import com.challenge.danny.rentmydriveway.UserInformation.UserRatingDataModel;
import com.challenge.danny.rentmydriveway.Utilities.ReviewCustomAdapter;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class DisplayHostActivity extends AppCompatActivity {
    ArrayList<UserRatingDataModel> dataModels;
    ListView listView;
    private TextView hostNameTextView;
    private CircleImageView hostProfileImageView;
    private ImageView hostDrivewayImageView;
    private RatingBar hostRatingBar;
    private ReviewCustomAdapter adapter;
    private DatabaseReference ref;
    private Host host;
    private String latLngString;
    private LatLng hostLatLng;
    private String hostUId;
    private double sumOfRatings = 0; //total stars of all users
    private int totalOfRatings = 0;//default at no people rated
    private ArrayList<UserRating> hostReviewArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hostinfo_display);
        startWidgets();
        Bundle bundle = getIntent().getParcelableExtra("bundle");//get the marker information from intent
        hostLatLng = bundle.getParcelable("mLatLng");//get marker
        Log.d("DisplayHostActivity",hostLatLng.latitude+" , " +hostLatLng.longitude);
        getHostUid(hostLatLng);
        listView = findViewById(R.id.list);
        dataModels = new ArrayList<>();
        adapter = new ReviewCustomAdapter(dataModels,getApplicationContext());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,long id){
                UserRatingDataModel dataModel = dataModels.get(position);
                Toast.makeText(DisplayHostActivity.this,dataModel.getUsername(),Toast.LENGTH_SHORT).show();
            }


        });
    }

    private void getHostUid(LatLng hostLatLng) {
        String compareLatLng = hostLatLng.latitude+" , "+hostLatLng.longitude;
        FirebaseDatabase.getInstance().getReference().child("host").orderByChild("latLngString").equalTo(compareLatLng)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        System.out.println(dataSnapshot.getKey());
                        hostUId = dataSnapshot.getKey().trim();
                        if(hostUId.equals("") || hostUId.isEmpty()){
                            Toast.makeText(DisplayHostActivity.this,"Error loading host information...",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            hostNameTextView.setText(hostUId);
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
        FirebaseDatabase.getInstance().getReference().child("host").equalTo(hostUId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                host = (Host) dataSnapshot.getValue(Host.class);
                Log.d("hostInHostReview",host.getStreetName());
                if(host==null){
                    Toast.makeText(DisplayHostActivity.this,"Error finding user...",Toast.LENGTH_SHORT).show();
                }
                else{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            hostNameTextView.setText(host.getStreetName());

                        }
                    });
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
        getHostReview();

    }

    public void displayReviews(ArrayList<UserRating> hostReviewArrayList){
        totalOfRatings = hostReviewArrayList.size();
        for(UserRating userRating : hostReviewArrayList) {
            dataModels.add(new UserRatingDataModel(userRating.getUserName(), userRating.getComment(),(int)userRating.getRating()));
        }

    }

    /*
    * Method to display host information
    */
    //private void displayHostInfo() {

    //}

    public void startWidgets(){
        hostNameTextView = findViewById(R.id.host_name_textView);
        hostProfileImageView = findViewById(R.id.host_profile_imageView);
        hostDrivewayImageView = findViewById(R.id.host_house_imageView);
        hostRatingBar = findViewById(R.id.host_rate_ratingBar);
    }

    public void getHostReview(){
        final ArrayList<String> usersReviewUIDs = new ArrayList<String>();
        ref = FirebaseDatabase.getInstance().getReference();
        ref.child("reviews").equalTo(hostUId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();

                for(DataSnapshot child: children) {
                     usersReviewUIDs.add(child.getKey());
                }
                if(usersReviewUIDs == null || usersReviewUIDs.size() == 0){
                    Toast.makeText(DisplayHostActivity.this,"No reviews",Toast.LENGTH_SHORT).show();
                }
                else{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            getUserReview(usersReviewUIDs);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private void getUserReview(final ArrayList<String> userReviewUids){
        hostReviewArrayList = new ArrayList<UserRating>();
            FirebaseDatabase.getInstance().getReference().child("reviews").child(hostUId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                    int index = 0;

                    for(DataSnapshot child: children) {
                        hostReviewArrayList.add(child.child(userReviewUids.get(index)).getValue(UserRating.class));
                        index++;
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        if(hostReviewArrayList == null || hostReviewArrayList.size() == 0){
            Toast.makeText(DisplayHostActivity.this,"Error loading comments",Toast.LENGTH_SHORT).show();
        }
        else{
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    displayReviews(hostReviewArrayList);
                }
            });
        }
    }

}
