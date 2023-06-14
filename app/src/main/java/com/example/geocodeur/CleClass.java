package com.example.geocodeur;

public class CleClass {
    String marque,type;
    double latitude,longitude,prix;

    public CleClass(){}

    public CleClass(String marque, String type, double latitude, double longitude, double prix) {
        this.marque = marque;
        this.type = type;
        this.latitude = latitude;
        this.longitude = longitude;
        this.prix = prix;
    }

    public String getMarque() {
        return marque;
    }

    public void setMarque(String marque) {
        this.marque = marque;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }
}
