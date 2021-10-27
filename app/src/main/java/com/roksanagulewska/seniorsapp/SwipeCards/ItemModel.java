package com.roksanagulewska.seniorsapp.SwipeCards;

public class ItemModel {
    private int image;
    private String nama, usia, kota;

    public ItemModel() {
    }

    public ItemModel(int image, String nama, String usia, String kota) {
        this.image = image;
        this.nama = nama; //imię
        this.usia = usia; //wiek
        this.kota = kota; //miasto
    }

    public int getImage() {
        return image;
    }

    public String getNama() {
        return nama;
    }

    public String getUsia() {
        return usia;
    }

    public String getKota() {
        return kota;
    }
}
