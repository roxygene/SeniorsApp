package com.roksanagulewska.seniorsapp.SwipeCards;

public class ItemModel {
    private int image;
    private String name;
    private int age;
    private String localisation;
    private String description;


    public ItemModel() {
    }

    public ItemModel(int image, String name, int age, String localisation, String description) {
        this.image = image;
        this.name = name;
        this.age = age;
        this.localisation = localisation;
        this.description = description;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
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
