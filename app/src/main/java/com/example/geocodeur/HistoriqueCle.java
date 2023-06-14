package com.example.geocodeur;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.Projection;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Overlay;

import java.util.List;

public class HistoriqueCle extends AppCompatActivity {

    MapView mapView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historique_cle);
        Configuration.getInstance().setUserAgentValue(getPackageName());

        mapView = findViewById(R.id.mapView);
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("utilisateur");

        databaseRef.orderByChild("libelle").equalTo("Batterie").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mapView.setTileSource(TileSourceFactory.MAPNIK);
                mapView.setBuiltInZoomControls(true);
                mapView.setMultiTouchControls(true);
                // Configurez la carte avec des options de zoom et de centre
                mapView.getController().setZoom(5.0);
                mapView.getController().setCenter(new GeoPoint(27.959, -9.712));
                for (DataSnapshot entrepriseSnapshot : dataSnapshot.getChildren()) {

                    String userId = entrepriseSnapshot.getKey();
                    // Utiliser l'identifiant auto_increment pour récupérer les données de l'utilisateur
                    Utilisateur utilisateur = entrepriseSnapshot.getValue(Utilisateur.class);
                    double latitude = utilisateur.getLatitude();
                    double longitude = utilisateur.getLongitude();
                    Marker marker = new Marker(mapView);
                    marker.setPosition(new GeoPoint(latitude, longitude));
                    marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                    marker.setTitle("Abdelmajid");
                    mapView.getOverlays().add(marker);
                }
                Marker marker = new Marker(mapView);
                marker.setPosition(new GeoPoint(35.849, -5.268));
                marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                marker.setTitle("Test");
                mapView.getOverlays().add(marker);
                mapView.invalidate();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("TAG", "Erreur de lecture des données de Firebase", databaseError.toException());
            }
        });











    }
}