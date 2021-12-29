package com.roksanagulewska.seniorsapp.Chat;

public class ChatModel {

    private String name;
    private String imageName;
    private String imageUri;
    private String userId;


    public ChatModel() {
    }

    public ChatModel(String userId, String name, String imageName, String imageUri) {
        this.userId = userId;
        this.name = name;
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
}
