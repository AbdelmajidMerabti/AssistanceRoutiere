package com.example.geocodeur;

import androidx.appcompat.app.AppCompatActivity;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class HistoriqueRemorque extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("utilisateur");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historique_remorque);



        myRef.orderByChild("libelle").equalTo("Remorque").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    GlobalList.list_panier.clear();
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                        String userId = childSnapshot.getKey();
                        // Utiliser l'identifiant auto_increment pour récupérer les données de l'utilisateur
                        Utilisateur utilisateur = childSnapshot.getValue(Utilisateur.class);
                        Geocoder geocoder = new Geocoder(HistoriqueRemorque.this, Locale.getDefault());
                        List<Address> addresses = null;
                        try {
                            addresses = geocoder.getFromLocation(utilisateur.getLatitude(),utilisateur.getLongitude(),1);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        String address = addresses.get(0).getAddressLine(0);

                        String type = address;
                        String nom = "utilisateur.getUsernae();";
                        double  prix = utilisateur.getPrix();
                        String lettre = "";
                        String date = utilisateur.getDateAujordhui();


                        // Ajouter l'objet PanierItem à la liste
                        GlobalList.list_panier.add(new PanierClass(nom, type, String.valueOf(lettre), Math.ceil(prix), date));
                    }
                }
                PanierAdapter adapter = new PanierAdapter(HistoriqueRemorque.this, GlobalList.list_panier);
                ListView listView = findViewById(R.id.listview);
                listView.setAdapter(adapter);


            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Gérer l'erreur ici
            }
        });



    }
}