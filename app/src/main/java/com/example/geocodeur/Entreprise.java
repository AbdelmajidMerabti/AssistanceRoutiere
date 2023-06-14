package com.example.geocodeur;

public class Entreprise {
    private String Nom;
    private String Adresse;
    private String Tel;
    private String Latitude;
    private String Longitude;
    private double distance;
    private String time;

    public Entreprise() {
        // Constructeur vide requis pour Firebase
    }

    public Entreprise(String Nom, String Adresse, String Tel, String Latitude, String Longitude, String time) {
        this.Nom = Nom;
        this.Adresse = Adresse;
        this.Tel = Tel;
        this.Latitude = Latitude;
        this.Longitude = Longitude;
        this.time = time;
    }

    public String getNom() {
        return Nom;
    }

    public void setNom(String Nom) {
        this.Nom = Nom;
    }

    public String getAdresse() {
        return Adresse;
    }

    public void setAdresse(String Adresse) {
        this.Adresse = Adresse;
    }

    public String getTel() {
        return Tel;
    }

    public void setTel(String Tel) {
        this.Tel = Tel;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String Latitude) {
        this.Latitude = Latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String Longitude) {
        this.Longitude = Longitude;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
