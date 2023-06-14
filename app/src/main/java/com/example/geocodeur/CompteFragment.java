package com.example.geocodeur;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;


public class CompteFragment extends Fragment {

    public CompteFragment(){}

   TextView username,telephone,email,password,testateur,modifieruser;
    String usern,tel,mail,pass;
    MapView mapView;

    

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

      //  Configuration.getInstance().setUserAgentValue(getPackageName());


        View v = inflater.inflate(R.layout.fragment_compte, container, false);
        Configuration.getInstance().setUserAgentValue(getContext().getPackageName());
        
       username = (TextView) v.findViewById(R.id.eusername);
        telephone = (TextView) v.findViewById(R.id.etelephone);
        email = (TextView) v.findViewById(R.id.eemail);
        password = (TextView) v.findViewById(R.id.epassword);
        modifieruser = (TextView) v.findViewById(R.id.modifieruser);
        mapView = v.findViewById(R.id.mapView);
     //   email.setText(MyGlobals.getEmail());



        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users");
        Query checkUserDatabase = reference.orderByChild("email").equalTo(MyGlobals.getEmail());
        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        UserClass user = snapshot1.getValue(UserClass.class);
                          username.setText(user.getUsername());
                          usern = user.getUsername();
                        telephone.setText(user.getTelephone());
                        tel = user.getTelephone();
                        email.setText(user.getEmail());
                        mail = user.getEmail();
                        password.setText(user.getPassword());
                        pass = user.getPassword();
                        mapView.setTileSource(TileSourceFactory.MAPNIK);
                        mapView.setBuiltInZoomControls(true);
                        mapView.setMultiTouchControls(true);
                        // Configurez la carte avec des options de zoom et de centre
                        mapView.getController().setZoom(18.0);
                        mapView.getController().setCenter(new GeoPoint(user.getLatitude(), user.getLongitude())); // définir le centre de la carte avec une GeoPoint
                        Marker marker = new Marker(mapView);
                        marker.setPosition(new GeoPoint(user.getLatitude(), user.getLongitude())); // définir la position du marqueur avec une GeoPoint
                        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                        mapView.getOverlays().add(marker); // ajouter le marqueur à la carte
                        mapView.invalidate();

                    }
                    }
                }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

        modifieruser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),UpdateUser.class);
                intent.putExtra("user", usern);
                intent.putExtra("tel", tel);
                intent.putExtra("mail", mail);
                intent.putExtra("pass", pass);
                startActivity(intent);
            }
        });

                return v;
    }



}