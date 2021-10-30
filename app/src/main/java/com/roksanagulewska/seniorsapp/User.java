package com.roksanagulewska.seniorsapp;

import android.graphics.Bitmap;

public class User {
    private String email; //konto
    private String password; //konto
    private String name; //konto
    private int age; //konto
    private String sex; //konto
    private String localisation; //profile
    private String preferenceSex; //profile
    private String description; //profile
    private Bitmap mainPicture; //profile
    private int minPrefAge; //profile
    private int maxPrefAge; //profile

    //dodać walidację do setterów

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

    public String getPreferenceSex() {
        return preferenceSex;
    }

    public void setPreferenceSex(String preferenceSex) {
        this.preferenceSex = preferenceSex;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Bitmap getMainPicture() {
        return mainPicture;
    }

    public void setMainPicture(Bitmap mainPicture) {
        this.mainPicture = mainPicture;
    }

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


//dodawanie danych do bazy danych np: jednej gałęzi z nową gałęzią która ma wartość abc
//FirebaseDatabase.getInstance().getReference().child("gałąź 1").child("gałąź 2").setValue("abcd");

//kiedy chcemy dodać więcej niż jedną gałąź
//HashMap<String, Object> map = new HashMap<>(); //utworzenie hashmapy
//map.put("Name", "Roksana");
//map.put("Email", "Roksana@gmail.com");
//FirebaseDatabase.getInstance().getReference().child("nazwa usera").updateChildren(map);
