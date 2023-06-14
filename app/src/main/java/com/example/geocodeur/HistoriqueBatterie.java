package com.example.geocodeur;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HistoriqueBatterie extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("utilisateur");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historique_batterie);

        myRef.orderByChild("libelle").equalTo("Batterie").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    GlobalList.list_panier.clear();
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                        String userId = childSnapshot.getKey();
                        // Utiliser l'identifiant auto_increment pour récupérer les données de l'utilisateur
                        Utilisateur utilisateur = childSnapshot.getValue(Utilisateur.class);

                        String type = "Type : "+utilisateur.getMarque();
                        String nom = utilisateur.getUsernae();
                        Double prix = utilisateur.getPrix();
                        String lettre = "";
                        String date = utilisateur.getDateAujordhui();


                        // Ajouter l'objet PanierItem à la liste
                        GlobalList.list_panier.add(new PanierClass(nom, type, String.valueOf(lettre), prix, date));
                    }
                }
                PanierAdapter adapter = new PanierAdapter(HistoriqueBatterie.this, GlobalList.list_panier);
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