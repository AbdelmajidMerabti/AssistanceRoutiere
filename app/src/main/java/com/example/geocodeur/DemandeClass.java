package com.example.geocodeur;

import android.text.format.DateFormat;

import java.time.LocalDateTime;

public class DemandeClass {

    String usernae,adress;
    double latitude,longitude;
    String dateAujordhui;
    int status;

    public DemandeClass(){}

    public DemandeClass(String usernae, String adress, double latitude, double longitude, String dateAujordhui) {
        this.usernae = usernae;
        this.adress = adress;
        this.latitude = latitude;
        this.longitude = longitude;
        this.dateAujordhui = dateAujordhui;
        this.status = 0;
    }

    public String getUsernae() {
        return usernae;
    }

    public void setUsernae(String usernae) {
        this.usernae = usernae;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
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

    public String getDateAujordhui() {
        return dateAujordhui;
    }

    public void setDateAujordhui(String dateAujordhui) {
        this.dateAujordhui = dateAujordhui;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
