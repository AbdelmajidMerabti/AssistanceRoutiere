package com.example.geocodeur;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    HomeFragment homeFragment = new HomeFragment();
    CompteFragment compteFragment = new CompteFragment();  //settingsFragment
    HistoriqueFragment historiqueFragment = new HistoriqueFragment();  //notificationFragment
    MapFragment mapFragment = new MapFragment();  //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView  = findViewById(R.id.bottom_navigation);
       // Toast.makeText(this, MyGlobals.getKey()+","+MyGlobals.getEmail()+","+MyGlobals.getTel()+","+MyGlobals.getLatitude(), Toast.LENGTH_SHORT).show();
        getSupportFragmentManager().beginTransaction().replace(R.id.container,homeFragment).commit();

      /*  BadgeDrawable badgeDrawable = bottomNavigationView.getOrCreateBadge(R.id.Historique);
        badgeDrawable.setVisible(true);
        badgeDrawable.setNumber(8);*/

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,homeFragment).commit();
                        return true;
                    case R.id.Historique:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,historiqueFragment).commit();
                        return true;
                    case R.id.Localisation:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,mapFragment).commit();
                        return true;
                    case R.id.Compte:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,compteFragment).commit();
                        return true;
                    case R.id.LogOut:
                      //  getSupportFragmentManager().beginTransaction().replace(R.id.container,compteFragment).commit();
                        Intent intent = new Intent(MainActivity.this,PageLogin.class);
                        startActivity(intent);
                        finish();
                        MyGlobals.setEmail("");
                        MyGlobals.setTel("");
                        MyGlobals.setNom("");
                        MyGlobals.setLongitude(0);
                        MyGlobals.setLongitude(0);
                        return true;
                }

                return false;
            }
        });

    }
}