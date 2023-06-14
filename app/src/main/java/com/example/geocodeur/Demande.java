package com.example.geocodeur;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class Demande extends AppCompatActivity implements LocationListener {

    Spinner spinner;
    Button btnDemande;
    String[] keys = {"Clé 1", "Clé 2", "Clé 3"};
    String[] values = {"Abdelmajid", "Omar", "Mohammed"};
    TextView textView_location;
    private LocationManager locationManager;
    private String provider;
    private final int MY_PERMISSIONS_REQUEST_LOCATION = 100;
    String result;
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demande);



        spinner = findViewById(R.id.spinner);
        textView_location = findViewById(R.id.text_location);
        btnDemande = findViewById(R.id.btnDemande);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, keys);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                result = values[position];
                String selectedValueText = "La valeur sélectionnée est : " + result;
                Toast.makeText(Demande.this, selectedValueText, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Ne fait rien si rien n'est sélectionné
            }
        });

        btnDemande.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocation();
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
        Toast.makeText(this, ""+location.getLatitude()+","+location.getLongitude(), Toast.LENGTH_SHORT).show();
        try {
            database = FirebaseDatabase.getInstance();
            reference = database.getReference("demandes");
            Geocoder geocoder = new Geocoder(Demande.this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
            String address = addresses.get(0).getAddressLine(0);
            DateTimeFormatter dtf = null;
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            {
                LocalDateTime now = LocalDateTime.now();
                dtf = DateTimeFormatter.ofPattern("yyyy");
                String year = dtf.format(now);
                dtf = DateTimeFormatter.ofPattern("MM");
                String month = dtf.format(now);
                dtf = DateTimeFormatter.ofPattern("dd");
                String day = dtf.format(now);
                dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss");
                String customFormat = dtf.format(now);
                textView_location.setText("Spinner : "+result+", "+address+", Latitude : "+location.getLatitude()+", Longitude : "+location.getLongitude()+", Date : "+customFormat);
                DemandeClass demandeClass = new DemandeClass(result, address, location.getLatitude(), location.getLongitude(),customFormat);
                reference.child(result).setValue(demandeClass);
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

}