package com.example.geocodeur;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MapFragment extends Fragment {

    private AutoCompleteTextView search;
    private MapView mapView;
    private List<String> villes = new ArrayList<>();
    private DatabaseReference ref;
    String longitude;
    String latitude;
    private String selectedItem;
    private String adresse, tel;
    private Marker marker, marker1;
    private String villeSelectionnee;
    private List<Marker> markersToDelete = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Initialiser la configuration d'OSMDroid
        Configuration.getInstance().load(getContext(), getActivity().getPreferences(Context.MODE_PRIVATE));

        // Inflater le layout du fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        // Récupérer les références des vues
        search = view.findViewById(R.id.search);
        mapView = view.findViewById(R.id.mapView);

        // Activer les contrôles de zoom et multitouch
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);

        // Configurer la carte avec des options de zoom et de centre
        GeoPoint point = new GeoPoint(MyGlobals.getLatitude(), MyGlobals.getLongitude());
        IMapController mapController = mapView.getController();
        mapController.setZoom(8.0);
        mapController.setCenter(point);

        // Récupérer la référence à la base de données Firebase
        ref = FirebaseDatabase.getInstance().getReference().child("societe");

        // Écouter les données une seule fois
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Parcourir les données pour récupérer les villes
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String ville = snapshot.child("Ville").getValue(String.class);
                    if (!villes.contains(ville)) {
                        villes.add(ville);
                    }
                }

                // Trier les villes par ordre alphabétique
                Collections.sort(villes);

                // Créer un adaptateur pour la saisie automatique
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, villes);
                search.setAdapter(adapter);

                // Écouter la sélection d'une ville dans la saisie automatique
                search.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // Récupérer la ville sélectionnée
                        villeSelectionnee = (String) parent.getItemAtPosition(position);

                        // Supprimer les marqueurs existants
                        removeMarkers();

                        // Parcourir les données pour trouver la ville sélectionnée
                        for (DataSnapshot dataSnapshot : dataSnapshot.getChildren()) {
                            String ville = dataSnapshot.child("Ville").getValue(String.class);
                            if (ville.equals(villeSelectionnee)) {
                                latitude = dataSnapshot.child("Latitude").getValue(String.class);
                                longitude = dataSnapshot.child("Longitude").getValue(String.class);
                                adresse = dataSnapshot.child("Adresse").getValue(String.class);
                                tel = dataSnapshot.child("Tel").getValue(String.class);
                                double paLatitude = Double.parseDouble(latitude);
                                double paLongitude = Double.parseDouble(longitude);
                                String paAdresse = adresse;
                                String paTel = tel;

                                // Créer un nouveau marqueur pour l'entreprise
                                marker = new Marker(mapView);
                                marker.setPosition(new GeoPoint(paLatitude, paLongitude));
                                marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                                marker.setIcon(getResources().getDrawable(R.drawable.ic_baseline_location_on_24));
                                //marker.setTextIcon(adresse);
                                markersToDelete.add(marker);
                                marker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
                                    @Override
                                    public boolean onMarkerClick(Marker marker, MapView mapView) {
                                        // Afficher une boîte de dialogue avec les coordonnées de contact
                                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                        builder.setTitle("Coordonnées de contact");
                                        builder.setMessage("Adresse : " + paAdresse + "\nTéléphone : " + paTel);
                                        builder.setPositiveButton("Appeler", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                // Composer le numéro de téléphone
                                                String phoneNumber = "tel:" + paTel;
                                                Intent dial = new Intent(Intent.ACTION_CALL, Uri.parse(phoneNumber));
                                                startActivity(dial);
                                            }
                                        });
                                        builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        });
                                        AlertDialog dialog = builder.create();
                                        dialog.show();

                                        return false;
                                    }
                                });
                                mapView.getOverlays().add(marker);
                                mapController.setCenter(new GeoPoint(paLatitude, paLongitude));
                                mapController.setZoom(12.0);
                            }
                        }

                        // Ajouter un marqueur pour la position actuelle
                        marker1 = new Marker(mapView);
                        marker1.setPosition(new GeoPoint(MyGlobals.getLatitude(), MyGlobals.getLongitude()));
                        marker1.setIcon(getResources().getDrawable(org.osmdroid.library.R.drawable.person));
                        marker1.setTitle("Test");
                        mapView.getOverlays().add(marker1);
                        mapView.invalidate();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("TAG", "Erreur de lecture des données : " + databaseError.getMessage());
            }
        });

        return view;
    }

    // Supprimer les marqueurs existants de la carte
    private void removeMarkers() {
        if (marker != null || marker1 != null) {
            for (Marker marker : markersToDelete) {
                mapView.getOverlays().remove(marker);
            }
            mapView.getOverlays().remove(marker1);
            markersToDelete.clear();
            marker = null;
            marker1 = null;
        }
    }
}

