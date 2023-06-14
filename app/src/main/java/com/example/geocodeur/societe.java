package com.example.geocodeur;

public class societe {
    String Nom,Ville,Adresse,Tel;
    double Latitude,Longitude,LatLon;

    public societe(String nom, String ville, String adresse, String tel, double latitude, double longitude, double latLon) {
        Nom = nom;
        Ville = ville;
        Adresse = adresse;
        Tel = tel;
        Latitude = latitude;
        Longitude = longitude;
        LatLon = latLon;
    }

    public String getNom() {
        return Nom;
    }

    public void setNom(String nom) {
        Nom = nom;
    }

    public String getVille() {
        return Ville;
    }

    public void setVille(String ville) {
        Ville = ville;
    }

    public String getAdresse() {
        return Adresse;
    }

    public void setAdresse(String adresse) {
        Adresse = adresse;
    }

    public String getTel() {
        return Tel;
    }

    public void setTel(String tel) {
        Tel = tel;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public double getLatLon() {
        return LatLon;
    }

    public void setLatLon(double latLon) {
        LatLon = latLon;
    }
}
