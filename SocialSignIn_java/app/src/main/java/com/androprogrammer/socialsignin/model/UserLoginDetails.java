package com.androprogrammer.socialsignin.model;

import java.io.Serializable;

/**
 * Created by Wasim on 04-12-2015.
 */
public class UserLoginDetails implements Serializable{

    private int UserID;

    private String FbID;
    private String GoogleID;
    private String TwitterID;
    private String fullName;
    private String LastName;
    private String Email;
    private String City;
    private String Mobile;
    private String Password;
    private String UserImageUrl;
    private String UserImageUri;
    private String State;


    private boolean isFbLogin;
    private boolean isAndroid;
    private boolean isGplusLogin;
    private boolean isTwittersLogin;
    private boolean isBasicLogin;
    private boolean IsSocial;



    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        this.City = city;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        this.Email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public int getUserID() {
        return UserID;
    }

    public void setUserID(int userID) {
        UserID = userID;
    }

    public boolean isBasicLogin() {
        return isBasicLogin;
    }

    public void setIsBasicLogin(boolean isBasicLogin) {
        this.isBasicLogin = isBasicLogin;
    }

    public boolean isFbLogin() {
        return isFbLogin;
    }

    public void setIsFbLogin(boolean isFbLogin) {
        this.isFbLogin = isFbLogin;
    }

    public boolean isGplusLogin() {
        return isGplusLogin;
    }

    public void setIsGplusLogin(boolean isGplusLogin) {
        this.isGplusLogin = isGplusLogin;
    }

    public String getUserImageUri() {
        return UserImageUri;
    }

    public void setUserImageUri(String userImageUri) {
        UserImageUri = userImageUri;
    }

    public void setFbID(String fbID) {
        FbID = fbID;
    }

    public void setGoogleID(String googleID) {
        this.GoogleID = googleID;
    }

    public String getUserImageUrl() {
        return UserImageUrl;
    }

    public void setUserImageUrl(String userImageUrl) {
        UserImageUrl = userImageUrl;
    }

    public boolean isSocial() {
        return IsSocial;
    }

    public void setIsSocial(boolean isSocial) {
        IsSocial = isSocial;
    }

    public void setIsAndroid(boolean isAndroid) {
        this.isAndroid = isAndroid;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getTwitterID() {
        return TwitterID;
    }

    public void setTwitterID(String twitterID) {
        TwitterID = twitterID;
    }

    public boolean isTwittersLogin() {
        return isTwittersLogin;
    }

    public void setTwittersLogin(boolean twittersLogin) {
        isTwittersLogin = twittersLogin;
    }
}
