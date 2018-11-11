package com.challenge.danny.rentmydriveway;

import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.challenge.danny.rentmydriveway.Firebase.FirebaseHelper;
import com.challenge.danny.rentmydriveway.Host.Host;
import com.challenge.danny.rentmydriveway.PickerWidget.TimePickerFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;


public class RentingOutActivity extends AppCompatActivity {

    private static final String TAG = "RentingOutActivity";

    EditText drivewayNNTextEdit;
    EditText addressTextEdit;
    EditText cityTextEdit;
    EditText zipCodeTextEdit;
    Spinner stateSpinner;
    Spinner countrySpinner;
    TextView startTimeTextView;
    TextView endTimeTextView;
    Button drivewayButton;
    Button submitButton;
    ArrayAdapter<String> statesArray;
    ArrayAdapter<String> countriesArray;
    private FirebaseHelper firebaseHelper;
    private String startTime = null;
    private String endTime = null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_renting_out);
        setWidgets();
        checkFirebaseAuthenticationState();
        sortStates();//sort and display the array adapter
        sortCountries();//sort and display the array adapter
        onButtonClick();
    }

    private void checkFirebaseAuthenticationState() {
        this.firebaseHelper = new FirebaseHelper();
        this.firebaseHelper.firebaseAuthentication();
        Log.d(TAG, "checkFirebaseAuthenticationState: " + firebaseHelper.getFirebaseAuth());
        checkFirebaseCurrentUser();
    }

    private void checkFirebaseCurrentUser() {
        firebaseHelper.currentUser();
        Log.d(TAG, "checkFirebaseCurrentUser: " + firebaseHelper.getFirebaseUser());
        addHostToDatabase();
    }

    private void addHostToDatabase() {
        this.firebaseHelper.addDatabaseInstance();
        this.firebaseHelper.addHostReferenceTable();
        FirebaseDatabase.getInstance().getReference().child("host").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "Value is: " + dataSnapshot.getValue());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "Failed to read value.", databaseError.toException());
            }
        });
        Log.d(TAG, "addDatabase: Firebase Database : " + firebaseHelper.getFirebaseDatabase());
        Log.d(TAG, "addDatabase: Firebase Database Reference : " + firebaseHelper.getDatabaseReference());
    }


    /*
        Function call for whenever startTextView, endTextView and submitButton is clicked
    */
    private void onButtonClick() {
        startTimeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new TimePickerFragment();
                ((TimePickerFragment) newFragment).setWordsOnStart("Start");
                ((TimePickerFragment) newFragment).setTextView(startTimeTextView);
                newFragment.show(getFragmentManager(),"Pick start time");
            }
        });

        //for end time
        endTimeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new TimePickerFragment();
                ((TimePickerFragment) newFragment).setWordsOnStart("End ");
                ((TimePickerFragment) newFragment).setTextView(endTimeTextView);
                newFragment.show(getFragmentManager(),"Pick end time");
            }
        });
        // when submit button is clicked
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkForInvalidInput();
            }
        });
    }

    /*
        Callback method from TimePickerFragment
        stores the time that the host chose
        @param currentTime - The current time
    */
    public void onTimeChanged(String hostTime, String wordOnStart) {
        if (wordOnStart.equalsIgnoreCase("Start")) {
            this.startTime = hostTime;
        } else {
            this.endTime = hostTime;
        }
    }

    //we sort the states in alphabetical order
    private void sortStates() {
        String[] arrayStates = getResources().getStringArray(R.array.us_states_string_array);
        List<String> states = Arrays.asList(arrayStates);
        statesArray = new ArrayAdapter<String>(RentingOutActivity.this,android.R.layout.simple_list_item_1,states);
        //sort alphabetical order
        statesArray.sort(new Comparator<String>() {
            @Override
            public int compare(String lhs, String rhs) {
                return lhs.compareTo(rhs);
            }
        });
        //insert in the spinner
        stateSpinner.setAdapter(statesArray);
        //switch color to white because it's difficult to see the selected state
        stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void sortCountries(){
        String[] arrayCountries = getResources().getStringArray(R.array.countries_string_array);
        List<String> countries = Arrays.asList(arrayCountries);
         countriesArray = new ArrayAdapter<String>(RentingOutActivity.this,android.R.layout.simple_list_item_1,countries);
        //sort alphabetical order
        countriesArray.sort(new Comparator<String>() {
            @Override
            public int compare(String lhs, String rhs) {
                return lhs.compareTo(rhs);
            }
        });
        //insert in the spinner
        countrySpinner.setAdapter(countriesArray);
        //switch color to white because it's difficult to see the selected state
        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setWidgets(){
        drivewayNNTextEdit = findViewById(R.id.driveway_nickname_textEdit);
        addressTextEdit = findViewById(R.id.address_textEdit);
        cityTextEdit = findViewById(R.id.city_textEdit);
        zipCodeTextEdit = findViewById(R.id.zip_code_textEdit);
        stateSpinner = findViewById(R.id.state_spinner);
        countrySpinner = findViewById(R.id.country_spinner);
        startTimeTextView = findViewById(R.id.start_time_textView);
        endTimeTextView = findViewById(R.id.end_time_TextView);
        drivewayButton = findViewById(R.id.driveway_picture_button);
        submitButton = findViewById(R.id.submit_button);
    }

    private void checkForInvalidInput() {
        if (addressTextEdit.getText().toString().trim().equalsIgnoreCase("") ||
                addressTextEdit.getText().toString().trim().isEmpty()) {
            this.addressTextEdit.requestFocus();
            this.addressTextEdit.setError("Please enter an address");
            /*
            this.addressTextEdit.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

                @Override
                public void afterTextChanged(Editable editable) {
                    if (addressTextEdit.getText().toString().length() <= 0) {
                        addressTextEdit.setError("Please enter an address");
                    } else {
                        addressTextEdit.setError(null);
                    }

                }
            });
            */
        }
        if (cityTextEdit.toString().trim().equalsIgnoreCase("") ||
                cityTextEdit.toString().trim().isEmpty()) {
            this.cityTextEdit.requestFocus();
            this.cityTextEdit.setError("Please enter a city");
            this.cityTextEdit.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

                @Override
                public void afterTextChanged(Editable editable) {
                    if (cityTextEdit.getText().toString().length() <= 0) {
                        cityTextEdit.setError("Please enter a city");
                    } else {
                        cityTextEdit.setError(null);
                    }
                }
            });
        }
        if (zipCodeTextEdit.toString().trim().equalsIgnoreCase("") ||
                zipCodeTextEdit.toString().trim().isEmpty()) {
            this.zipCodeTextEdit.setError("Please enter a zip code");
            this.zipCodeTextEdit.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (zipCodeTextEdit.getText().toString().length() <= 0) {
                        zipCodeTextEdit.setError("Please enter a zip code");
                    } else {
                        zipCodeTextEdit.setError(null);
                    }

                }
            });
        }
        if (stateSpinner.toString().trim().equalsIgnoreCase("") ||
                stateSpinner.toString().trim().isEmpty()) {
            TextView errorText = (TextView) this.stateSpinner.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);
            errorText.setText("Enter State");
        }
        if (countrySpinner.toString().trim().equalsIgnoreCase("") ||
                countrySpinner.toString().trim().isEmpty()) {
            TextView errorText = (TextView) this.countrySpinner.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);
            errorText.setText("Enter Country");
        }
        if (startTime == null) {
            startTimeTextView.setError("Enter start time");
        }
        if (endTime == null) {
            endTimeTextView.setError("Enter end time");
        }
        if ((!addressTextEdit.getText().toString().trim().equalsIgnoreCase("")) &&
                    (!addressTextEdit.getText().toString().trim().isEmpty()) &&
                    (!cityTextEdit.toString().trim().equalsIgnoreCase("")) &&
                    (!cityTextEdit.toString().trim().isEmpty()) &&
                    (!zipCodeTextEdit.toString().trim().equalsIgnoreCase("")) &&
                    (!zipCodeTextEdit.toString().trim().isEmpty()) &&
                    (!stateSpinner.toString().trim().equalsIgnoreCase("")) &&
                    (!stateSpinner.toString().trim().isEmpty()) &&
                    (!countrySpinner.toString().trim().equalsIgnoreCase("")) &&
                    (!countrySpinner.toString().trim().isEmpty()) &&
                    startTime != null && endTime != null) {
            convertAddressIntoCoordinates();
        }
    }

    private void convertAddressIntoCoordinates() {
        TextView stateTextView = (TextView) stateSpinner.getSelectedView();
        // Check for null inputs.
        if (addressTextEdit.getText().toString() != null &&
                cityTextEdit.getText().toString() != null &&
                zipCodeTextEdit.getText().toString() != null &&
                stateTextView.getText().toString() != null ) {
            String completeAddress = addressTextEdit.getText().toString() + cityTextEdit.getText().toString()
                                        + stateTextView.getText().toString() + zipCodeTextEdit.getText().toString();
            List<Address> addressList = new ArrayList<>();
            Geocoder geocoder = new Geocoder(RentingOutActivity.this);
            try {
                addressList = geocoder.getFromLocationName(completeAddress, 1);
            } catch (IOException e) {
                Log.e(TAG, "IOException : " + e.getMessage());
                Toast.makeText(this, "IOException error. Please modify address", Toast.LENGTH_SHORT).show();
            }
            storeHostDataToFirebase(addressList.get(0).getLatitude(),
                    addressList.get(0).getLongitude());
        }
    }

    private void storeHostDataToFirebase(double latitude, double longitude) {
        String userID = firebaseHelper.getFirebaseUser().getUid();
        Host host = new Host(addressTextEdit.getText().toString().trim(),
                cityTextEdit.getText().toString().trim(),
                stateSpinner.getSelectedItem().toString().trim(),
                countrySpinner.getSelectedItem().toString().trim(),
                Integer.parseInt(zipCodeTextEdit.getText().toString().trim()),
                startTime, endTime, latitude, longitude);
        FirebaseDatabase.getInstance().getReference().child("host").child(userID).setValue(host);
        //Log.d(TAG, "Host Firebase Database :" + firebaseHelper.getDatabaseReference().getDatabase());
        Intent intent = new Intent(RentingOutActivity.this, HostMenuActivity.class);
        startActivity(intent);
        this.finish();
    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(),"Pick the time");
    }
}
