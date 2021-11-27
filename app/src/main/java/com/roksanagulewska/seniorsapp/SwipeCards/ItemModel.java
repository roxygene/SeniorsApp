package com.roksanagulewska.seniorsapp.SwipeCards;

import android.net.Uri;

public class ItemModel {
    private int image;
    private String email;
    private String name;
    private int age;
    private String localisation;
    private String imageName;
    private String imageURL;
    private String imageUri;
    private String description;
    private String userId;


    public ItemModel() {
    }

    public ItemModel(int image, String email, String userId, String name, int age, String localisation, String imageName, String imageURL, String imageUri, String description) {
        this.image = image;
        this.email = email;
        this.userId = userId;
        this.name = name;
        this.age = age;
        this.localisation = localisation;
        this.imageName = imageName;
        this.imageURL = imageURL;
        this.imageUri = imageUri;
        this.description = description;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
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

    public String getLocalisation() {
        return localisation;
    }

    public void setLocalisation(String localisation) {
        this.localisation = localisation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
