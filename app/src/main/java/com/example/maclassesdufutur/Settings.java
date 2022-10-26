package com.example.maclassesdufutur;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class Settings extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    String classe = "";
    String nom = "";
    String prenom = "";
    String uriPath = "";
    Switch aSwitch;
    String boolNotif;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settingspage);

        Intent intent = getIntent();

        if (intent.hasExtra("classe")){
            classe = intent.getStringExtra("classe");
        }

        if (intent.hasExtra("nom")){
            nom = intent.getStringExtra("nom");
        }

        if (intent.hasExtra("prenom")){
            prenom = intent.getStringExtra("prenom");
        }
        if (intent.hasExtra("uri")){
            uriPath = intent.getStringExtra("uri");
        }
        aSwitch = findViewById(R.id.switch_notif);
        if(intent.getStringExtra("onNotifications").equals("true")){
            aSwitch.setChecked(true);
        }else{
            aSwitch.setChecked(false);
        }


        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        navigationView.bringToFront();
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    boolNotif = "true";
                } else {
                    boolNotif = "false";
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        getMenuInflater().inflate(R.menu.menuheader, menu);
        return true;
    }

    @Override
    public void onBackPressed () {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_Settings:
                break;
            case R.id.infos:
                Intent intent = getIntent();
                Intent intent2 = new Intent( Settings.this, Infos.class);
                intent2.putExtra("classe", intent.getStringExtra("classe"));
                intent2.putExtra("nom", intent.getStringExtra("nom"));
                intent2.putExtra("prenom", intent.getStringExtra("prenom"));
                intent2.putExtra("uri", uriPath);
                intent2.putExtra("onNotifications", boolNotif);
                startActivity(intent2);

                finish();
                break;
            case R.id.nav_home:
                Intent intent3 = getIntent();
                Intent intent4 = new Intent( Settings.this, Success.class);
                intent4.putExtra("classe", intent3.getStringExtra("classe"));
                intent4.putExtra("nom", intent3.getStringExtra("nom"));
                intent4.putExtra("prenom", intent3.getStringExtra("prenom"));
                intent4.putExtra("onNotifications", boolNotif);
                intent4.putExtra("uri", uriPath);
                startActivity(intent4);

                finish();
                break;
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                Intent intent5 = new Intent( Settings.this, MainActivity.class);
                startActivity(intent5);

                finish();
                Toast.makeText( this,"Disconnect", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_messagerie:
                Intent intent8 = getIntent();
                Intent intent9 = new Intent( Settings.this, messagerie.class);
                intent9.putExtra("classe", intent8.getStringExtra("classe"));
                intent9.putExtra("nom", intent8.getStringExtra("nom"));
                intent9.putExtra("prenom", intent8.getStringExtra("prenom"));
                intent9.putExtra("uri", intent8.getStringExtra("uri"));
                intent9.putExtra("onNotifications", intent8.getStringExtra("onNotifications"));
                startActivity(intent9);

                finish();
                break;
        }
        drawerLayout.closeDrawer (GravityCompat.START);
        return true;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
