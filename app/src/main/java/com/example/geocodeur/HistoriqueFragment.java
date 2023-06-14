package com.example.geocodeur;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.cardview.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


public class HistoriqueFragment extends Fragment {

    CardView cardViewenpanne,cardView3,cardView2,cardView1,historique_remorque;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_historique, container, false);

        cardViewenpanne = v.findViewById(R.id.cardViewenpanne);
        cardViewenpanne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),HistoriqueCarburant.class);
                startActivity(intent);
            }
        });

        historique_remorque = v.findViewById(R.id.cardView4);
        historique_remorque.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),HistoriqueRemorque.class);
                startActivity(intent);
            }
        });

        cardView3 = v.findViewById(R.id.cardView3);
        cardView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),HistoriquePneu.class);
                startActivity(intent);
            }
        });

        cardView2 = v.findViewById(R.id.cardView2);
        cardView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),HistoriqueBatterie.class);
                startActivity(intent);
            }
        });

       /* cardView1 = v.findViewById(R.id.cardView1);
        cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),HistoriqueCle.class);
                startActivity(intent);
            }
        });
*/
        return v;
    }

}