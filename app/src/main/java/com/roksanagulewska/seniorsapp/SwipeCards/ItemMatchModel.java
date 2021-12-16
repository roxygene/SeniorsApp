package com.roksanagulewska.seniorsapp.SwipeCards;

public class ItemMatchModel {

    private String name;
    private String age;
    private String imageName;
    private String imageUri;
    private String userId;


    public ItemMatchModel() {
    }

    public ItemMatchModel(String userId, String name, String age, String imageName, String imageUri) {
        this.userId = userId;
        this.name = name;
        this.age = age;
        this.imageName = imageName;
        this.imageUri = imageUri;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
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

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}
