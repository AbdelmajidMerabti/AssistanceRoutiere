package com.example.geocodeur;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class GlobalList {

    public static ArrayList<PanierClass> list_panier = new ArrayList<>();

    public static String formatNumber(double number) {
        return String.format("%.2f", number);
    }



}
