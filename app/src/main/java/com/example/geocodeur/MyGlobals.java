package com.example.geocodeur;

public class MyGlobals {
    private static String email;
    private static double latitude;
    private static double longitude;
    private static String tel;
    private static String key;
    private static String nom;

    public static String getKey() {
        return key;
    }

    public static void setKey(String key) {
        MyGlobals.key = key;
    }

    public static String getNom() {
        return nom;
    }

    public static void setNom(String nom) {
        MyGlobals.nom = nom;
    }

    public static String getTel() {
        return tel;
    }

    public static void setTel(String tel) {
        MyGlobals.tel = tel;
    }

    public static double getLatitude() {
        return latitude;
    }

    public static void setLatitude(double latitude) {
        MyGlobals.latitude = latitude;
    }

    public static double getLongitude() {
        return longitude;
    }

    public static void setLongitude(double longitude) {
        MyGlobals.longitude = longitude;
    }

    public static String getEmail() {
        return email;
    }

    public static void setEmail(String email) {
        MyGlobals.email = email;
    }
}
