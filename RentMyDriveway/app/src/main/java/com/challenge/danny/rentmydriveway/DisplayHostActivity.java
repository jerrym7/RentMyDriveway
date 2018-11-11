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
    String TAG = "DisplayHostActivity";
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hostinfo_display);
        startWidgets();
        Bundle bundle = getIntent().getParcelableExtra("bundle");//get the marker information from intent
        hostLatLng = bundle.getParcelable("mLatLng");//get marker
        Log.d("DisplayHostActivity", hostLatLng.latitude + " , " + hostLatLng.longitude);
        listView = findViewById(R.id.list);
        dataModels = new ArrayList<>();
        getHostUid(hostLatLng);

    }


    private void getHostUid(LatLng hostLatLng) {
        final String compareLatLng = hostLatLng.latitude + " , " + hostLatLng.longitude;
        FirebaseDatabase.getInstance().getReference().child("host")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                            final String uid = childSnapshot.getKey();
                            final Host hostObject = childSnapshot.getValue(Host.class);
                            if (hostObject.getLatLngString().equals(compareLatLng)) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        getHostInfo(uid);
                                    }
                                });
                                return;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


    }

    /*
     * retrieving the host info (for ex. street address....)
     */
    private void getHostInfo(String uid) {
        hostUId = uid;
        if (hostUId == null) {
            return;
        } else {
            FirebaseDatabase.getInstance().getReference("host/" + hostUId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Host hostValue = dataSnapshot.getValue(Host.class);
                    host = hostValue;
                    Log.d(TAG, "Value is: " + hostValue);
                    //Toast.makeText(DisplayHostActivity.this,hostValue.getStreetName(),Toast.LENGTH_LONG).show();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            hostNameTextView.setText(host.getStreetName());
                            getHostReview();
                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
    }

    public void displayReviews(ArrayList<UserRating> hostReviewArrayList) {
        totalOfRatings = hostReviewArrayList.size();
        for (UserRating userRating : hostReviewArrayList) {
            dataModels.add(new UserRatingDataModel(userRating.getUserName(), userRating.getComment(), (int) userRating.getRating()));
        }
        adapter = new ReviewCustomAdapter(dataModels, getApplicationContext());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserRatingDataModel dataModel = dataModels.get(position);
                Toast.makeText(DisplayHostActivity.this, dataModel.getUsername(), Toast.LENGTH_SHORT).show();
            }


        });

    }
    public void displayReviews(UserRating userRating) {
        totalOfRatings = totalOfRatings+1;

            dataModels.add(new UserRatingDataModel(userRating.getUserName(), userRating.getComment(), (int) userRating.getRating()));
        adapter = new ReviewCustomAdapter(dataModels, getApplicationContext());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserRatingDataModel dataModel = dataModels.get(position);
                Toast.makeText(DisplayHostActivity.this, dataModel.getUsername(), Toast.LENGTH_SHORT).show();
            }


        });
    }

    /*
     * Method to start all layout buttons and widgets
     */

    public void startWidgets() {
        hostNameTextView = findViewById(R.id.host_name_textView);
        hostProfileImageView = findViewById(R.id.host_profile_imageView);
        hostDrivewayImageView = findViewById(R.id.host_house_imageView);
        hostRatingBar = findViewById(R.id.host_rate_ratingBar);
    }

    /*
     * Method to retrieve the reviews from the currentHost (Still working on it. This is the last missing part)
     */
    public void getHostReview() {
        final ArrayList<String> usersReviewUIDs = new ArrayList<String>();
        FirebaseDatabase.getInstance().getReference("reviews/" + hostUId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot datas : dataSnapshot.getChildren()) {
                    String userid = datas.getKey();
                    usersReviewUIDs.add(userid);
                    Toast.makeText(DisplayHostActivity.this, userid, Toast.LENGTH_SHORT).show();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getUserReview(usersReviewUIDs);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    /*
    * Method to retrieve all comments from users
     */
    private void getUserReview(final ArrayList<String> userReviewUids) {
        hostReviewArrayList = new ArrayList<UserRating>();
        if(userReviewUids == null||userReviewUids.size()==0){
            Toast.makeText(DisplayHostActivity.this,"Error getting uids",Toast.LENGTH_SHORT).show();
        }
        else{
            for(int i = 0;i<userReviewUids.size();i++){
                FirebaseDatabase.getInstance().getReference("reviews/"+hostUId+"/"+userReviewUids.get(i)).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        final UserRating userRating = dataSnapshot.getValue(UserRating.class);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                displayReviews(userRating);
                            }
                        });
                        hostReviewArrayList.add(userRating);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            //call the displayreview 


        }

    }
}
