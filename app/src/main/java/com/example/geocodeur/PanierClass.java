package com.example.geocodeur;

public class PanierClass {
    String nom,type,lettre;
    double prix;
    String date;

    public PanierClass(){}

    public PanierClass(String nom, String type, String lettre, double prix, String date) {
        this.nom = nom;
        this.type = type;
        this.lettre = lettre;
        this.prix = prix;
        this.date = date;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLettre() {
        return lettre;
    }

    public void setLettre(String lettre) {
        this.lettre = lettre;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
