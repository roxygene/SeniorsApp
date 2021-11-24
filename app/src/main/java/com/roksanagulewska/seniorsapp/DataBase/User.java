package com.roksanagulewska.seniorsapp.DataBase;

import android.graphics.Bitmap;

public class User {
    private String userId;
    private String email;
    private String password; //konto
    private String name;
    private int age = 0;
    private String sex;
    private String localisation; //profile
    private String preferredSex; //profile
    private String description; //profile
    private String mainPicture; //profile
    private int minPrefAge = 0; //profile
    private int maxPrefAge = 0; //profile
    //dodać walidację do setterów

    public User() {

    }


    public User(String userId, String email, String password, String name, int age, String sex, String localisation, String preferredSex, String description, String mainPicture, int minPrefAge, int maxPrefAge) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.name = name;
        this.age = age;
        this.sex = sex;
        this.localisation = localisation;
        this.preferredSex = preferredSex;
        this.description = description;
        this.mainPicture = mainPicture;
        this.minPrefAge = minPrefAge;
        this.maxPrefAge = maxPrefAge;
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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
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

    //public Bitmap getMainPicture() {
        //return mainPicture;
    //}

    //public void setMainPicture(Bitmap mainPicture) {
       // this.mainPicture = mainPicture;
    //}

    public int getMinPrefAge() {
        return minPrefAge;
    }

    public void setMinPrefAge(int minPrefAge) {
        this.minPrefAge = minPrefAge;
    }

    public int getMaxPrefAge() {
        return maxPrefAge;
    }

    public void setMaxPrefAge(int maxPrefAge) {
        this.maxPrefAge = maxPrefAge;
    }
}
