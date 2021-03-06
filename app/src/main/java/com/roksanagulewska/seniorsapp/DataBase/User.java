package com.roksanagulewska.seniorsapp.DataBase;

import android.graphics.Bitmap;
import android.net.Uri;

public class User {
    private String userId;
    private String email;
    private String password;
    private String name;
    private String age;
    private String sex;
    private String localisation;
    private String preferredSex;
    private String description;
    private String mainPictureName;
    private String imageUri;
    private String minPrefAge;
    private String maxPrefAge;
    //dodać walidację do setterów

    public User() { //pusty konstruktor

    }

    public User(String userId, String email, String password, String name, String age, String sex, String localisation, String preferredSex, String description, String mainPictureName, String imageUri, String minPrefAge, String maxPrefAge) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.name = name;
        this.age = age;
        this.sex = sex;
        this.localisation = localisation;
        this.preferredSex = preferredSex;
        this.description = description;
        this.mainPictureName = mainPictureName;
        this.imageUri = imageUri;
        this.minPrefAge = minPrefAge;
        this.maxPrefAge = maxPrefAge;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getLocalisation() {
        return localisation;
    }

    public void setLocalisation(String localisation) {
        this.localisation = localisation;
    }

    public String getPreferredSex() {
        return preferredSex;
    }

    public void setPreferredSex(String preferredSex) {
        this.preferredSex = preferredSex;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMainPictureName() {
        return mainPictureName;
    }

    public void setMainPicture(String mainPicture) {
        this.mainPictureName = mainPictureName;
    }

    public String getMinPrefAge() {
        return minPrefAge;
    }

    public void setMinPrefAge(String minPrefAge) {
        this.minPrefAge = minPrefAge;
    }

    public String getMaxPrefAge() {
        return maxPrefAge;
    }

    public void setMaxPrefAge(String maxPrefAge) {
        this.maxPrefAge = maxPrefAge;
    }
}
