package com.challenge.danny.rentmydriveway.GoogleMaps;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MarkerHelper {
    private String title;
    private Marker mmarker;
    private String address;
    private LatLng mlatlng;
    private Context context;
    //add title
    public MarkerHelper(String title, String address, Context context){
        this.title = title;
        this.address = address;
        this.context = context;
        mlatlng = getLatLng();
    }
    public void addMarker(GoogleMap map, String title){
        mmarker = map.addMarker(new MarkerOptions()
                .position(mlatlng)
                .title(title));
        mmarker.setTag(0);
    }
    public void setTag(String tag){
        mmarker.setTag(0);
    }

    public void setVisibility(boolean visibility){
         mmarker.setVisible(visibility);
    }
    public void addSnippet(GoogleMap map, String snippet){
        if(mmarker != null) {
            mmarker.remove();
        }
        mmarker = map.addMarker(new MarkerOptions()
                .position(mlatlng)
                .title(title)
                .snippet(snippet));
        mmarker.setTag(0);
    }
    public void removeMarker(){
        mmarker.remove();
    }
    public Marker getMarker(){
        return mmarker;
    }
    private LatLng getLatLng(){
        Geocoder geocoder = new Geocoder(context);
        List<Address> listAddress;
        LatLng templatlng = null;
        Address location;
        try{
            //get at most one place, can have multiple if needed
            listAddress = geocoder.getFromLocationName(address,1);
            if(listAddress == null){
                return null;
            }
            if(listAddress.size() == 0){
                return null;
            }
            //get the address address
            location = listAddress.get(0);
            //return the location in type latlng
            templatlng = new LatLng(location.getLatitude(),location.getLongitude());
        }
        catch(IOException e){
            e.printStackTrace();
            Toast.makeText(context,"error occurred",Toast.LENGTH_SHORT).show();
        }
        return templatlng;
    }
}