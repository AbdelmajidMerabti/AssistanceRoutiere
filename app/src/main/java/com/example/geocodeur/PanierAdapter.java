package com.example.geocodeur;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class PanierAdapter extends BaseAdapter {
    private ArrayList<PanierClass> paniers;
    private LayoutInflater inflater;

    public PanierAdapter(Context context, ArrayList<PanierClass> paniers) {
        this.paniers = paniers;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return paniers.size();
    }

    @Override
    public Object getItem(int position) {
        return paniers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_panier, parent, false);
            holder = new ViewHolder();
            holder.nomTextView = convertView.findViewById(R.id.nom_text_view);
            holder.typeTextView = convertView.findViewById(R.id.type_text_view);
            holder.prixTextView = convertView.findViewById(R.id.prix_text_view);
            holder.dateTextView = convertView.findViewById(R.id.date_text_view);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        PanierClass panier = paniers.get(position);
        holder.nomTextView.setText(panier.getType());
        holder.typeTextView.setText(panier.getLettre());
        holder.prixTextView.setText("Prix : "+String.valueOf(panier.getPrix()));
        holder.dateTextView.setText("Date : "+panier.getDate());

        return convertView;
    }

    private static class ViewHolder {
        TextView nomTextView;
        TextView typeTextView;
        TextView prixTextView;
        TextView dateTextView;
    }
}
