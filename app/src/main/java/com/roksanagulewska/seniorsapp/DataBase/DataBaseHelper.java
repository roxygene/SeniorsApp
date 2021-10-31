package com.roksanagulewska.seniorsapp.DataBase;

import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class DataBaseHelper {

    public void addUserToDB(String uId, String email, String password) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("userId", uId);
        map.put("email", email);
        map.put("password", password);
        FirebaseDatabase.getInstance().getReference().child("Users").child(uId).updateChildren(map);
    }

    public void addUsersInfo(String uId, String name, String localisation, int age, String sex) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("localisation", localisation);
        map.put("age", age);
        map.put("sex", sex);
        FirebaseDatabase.getInstance().getReference().child("Users").child(uId).updateChildren(map);
    }

    public void addUsersPreferences(String uId, String prefferedSex, int minAge, int maxAge) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("prefferedSex", prefferedSex);
        map.put("minPrefAge", minAge);
        map.put("maxPrefAge", maxAge);
        FirebaseDatabase.getInstance().getReference().child("Users").child(uId).updateChildren(map);
    }
}
