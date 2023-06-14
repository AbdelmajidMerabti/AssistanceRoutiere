package com.example.geocodeur;

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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jasypt.util.text.BasicTextEncryptor;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;
import java.util.Locale;

public class PageSignup extends AppCompatActivity implements LocationListener {

    private Button signupButton;
    private Button previeus;
    EditText signupTel, signupUsername, signupEmail, signupPassword, signupRePassword;
    TextView loginRedirectText;
    FirebaseDatabase database;
    DatabaseReference reference;
    private LocationManager locationManager;
    private String provider;
    private final int MY_PERMISSIONS_REQUEST_LOCATION = 100;
    String address;
    double latitude,longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_signup);

        signupTel = findViewById(R.id.editTextTextPersonName7);
        signupEmail = findViewById(R.id.editTextTextPersonName3);
        signupUsername = findViewById(R.id.editTextTextPersonName4);
        signupPassword = findViewById(R.id.editTextTextPersonName2);
        signupRePassword = findViewById(R.id.editTextTextPersonName);
        signupButton = findViewById(R.id.button10);
        previeus = findViewById(R.id.previeus);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("users");
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLocation();

                }
        });
        previeus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PageSignup.this, authentification.class);
                startActivity(i);
                finish();
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
           // locationManager.requestLocationUpdates(provider, 400, 1, this);

    }


    public void onLocationChanged(Location location) {

        try {
            //  Toast.makeText(this, String.valueOf(location.getLatitude()+" ; "+location.getLongitude()), Toast.LENGTH_SHORT).show();
            Geocoder geocoder = new Geocoder(PageSignup.this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
            address = addresses.get(0).getAddressLine(0);
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            String tel = signupTel.getText().toString();
            String email = signupEmail.getText().toString();
            String username = signupUsername.getText().toString();
            String password = signupPassword.getText().toString();
            String Repassword = signupRePassword.getText().toString();
              /*  UserClass helperClass = new UserClass(username, tel, email, password);
                reference.child(username).setValue(helperClass);
                Toast.makeText(PageSignup.this, "You have signup successfully!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(PageSignup.this, PageLogin.class);
                startActivity(intent);*/
            if(tel.isEmpty() || email.isEmpty() || username.isEmpty() || password.isEmpty() || Repassword.isEmpty())
            {
                Toast.makeText(PageSignup.this,"les champs vide !!!",Toast.LENGTH_SHORT).show();
            }
            else if(!password.equals(Repassword))
            {
                Toast.makeText(PageSignup.this,"mot de passe n'est pas correct !!!",Toast.LENGTH_SHORT).show();
            }
            else {
                DatabaseReference newChildRef = reference.push();
                UserClass userClass = new UserClass(username, tel, email, password,latitude,longitude);
                reference.child(newChildRef.getKey()).setValue(userClass);
                signupUsername.setText("");
                signupTel.setText("");
                signupEmail.setText("");
                signupPassword.setText("");
                signupRePassword.setText("");
                Toast.makeText(PageSignup.this, "Vous vous êtes inscrit avec succès !", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(PageSignup.this, authentification.class);
                startActivity(intent);
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