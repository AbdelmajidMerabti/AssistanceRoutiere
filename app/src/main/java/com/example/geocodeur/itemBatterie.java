package com.example.geocodeur;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class itemBatterie extends AppCompatActivity implements LocationListener {

    RadioGroup radioGroup;
    String text;
    Button envoyer;
    TextView result;
    double prix = 0;
    private Button previeus;
    FirebaseDatabase database;
    DatabaseReference reference;
    private LocationManager locationManager;
    private String provider;
    private final int MY_PERMISSIONS_REQUEST_LOCATION = 100;
    public List<Entreprise> entreprises = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_batterie);
        radioGroup = findViewById(R.id.radio_group);
        result = findViewById(R.id.result);
        envoyer = findViewById(R.id.envoyer);
        previeus = findViewById(R.id.previeus);
        entreprises.clear();
        afficherEntreprisesProches(MyGlobals.getLatitude(), MyGlobals.getLongitude());
        previeus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(itemBatterie.this, TypeAssistance.class);
                startActivity(i);
                finish();
            }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = findViewById(checkedId);
                text = radioButton.getText().toString();
                if(text.equals("Chargement d'eau"))
                {
                    prix = 500;
                }else  if(text.equals("Chargement entiere"))
                {
                    prix = 700;
                }else  if(text.equals("Alimentation electrique"))
                {
                    prix = 300;
                }
                result.setText(String.valueOf(prix));

            }
        });

        envoyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //   Toast.makeText(itemBatterie.this, "Valeur = "+text+", Prix = "+String.valueOf(prix), Toast.LENGTH_SHORT).show();
                getLocation();
                String uri = "le client il ya demander Batterie "+text+", Voici le map :  http://maps.google.com/maps?q=" + MyGlobals.getLatitude() + "," + MyGlobals.getLongitude();
                String uri1 = "il y a "+entreprises.get(0).getTime()+" pour arriver voila le map : http://maps.google.com/maps?q=" + entreprises.get(0).getLatitude() + "," + entreprises.get(0).getLongitude();
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(entreprises.get(0).getTel(), null, uri, null, null);
                smsManager.sendTextMessage(MyGlobals.getTel(), null, uri1, null, null);

            }
        });

    }

    @SuppressLint("MissingPermission")
    private void getLocation() {

        // Get the location manager
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);


        Location location = locationManager.getLastKnownLocation(provider);
        // If location is available, animate to it
        if (location != null) {
            onLocationChanged(location);
        }


        // Request location updates
        //     locationManager.requestLocationUpdates(provider, 400, 1, this);

    }


    @Override
    public void onLocationChanged(Location location) {
        //   Toast.makeText(this, ""+location.getLatitude()+","+location.getLongitude(), Toast.LENGTH_SHORT).show();
        try {
            database = FirebaseDatabase.getInstance();
            reference = database.getReference("utilisateur");
            DatabaseReference newChildRef = reference.push();
            Geocoder geocoder = new Geocoder(itemBatterie.this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
            String address = addresses.get(0).getAddressLine(0);
            DateTimeFormatter dtf = null;
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            {
                LocalDateTime now = LocalDateTime.now();
                dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss");
                String customFormat = dtf.format(now);
                //textView_location.setText("Spinner : "+result+", "+address+", Latitude : "+location.getLatitude()+", Longitude : "+location.getLongitude()+", Date : "+customFormat);
                Utilisateur utilisateurClass = new Utilisateur(MyGlobals.getEmail(), text,"","Batterie",location.getLatitude(), location.getLongitude(),prix,customFormat,0);
                reference.child(newChildRef.getKey()).setValue(utilisateurClass);
                Toast.makeText(this, "Success !!!!", Toast.LENGTH_SHORT).show();
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public void afficherEntreprisesProches(double lat, double lng) {


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("societe");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

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

    public String calculerTempsTrajet(double distance) {

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