package com.example.geocodeur;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity2 extends AppCompatActivity {

    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        button = findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                afficherEntreprisesProches(MyGlobals.getLatitude(), MyGlobals.getLongitude());
            }
        });

    }

   public void afficherEntreprisesProches(double lat, double lng) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("societe");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Entreprise> entreprises = new ArrayList<>();
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    Entreprise entreprise = snap.getValue(Entreprise.class);
                    double palat = Double.parseDouble(entreprise.getLatitude());
                    double palong = Double.parseDouble(entreprise.getLongitude());
                    double distance = distanceEntreDeuxPoints(lat, lng, palat, palong);
                    String time = calculerTempsTrajet(distance);
                    entreprise.setDistance(distance);
                    entreprise.setTime(time);
                    entreprises.add(entreprise);
                }
                Collections.sort(entreprises, new Comparator<Entreprise>() {
                    @Override
                    public int compare(Entreprise o1, Entreprise o2) {
                        return Double.compare(o1.getDistance(), o2.getDistance());
                    }
                });
                EntrepriseAdapte adapter = new EntrepriseAdapte(entreprises);
                RecyclerView recyclerView = findViewById(R.id.recycler_view);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Gérer l'erreur ici
            }
        });

    }

    public double distanceEntreDeuxPoints(double lat1, double lng1, double lat2, double lng2) {
        int rayonTerre = 6371;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return rayonTerre * c;
    }

    /*public String calculerTempsTrajet(double distance, double vitesse) {
        double distance_km = distance / 1000;
        double vitesse_km_h = vitesse * 3.6;
        double temps_h = distance_km / vitesse_km_h;
        int heures = (int)temps_h;
        int minutes = (int)((temps_h - heures) * 60);
        return String.format("%d heures %d minutes", heures, minutes);
    }*/

    public String calculerTempsTrajet(double distance) {
        // Vitesse moyenne en km/h
        double vitesseMoyenne = 50;
        String message = "";

        // Calcul du temps de trajet en heures
        double tempsTrajetEnHeures = distance / vitesseMoyenne;

        // Conversion du temps de trajet en heures en millisecondes
        long tempsTrajetEnMillisecondes = (long) (tempsTrajetEnHeures * 3600 * 1000);

        // Création d'un objet Date pour le temps de trajet en millisecondes
        Date tempsTrajetDate = new Date(tempsTrajetEnMillisecondes);

        // Renvoi du temps de trajet formaté en heures et minutes
        SimpleDateFormat formatHeuresMinutes = new SimpleDateFormat("HH:mm");
        String res = formatHeuresMinutes.format(tempsTrajetDate);
        String[] sousChaines = res.split(":");
        String heure = sousChaines[0];
        String minute = sousChaines[1];
        if(heure.equals("00"))
        {
            message = minute+" min";
        }
        else
        {
            message = heure+" heure et "+minute+" min";
        }
        return message;
    }




}
