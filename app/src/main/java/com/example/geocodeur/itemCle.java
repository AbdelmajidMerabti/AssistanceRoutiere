package com.example.geocodeur;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

public class itemCle extends AppCompatActivity {

    AutoCompleteTextView autoCompleteTextView1;
    String[] values = {"BMW", "Mercedes","Dacia"};
    Button previeus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_cle);

        previeus = findViewById(R.id.previeus);
        previeus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(itemCle.this, TypeAssistance.class);
                startActivity(i);
                finish();
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.drop_down_item, values);
        autoCompleteTextView1 = findViewById(R.id.fieled_exposed1);
        autoCompleteTextView1.setAdapter(adapter);
    }
}