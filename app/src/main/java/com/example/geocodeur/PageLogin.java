package com.example.geocodeur;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jasypt.util.text.BasicTextEncryptor;
import org.mapsforge.map.layer.overlay.Marker;
import org.osmdroid.util.GeoPoint;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.Random;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;

import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class PageLogin extends AppCompatActivity implements LocationListener {

    private Button previeus;
    EditText loginUsername, loginPassword;
    Button loginButton;
    TextView signupRedirectText,textView2;
    private LocationManager locationManager;
    private String provider;
    private final int MY_PERMISSIONS_REQUEST_LOCATION = 100;
    double longitude;
    double latitude;
    String telephone;
    private TextView ForgetPassword;
    private TextInputEditText mEditText;
  /*  private static final String RECIPIENT_EMAIL = "abdelmajid.merabti@gmail.com";
    private static final String SUBJECT = "Votre nouveau mot de passe";
    //private static final String MESSAGE = "";
    private static final String SENDER_EMAIL = "abdelmajid.merabti@ensafez.com";
    private static final String SENDER_PASSWORD = "Ensaf2019@@";*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_login);


        previeus = findViewById(R.id.previeus);
        previeus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PageLogin.this, authentification.class);
                startActivity(i);
                finish();
            }
        });

        ForgetPassword = findViewById(R.id.ForgetPassword);
        ForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              /*  String toEmail = "abdelmajid.merabti@gmail.com";
                String subject = "Sujet de l'e-mail";
                String body = "Contenu de l'e-mail";
                new SendEmailTask(toEmail, subject, body).execute();*/

                // Créer le dialogue
                AlertDialog.Builder builder = new AlertDialog.Builder(PageLogin.this);
                builder.setTitle("Récupérer un mot de passe");

                // Créer le layout pour le EditText
                LayoutInflater inflater = LayoutInflater.from(PageLogin.this);
                View dialogLayout = inflater.inflate(R.layout.dialog_layout, null);
                mEditText = dialogLayout.findViewById(R.id.edit_text);


                builder.setView(dialogLayout);

                // Ajouter les boutons "OK" et "Annuler"
                builder.setPositiveButton("Envoyer", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String toEmail = mEditText.getText().toString();
                        String subject = "Sujet de l'e-mail";
                        String body = "Contenu de l'e-mail";
                        boolean isEmailValid = isEmailValid(mEditText);
                        if (isEmailValid) {

                            DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
                            Query query = usersRef.orderByChild("email").equalTo(mEditText.getText().toString().trim());
                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                                        UserClass utilisateur = userSnapshot.getValue(UserClass.class);
                                        if (utilisateur != null && utilisateur.getEmail().equals(mEditText.getText().toString())) {
                                            String nvpassword = generatePassword();
                                            new SendEmailTask(utilisateur.getEmail(),
                                                    "Récupérer un mot de passe",
                                                    "Bonjour Mr,\n" +utilisateur.getUsername()+
                                                            "\n" +
                                                            "Nous avons réinitialisé votre mot de passe et nous vous envoyons un nouveau mot de passe temporaire. Veuillez utiliser les informations ci-dessous pour vous connecter à votre compte :\n" +
                                                            "\n" +
                                                            "Nouveau mot de passe : "+nvpassword+"\n" +
                                                            "\n" +
                                                            "Nous vous recommandons de changer votre mot de passe dès que possible pour des raisons de sécurité. Si vous avez des questions ou des préoccupations, n'hésitez pas à nous contacter.\n" +
                                                            "\n" +
                                                            "Cordialement,").execute();
                                            //  DatabaseReference userRef = usersRef.child("Utilisateurs").child(utilisateur.getKey());
                                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userSnapshot.getKey());

                                            Map<String, Object> updatedValues = new HashMap<>();
                                            updatedValues.put("password", nvpassword);
                                            databaseReference.updateChildren(updatedValues)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            AlertDialog.Builder builder = new AlertDialog.Builder(PageLogin.this, R.style.AlertDialogTheme);
                                                            builder.setTitle("Information")
                                                                    .setMessage("Mot de passe envoi sur email")
                                                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(DialogInterface dialog, int which) {
                                                                            // Code à exécuter lorsque l'utilisateur clique sur le bouton OK
                                                                        }
                                                                    }) ;
                                                            AlertDialog alertDialog = builder.create();
                                                            alertDialog.show();

                                                            return;
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            // Une erreur s'est produite lors de la mise à jour
                                                        }
                                                    });

                                        }
                                        else
                                        {
                                            Toast.makeText(PageLogin.this, "Email inconnu", Toast.LENGTH_SHORT).show();
                                            break;
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Log.e("TAG", "Erreur lors de la recherche de l'utilisateur", error.toException());
                                }
                            });


                        } else {
                            Toast.makeText(PageLogin.this, "entrez mail svp !!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.setNegativeButton("Annuler", null);

                // Afficher le dialogue
                AlertDialog dialog = builder.create();
                dialog.show();


            }
        });


        loginUsername = findViewById(R.id.editTextTextPersonName4);
        loginPassword = findViewById(R.id.editTextTextPersonName2);
        loginButton = findViewById(R.id.button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateUsername() | !validatePassword()) {
                } else {
                    checkUser();
                }
            }
        });


    }

    public Boolean validateUsername() {
        String val = loginUsername.getText().toString();
        if (val.isEmpty()) {
            loginUsername.setError("Username cannot be empty");
            return false;
        } else {
            loginUsername.setError(null);
            return true;
        }
    }
    public Boolean validatePassword(){
        String val = loginPassword.getText().toString();
        if (val.isEmpty()) {
            loginPassword.setError("Password cannot be empty");
            return false;
        } else {
            loginPassword.setError(null);
            return true;
        }
    }
    public void checkUser(){
        MyGlobals.setEmail("");
        MyGlobals.setLatitude(0);
        MyGlobals.setLongitude(0);
        String userUsername = loginUsername.getText().toString().trim();
        String userPassword = loginPassword.getText().toString().trim();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users");
        Query checkUserDatabase = reference.orderByChild("username").equalTo(userUsername);
        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    loginUsername.setError(null);
                    for(DataSnapshot snapshot1 : snapshot.getChildren())
                    {
                        UserClass user = snapshot1.getValue(UserClass.class);
                         if(user.getUsername().equals(userUsername) && user.getPassword().equals(userPassword))
                         {
                             telephone = user.getTelephone();
                             String userKey = snapshot1.getKey();
                             //Toast.makeText(PageLogin.this, "bravo", Toast.LENGTH_SHORT).show();

                          /*   if (ContextCompat.checkSelfPermission(PageLogin.this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                                     != PackageManager.PERMISSION_GRANTED){
                                 ActivityCompat.requestPermissions(PageLogin.this,new String[]{
                                         android.Manifest.permission.ACCESS_FINE_LOCATION
                                 },100);
                             }
                             else {
                                 LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                                 // Obtenir la dernière position connue
                                 Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                                 if (location != null) {

                                     latitude = location.getLatitude();
                                     longitude = location.getLongitude();
                                     reference.child(userKey).child("latitude").setValue(latitude);
                                     reference.child(userKey).child("longitude").setValue(longitude);

                                     //       reference.child(userKey).child("telephone").setValue(longitude);

                                 } else {
                                     Toast.makeText(PageLogin.this, "error location", Toast.LENGTH_SHORT).show();
                                 }
                             }*/
                             // Vérifier que l'application a l'autorisation de localisation


                             locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                             Criteria criteria = new Criteria();
                             provider = locationManager.getBestProvider(criteria, false);

                             // Get the last known location
                             if (ContextCompat.checkSelfPermission(PageLogin.this,
                                     android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                                 Location location = locationManager.getLastKnownLocation(provider);
                                 // If location is available, animate to it
                                 if (location != null) {
                                     onLocationChanged(location);
                                 }
                             } else {
                                 // Permission is not granted
                                 ActivityCompat.requestPermissions(PageLogin.this,
                                         new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                                         MY_PERMISSIONS_REQUEST_LOCATION);
                             }

                     //        Toast.makeText(PageLogin.this, String.valueOf(latitude+","+longitude), Toast.LENGTH_SHORT).show();
                             MyGlobals.setEmail(user.getEmail());
                             MyGlobals.setNom(user.getUsername());
                             MyGlobals.setLatitude(latitude);
                             MyGlobals.setLongitude(longitude);
                             MyGlobals.setTel(telephone);
                             MyGlobals.setKey(userKey);
                             GlobalList.list_panier.clear();
                             Intent intent = new Intent(PageLogin.this, MainActivity.class);
                             startActivity(intent);
                             finish();
                         }
                         else
                         {
                             Toast.makeText(PageLogin.this, "Errrrror", Toast.LENGTH_SHORT).show();
                         }
                    }
                } else {
                    loginUsername.setError("User does not exist");
                    loginUsername.requestFocus();
                    Toast.makeText(PageLogin.this, "User does not exist", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

  /*  public String decrypt(String encryptedPassword) {
        BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
        textEncryptor.setPassword("myEncryptionPassword");
        return textEncryptor.decrypt(encryptedPassword);
    }*/



    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();


        // Toast.makeText(this,String.valueOf(lat+","+lng),Toast.LENGTH_SHORT).show();

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

    public boolean isEmailValid(EditText editText) {
        String email = editText.getText().toString().trim();

        if (email.isEmpty()) {
            return false;
        }

        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


    public static String generatePassword() {
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890!@#$%^&*()_+";
        StringBuilder password = new StringBuilder();
        Random rnd = new Random();
        while (password.length() < 8) {
            int index = (int) (rnd.nextFloat() * chars.length());
            password.append(chars.charAt(index));
        }
        return password.toString();
    }


    // Remove the location updates when the app is stopped
  /*  @Override
    protected void onStop() {
        super.onStop();
        locationManager.removeUpdates(this);
    }*/
}