package com.example.geocodeur;

public class Utilisateur {

    String usernae,marque,type,libelle;
    double latitude,longitude,prix;
    String dateAujordhui;
    int status;

    public Utilisateur(){}

    public Utilisateur(String usernae, String marque, String type, String libelle, double latitude, double longitude, double prix, String dateAujordhui, int status) {
        this.usernae = usernae;
        this.marque = marque;
        this.type = type;
        this.libelle = libelle;
        this.latitude = latitude;
        this.longitude = longitude;
        this.prix = prix;
        this.dateAujordhui = dateAujordhui;
        this.status = status;
    }

    public String getUsernae() {
        return usernae;
    }

    public void setUsernae(String usernae) {
        this.usernae = usernae;
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

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
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
