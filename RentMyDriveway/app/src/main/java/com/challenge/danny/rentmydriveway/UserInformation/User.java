package com.challenge.danny.rentmydriveway.UserInformation;


/*
 *This class is designed to save current user's input and store it in the Firebase database
 * (Just in case, it might be needed in the future.)
 */
public class User {
    private String name;
    private String email;

    public User() {
    }
    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
