package com.example.geocodeur;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class UpdateUser extends AppCompatActivity {

    EditText username, telephone, email, password;
    Button updateButton,previeus;
    String usern, tel, mail, pass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user);

        username = findViewById(R.id.username);
        telephone = findViewById(R.id.phone);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        updateButton = findViewById(R.id.updateButton);


        previeus = findViewById(R.id.previeus);
        previeus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });


        Intent intent = getIntent();
        username.setText(intent.getStringExtra("user"));
        telephone.setText(intent.getStringExtra("tel"));
        email.setText(intent.getStringExtra("mail"));
        password.setText(intent.getStringExtra("pass"));

        usern = username.getText().toString();
        tel = telephone.getText().toString();
        mail = email.getText().toString();
        pass = password.getText().toString();


// Lorsque le bouton de modification est cliqué
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUser(MyGlobals.getKey(), username.getText().toString(), telephone.getText().toString(), email.getText().toString(), password.getText().toString());

            }
        });






    }


    public void updateUser(String userId, String newUsername, String newTelephone, String newEmail, String newPassword) {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userId);

        Map<String, Object> updatedValues = new HashMap<>();
        updatedValues.put("email", newEmail);
       /* updatedValues.put("latitude", MyGlobals.getLatitude());
        updatedValues.put("longitude", MyGlobals.getLongitude());*/
        updatedValues.put("password", newPassword);
        updatedValues.put("telephone", newTelephone);
        updatedValues.put("username", newUsername);

        databaseReference.updateChildren(updatedValues)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                     //   Toast.makeText(getApplicationContext(), "Les informations de l'utilisateur ont été mises à jour avec succès.", Toast.LENGTH_SHORT).show();
                        Intent intent1 = new Intent(UpdateUser.this,MainActivity.class);
                        startActivity(intent1);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Impossible de mettre à jour les informations de l'utilisateur : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


}