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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
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
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class itemPneu extends AppCompatActivity implements LocationListener {

    AutoCompleteTextView marque;
    AutoCompleteTextView taille;
    Button envoyer,previeus;
    String selectedItem,selectedItem1;
    String[] tabMarque = {"PIRELLI", "BRIDGESTONE","nokian TYRES","MICHELIN","HANKOOK","DUNLOP","Continental","GOODYEAR"};
    String[] tabTaille = {"16", "18","20","24","28","32"};
    Map<String, Map<String, Double>> prixPneus = new HashMap<>();

    double prix = 0;
    double prixMichelin = 100;
    double prixGoodyear = 120;
    double prixPirelli = 110;
    String v = "";
    TextView result;
    FirebaseDatabase database;
    DatabaseReference reference;
    private LocationManager locationManager;
    private String provider;
    private final int MY_PERMISSIONS_REQUEST_LOCATION = 100;
    public List<Entreprise> entreprises = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_pneu);

        envoyer = findViewById(R.id.envoyer);
        result = findViewById(R.id.result);
        ArrayAdapter<String> adapterMarque = new ArrayAdapter<>(this,
                R.layout.drop_down_item, tabMarque);
        marque = findViewById(R.id.fieled_exposed2);
        marque.setAdapter(adapterMarque);

        ArrayAdapter<String> adapterTaille = new ArrayAdapter<>(this,
                R.layout.drop_down_item, tabTaille);
        taille = findViewById(R.id.fieled_exposed3);
        taille.setAdapter(adapterTaille);
        entreprises.clear();
        afficherEntreprisesProches(MyGlobals.getLatitude(), MyGlobals.getLongitude());
        previeus = findViewById(R.id.previeus);
        previeus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(itemPneu.this, TypeAssistance.class);
                startActivity(i);
                finish();
            }
        });


// Ajoute les prix des pneus pour chaque marque et taille
        prixPneus.put("MICHELIN", new HashMap<>());
        prixPneus.get("MICHELIN").put("16", 100.0);
        prixPneus.get("MICHELIN").put("18", 120.0);
        prixPneus.get("MICHELIN").put("20", 140.0);

        prixPneus.put("GOODYEAR", new HashMap<>());
        prixPneus.get("GOODYEAR").put("16", 110.0);
        prixPneus.get("GOODYEAR").put("18", 130.0);
        prixPneus.get("GOODYEAR").put("20", 150.0);

        prixPneus.put("PIRELLI", new HashMap<>());
        prixPneus.get("PIRELLI").put("16", 90.0);
        prixPneus.get("PIRELLI").put("18", 110.0);
        prixPneus.get("PIRELLI").put("20", 130.0);

        marque.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                 selectedItem = parent.getItemAtPosition(position).toString();
                     prix = 0;
                     result.setText("");
                     taille.setText("");

              //  Toast.makeText(itemPneu.this, selectedItem , Toast.LENGTH_SHORT).show();
            }
        });

        taille.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedItem1 = parent.getItemAtPosition(position).toString();

                if (selectedItem == null || selectedItem.isEmpty()) {
                    result.setText("Veuillez sélectionner une marque");
                } else {
                    Map<String, Double> prixParTaille = prixPneus.get(selectedItem);
                    if (prixParTaille != null) {
                        Double prix = prixParTaille.get(selectedItem1);
                        if (prix != null) {
                            v = String.valueOf(prix);
                        } else {
                            v = "Prix non disponible";
                        }
                    } else {
                        v = "Marque non disponible";
                    }
                    result.setText(v);
                }
            }
        });



        envoyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isNumeric = v.matches("-?\\d+(\\.\\d+)?");
                if (isNumeric) {
                    if (marque.getText().toString().isEmpty() || marque.getText().toString().isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Veuillez sélectionner une marque et une taille", Toast.LENGTH_SHORT).show();
                    } else {
                        //    Toast.makeText(itemPneu.this, marque.getText().toString() + " : " + taille.getText().toString(), Toast.LENGTH_SHORT).show();
                        getLocation();
                        String uri = "le client il ya demander la cle de " + selectedItem + ", La Taile " + selectedItem1 + ", Voici le map :  http://maps.google.com/maps?q=" + MyGlobals.getLatitude() + "," + MyGlobals.getLongitude();
                        String uri1 = "il y a " + entreprises.get(0).getTime() + " pour arriver voila le map : http://maps.google.com/maps?q=" + entreprises.get(0).getLatitude() + "," + entreprises.get(0).getLongitude();
                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage(entreprises.get(0).getTel(), null, uri, null, null);
                        smsManager.sendTextMessage(MyGlobals.getTel(), null, uri1, null, null);
                        Toast.makeText(itemPneu.this, MyGlobals.getTel(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(itemPneu.this, "aucun choisi", Toast.LENGTH_SHORT).show();
                }
            }

        });

    }

    private void calculateResult(String selectedItemMarque, String selectedItemTaille, TextView textView) {
        if (selectedItemMarque.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Veuillez sélectionner une marque et une taille", Toast.LENGTH_SHORT).show();
        }else{
            if (selectedItemMarque.equals("MICHELIN")) {
                if (selectedItemTaille.equals("16")) {
                    prix = prixMichelin;
                } else if (selectedItemTaille.equals("18")) {
                    prix = prixMichelin + 20.0;
                } else if (selectedItemTaille.equals("20")) {
                    prix = prixMichelin + 40.0;
                }
            } else if (selectedItemTaille.equals("GOODYEAR")) {
                if (selectedItemTaille.equals("16")) {
                    prix = prixGoodyear;
                } else if (selectedItemTaille.equals("18")) {
                    prix = prixGoodyear + 20.0;
                } else if (selectedItemTaille.equals("20")) {
                    prix = prixGoodyear + 40.0;
                }
            } else if (selectedItemTaille.equals("PIRELLI")) {
                if (selectedItemTaille.equals("16")) {
                    prix = prixPirelli;
                } else if (selectedItemTaille.equals("18")) {
                    prix = prixPirelli + 20.0;
                } else if (selectedItemTaille.equals("20")) {
                    prix = prixPirelli + 40.0;
                }
            }

            textView.setText(String.valueOf(prix));
        }
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
            Geocoder geocoder = new Geocoder(itemPneu.this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
            String address = addresses.get(0).getAddressLine(0);
            DateTimeFormatter dtf = null;
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            {
                LocalDateTime now = LocalDateTime.now();
                dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss");
                String customFormat = dtf.format(now);
                //textView_location.setText("Spinner : "+result+", "+address+", Latitude : "+location.getLatitude()+", Longitude : "+location.getLongitude()+", Date : "+customFormat);
                Utilisateur utilisateurClass = new Utilisateur(MyGlobals.getEmail(), selectedItem,selectedItem1,"Pneu",location.getLatitude(), location.getLongitude(),prix,customFormat,0);
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