package com.example.geocodeur;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class EntrepriseAdapter extends ArrayAdapter<societe> {
    private Context context;
    private List<societe> entreprisesList;

    public EntrepriseAdapter(Context context, List<societe> entreprisesList) {
        super(context, R.layout.entreprise_item, entreprisesList);
        this.context = context;
        this.entreprisesList = entreprisesList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.entreprise_item, parent, false);

        TextView nomTextView = rowView.findViewById(R.id.nomTextView);
        TextView adresseTextView = rowView.findViewById(R.id.adresseTextView);
        TextView telephoneTextView = rowView.findViewById(R.id.telephoneTextView);

        societe entreprise = entreprisesList.get(position);
        nomTextView.setText(entreprise.getNom());
        adresseTextView.setText(entreprise.getAdresse());
        telephoneTextView.setText(entreprise.getTel());
        return rowView;
    }
}
