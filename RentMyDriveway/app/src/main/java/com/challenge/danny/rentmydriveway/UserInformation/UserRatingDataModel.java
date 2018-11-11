package com.challenge.danny.rentmydriveway.UserInformation;
/**
 * Created by Gerardo on 10/29/18.
 * To save the data in the comment section (rows)
 */
import android.net.Uri;

public class UserRatingDataModel {

    String username;
    String comment;
    double rating;
    Uri picture;//not needed but in the future

    public UserRatingDataModel(String username , String comment
            , int rating) {
        this.username = username;
        this.comment = comment;
        this.rating = rating;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public Uri getPicture() {
        return picture;
    }

    public void setPicture(Uri picture) {
        this.picture = picture;
    }
}
