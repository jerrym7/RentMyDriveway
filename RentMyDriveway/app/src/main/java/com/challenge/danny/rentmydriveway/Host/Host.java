package com.challenge.danny.rentmydriveway.Host;

public class Host {
    private String streetName;
    private String city;
    private String state;
    private String country;
    private int zipcode = -1;
    private String timeIn;
    private String timeOut;
    private double latitude;
    private double longitude;
    private String latLngString; //need to check both lat and lng to retrieve the host with this latitude and longitude
    public Host(){

    }
    public Host(String streetName,String city,String state,
                String country, int zipcode, String timeIn,
                String timeOut, double latitude, double longitude) {
        this.streetName = streetName;
        this.city = city;
        this.state = state;
        this.country = country;
        this.zipcode = zipcode;
        this.timeIn = timeIn;
        this.timeOut = timeOut;
        this.latitude = latitude;
        this.longitude = longitude;
        this.latLngString = latitude+" , " + longitude;
    }

    //didn't return zipcode because in geocoding api we don't need it. But might, to be more accurate
    public String getFullAddress(){
        return streetName + ", " + city + ", " + state + ", " + country;
    }
    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setZipcode(int zipcode) {
        this.zipcode = zipcode;
    }

    public void setTimeIn(String timeIn) {
        this.timeIn = timeIn;
    }

    public void setTimeOut(String timeOut) {
        this.timeOut = timeOut;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getStreetName() {
        return streetName;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getCountry() {
        return country;
    }

    public int getZipcode() {
        return zipcode;
    }

    public String getTimeIn() {
        return timeIn;
    }

    public String getTimeOut() {
        return timeOut;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getLatLngString() {
        return this.latLngString;
    }

    public void setLatLngString(double latitude, double longitude) {
        this.latLngString = latitude+" , "+longitude;
    }
}
