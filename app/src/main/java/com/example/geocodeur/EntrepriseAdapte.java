package com.example.geocodeur;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class EntrepriseAdapte extends RecyclerView.Adapter<EntrepriseAdapte.ViewHolder> {

    private List<Entreprise> entreprises;

    public EntrepriseAdapte(List<Entreprise> entreprises) {
        this.entreprises = entreprises;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_entreprise, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Entreprise entreprise = entreprises.get(position);
        holder.nom.setText(entreprise.getNom());
        holder.adresse.setText(entreprise.getTime());
        holder.telephone.setText(entreprise.getTel());
        holder.distance.setText(String.format("%.2f km", entreprise.getDistance()));
    }

    @Override
    public int getItemCount() {
        return entreprises.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView nom;
        TextView adresse;
        TextView telephone;
        TextView distance;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nom = itemView.findViewById(R.id.nom);
            adresse = itemView.findViewById(R.id.adresse);
            telephone = itemView.findViewById(R.id.telephone);
            distance = itemView.findViewById(R.id.distance);
        }
    }
}
