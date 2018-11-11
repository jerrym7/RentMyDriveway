package com.challenge.danny.rentmydriveway.UserInformation;

/**
 * Created by Gerardo on 10/29/18.
 * To store to the
 */
public class UserRating {
    String userName;
    String comment;
    double rating;

    public UserRating(String userName, String comment, double rating){
        this.userName = userName;
        this.comment = comment;
        this.rating = rating;

    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

}