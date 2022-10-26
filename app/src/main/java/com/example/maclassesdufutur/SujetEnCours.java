package com.example.maclassesdufutur;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class SujetEnCours extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemSelectedListener {

    Spinner spinnerTache;
    ArrayList<String> TacheList = new ArrayList<>();
    ArrayAdapter<String> TacheAdaptater;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    String classe = "";
    String nom = "";
    String prenom = "";
    String uriPath = "";
    TextView SujetText;
    RequestQueue requestQueue;
    String NomTacheEnCours;
    Button btn;
    LinearLayout lL;
    AccesLocal accesLocal;
    RunTime run;
    TextView timeView;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sujetencours);
        run = new RunTime(getApplicationContext());
        accesLocal = new AccesLocal(getApplicationContext());
        Intent intent = getIntent();
        requestQueue = Volley.newRequestQueue(this);
        spinnerTache = findViewById(R.id.spinnerTache);
        spinnerTache.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        timeView = findViewById(R.id.timeView);
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
        if(intent.hasExtra("sujet")) {
            SujetText = findViewById(R.id.activityNamed);
            SujetText.setText(intent.getStringExtra("sujet"));
        }
        drawerLayout = findViewById(R.id.drawer_layout2);
        navigationView = findViewById(R.id.nav_view2);
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar2);
        navigationView.bringToFront();
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);
        InitSpinner();
        lL = findViewById(R.id.LayoutQuestion);
        lL.setVisibility(View.GONE);
        UpdateTime();
    }

    public void InitSpinner(){
        String url = "http://192.168.56.1/paulcornu/getTache.php?classe_name="+getIntent().getStringExtra("classe")+"&prenom="+getIntent().getStringExtra("prenom")+"&name="+getIntent().getStringExtra("nom")+"&NomActivite="+getIntent().getStringExtra("sujet");
        JsonObjectRequest jsonobjectRequest = new JsonObjectRequest(Request.Method.POST,
                url, null, response -> {
            try {
                JSONArray jsonArray = response.getJSONArray("Results");

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonobject = jsonArray.getJSONObject(i);
                    String TacheName = jsonobject.optString("sujet");
                    System.out.println(TacheName);
                    TacheList.add(TacheName);
                    TacheAdaptater = new ArrayAdapter<>(SujetEnCours.this,
                            android.R.layout.simple_spinner_item, TacheList);
                    TacheAdaptater.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerTache.setAdapter(TacheAdaptater);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            System.out.println(error);
        });
        requestQueue.add(jsonobjectRequest);
        spinnerTache.setOnItemSelectedListener(this);
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
                Intent intent6 = getIntent();
                Intent intent7 = new Intent( SujetEnCours.this, Settings.class);
                intent7.putExtra("classe", intent6.getStringExtra("classe"));
                intent7.putExtra("nom", intent6.getStringExtra("nom"));
                intent7.putExtra("prenom", intent6.getStringExtra("prenom"));
                intent7.putExtra("uri", intent6.getStringExtra("uri"));
                intent7.putExtra("onNotifications", intent6.getStringExtra("onNotifications"));
                startActivity(intent7);

                finish();
                break;
            case R.id.infos:
                Intent intent = getIntent();
                Intent intent2 = new Intent( SujetEnCours.this, Infos.class);
                intent2.putExtra("classe", intent.getStringExtra("classe"));
                intent2.putExtra("nom", intent.getStringExtra("nom"));
                intent2.putExtra("prenom", intent.getStringExtra("prenom"));
                intent2.putExtra("uri", uriPath);
                intent2.putExtra("onNotifications", intent.getStringExtra("onNotifications"));
                startActivity(intent2);

                finish();
                break;
            case R.id.nav_home:
                Intent intent3 = getIntent();
                Intent intent4 = new Intent( SujetEnCours.this, Success.class);
                intent4.putExtra("classe", intent3.getStringExtra("classe"));
                intent4.putExtra("nom", intent3.getStringExtra("nom"));
                intent4.putExtra("prenom", intent3.getStringExtra("prenom"));
                intent4.putExtra("uri", uriPath);
                intent4.putExtra("onNotifications", intent3.getStringExtra("onNotifications"));
                startActivity(intent4);

                finish();
                break;
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                Intent intent5 = new Intent( SujetEnCours.this, MainActivity.class);
                startActivity(intent5);

                finish();
                Toast.makeText( this,"Disconnect", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_messagerie:
                Intent intent8 = getIntent();
                Intent intent9 = new Intent( SujetEnCours.this, messagerie.class);
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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if(parent.getId() == R.id.spinnerTache){
            getInfoTaches((String) parent.getSelectedItem());
        }
        ((TextView) parent.getChildAt(0)).setTextColor(getColor(R.color.white));
    }

    private void getInfoTaches(String it) {

        String url = "http://192.168.56.1/paulcornu/getTacheInfos.php?classe_name="+getIntent().getStringExtra("classe")+"&prenom="+getIntent().getStringExtra("prenom")+"&name="+getIntent().getStringExtra("nom")+"&NomActivite="+getIntent().getStringExtra("sujet")+"&tache="+it;
        JsonObjectRequest jsonobjectRequest = new JsonObjectRequest(Request.Method.POST,
                url, null, response -> {
            try {
                JSONArray jsonArray = response.getJSONArray("Results");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonobject = jsonArray.getJSONObject(i);
                    String TacheName = jsonobject.optString("sujet");
                    String StatsTache;
                    if(jsonobject.optString("sts").equals("enCours"))
                        StatsTache = accesLocal.getStatus(getIntent().getStringExtra("sujet"), TacheName);
                    else
                        StatsTache = jsonobject.optString("sts");
                    String temps = jsonobject.optString("time");
                    setInfosTache(TacheName, StatsTache, temps);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            System.out.println(error);
        });
        requestQueue.add(jsonobjectRequest);
    }

    private void setInfosTache(String tacheName, String statsTache, String Time) {

        TextView TacheName = findViewById(R.id.TacheName);
        TextView StatsTache = findViewById(R.id.stats);
        TextView timeView = findViewById(R.id.timeView);
        TacheName.setText(tacheName);
        StatsTache.setText(statsTache);
        Button Start = findViewById(R.id.buttonStart);
        Button Pause = findViewById(R.id.buttonPause);
        if(statsTache.equals("new")){
            Start.setVisibility(View.VISIBLE);
            Start.setText("Start");
            Pause.setVisibility(View.GONE);
            timeView.setVisibility(View.GONE);
        }else if(statsTache.equals("enCours")){
            NomTacheEnCours = tacheName;
            Start.setText("Finir");
            Start.setVisibility(View.VISIBLE);
            Pause.setVisibility(View.VISIBLE);
            Pause.setText("Pause");
            timeView.setVisibility(View.VISIBLE);
            Integer newTime = run.getTimeOfTab(accesLocal.getID(getIntent().getStringExtra("sujet"), NomTacheEnCours));
            Integer Hours = (newTime / (60 * 60));
            Integer Minutes = (newTime/ 60) % 60;
            timeView.setText(Hours + ":" + Minutes);
        }else if(statsTache.equals("pause")){
            Start.setText("Finir");
            Start.setVisibility(View.GONE);
            Pause.setVisibility(View.VISIBLE);
            Pause.setText("Resume");
            timeView.setVisibility(View.VISIBLE);
            Integer newTime = accesLocal.recupereTime(getIntent().getStringExtra("sujet"), TacheName.getText().toString());
            Integer Hours = (newTime / (60 * 60));
            Integer Minutes = (newTime/ 60) % 60;
            timeView.setText(Hours + ":" + Minutes);
        }else if(statsTache.equals("terminer")){
            Start.setVisibility(View.GONE);
            Pause.setVisibility(View.GONE);
            timeView.setVisibility(View.VISIBLE);
            timeView.setText(Time);
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void ActionStart(View view) {
        btn = (Button) view;
        lL.setVisibility(View.VISIBLE);
    }

    public void ActionPause(View view) {

        btn = (Button) view;
        lL.setVisibility(View.VISIBLE);
    }

    public void YesClick(View view) {
        Spinner spinn = findViewById(R.id.spinnerTache);
        if(btn.getText().equals("Start")){
            lL.setVisibility(View.GONE);
            StartTache((String)spinn.getSelectedItem());
            run.save();
            run.setTabTime();
        }else if(btn.getText().equals("Finir")){
            run.save();
            Integer newTime = accesLocal.recupereTime(getIntent().getStringExtra("sujet"), NomTacheEnCours);
            StopTache((String)spinn.getSelectedItem(), newTime);
            System.out.println(newTime);
            lL.setVisibility(View.GONE);
            run.setTabTime();
        }else if(btn.getText().equals("Pause")){
            run.save();
            PauseTache((String)spinn.getSelectedItem());
            lL.setVisibility(View.GONE);
            run.setTabTime();
        }else if(btn.getText().equals("Resume")){
            run.save();
            ResumeTache((String)spinn.getSelectedItem());
            lL.setVisibility(View.GONE);
            run.setTabTime();
        }else if(btn.getText().equals("Finir Activite")){
            run.save();
            FinishAllTacheEnCours();
            lL.setVisibility(View.GONE);
            run.setTabTime();
        }
    }

    private void ResumeTache(String selectedItem) {
        accesLocal.changeStatus(getIntent().getStringExtra("sujet"), selectedItem, "enCours");


        String url = "http://192.168.56.1/paulcornu/ResumeTache.php?classe_name="+getIntent().getStringExtra("classe")+"&prenom="+getIntent().getStringExtra("prenom")+"&name="+getIntent().getStringExtra("nom")+"&NomActivite="+getIntent().getStringExtra("sujet")+"&tache="+selectedItem;
        JsonObjectRequest jsonobjectRequest = new JsonObjectRequest(Request.Method.POST,
                url, null, response -> {
            try {
                JSONArray jsonArray = response.getJSONArray("Results");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonobject = jsonArray.getJSONObject(i);
                    String bool = jsonobject.optString("Response");
                    if(bool.equals("success")){
                        Toast.makeText(SujetEnCours.this, "Tache en Cours", Toast.LENGTH_SHORT).show();

                    }else if(bool.equals("failed")){
                        Toast.makeText(SujetEnCours.this, "Echec", Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            System.out.println(error);
        });
        requestQueue.add(jsonobjectRequest);
        getInfoTaches(selectedItem);
    }

    private void PauseTache(String selectedItem) {
        accesLocal.changeStatus(getIntent().getStringExtra("sujet"), selectedItem, "pause");
        String url = "http://192.168.56.1/paulcornu/PauseTache.php?classe_name="+getIntent().getStringExtra("classe")+"&prenom="+getIntent().getStringExtra("prenom")+"&name="+getIntent().getStringExtra("nom")+"&NomActivite="+getIntent().getStringExtra("sujet")+"&tache="+selectedItem;
        JsonObjectRequest jsonobjectRequest = new JsonObjectRequest(Request.Method.POST,
                url, null, response -> {
            try {
                JSONArray jsonArray = response.getJSONArray("Results");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonobject = jsonArray.getJSONObject(i);
                    String bool = jsonobject.optString("Response");
                    if(bool.equals("success")){
                        Toast.makeText(SujetEnCours.this, "Tache en Pause", Toast.LENGTH_SHORT).show();
                    }else if(bool.equals("failed")){
                        Toast.makeText(SujetEnCours.this, "Echec", Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            System.out.println(error);
        });
        requestQueue.add(jsonobjectRequest);
        getInfoTaches(selectedItem);
    }

    private void StopTache(String selectedItem, int temps) {
        String url = "http://192.168.56.1/paulcornu/StopTache.php?classe_name="+getIntent().getStringExtra("classe")+"&prenom="+getIntent().getStringExtra("prenom")+"&name="+getIntent().getStringExtra("nom")+"&NomActivite="+getIntent().getStringExtra("sujet")+"&tache="+selectedItem+"&temps="+temps;
        JsonObjectRequest jsonobjectRequest = new JsonObjectRequest(Request.Method.POST,
                url, null, response -> {
            try {
                JSONArray jsonArray = response.getJSONArray("Results");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonobject = jsonArray.getJSONObject(i);
                    String bool = jsonobject.optString("Response");
                    if(bool.equals("success")){
                        Toast.makeText(SujetEnCours.this, "Tache Arrété", Toast.LENGTH_SHORT).show();
                        accesLocal.delete(getIntent().getStringExtra("sujet"), selectedItem);
                    }else if(bool.equals("failed")){
                        Toast.makeText(SujetEnCours.this, "Echec", Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            System.out.println(error);
        });
        requestQueue.add(jsonobjectRequest);
        getInfoTaches(selectedItem);
    }

    private void StartTache(String selectedItem) {

        String url = "http://192.168.56.1/paulcornu/StartTache.php?classe_name="+getIntent().getStringExtra("classe")+"&prenom="+getIntent().getStringExtra("prenom")+"&name="+getIntent().getStringExtra("nom")+"&NomActivite="+getIntent().getStringExtra("sujet")+"&tache="+selectedItem;
        JsonObjectRequest jsonobjectRequest = new JsonObjectRequest(Request.Method.POST,
                url, null, response -> {
            try {
                JSONArray jsonArray = response.getJSONArray("Results");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonobject = jsonArray.getJSONObject(i);
                    String bool = jsonobject.optString("Response");
                    if(bool.equals("success")){
                        Toast.makeText(SujetEnCours.this, "Tache Démarré", Toast.LENGTH_SHORT).show();
                        accesLocal.ajout(getIntent().getStringExtra("sujet"), selectedItem, 0, "enCours");
                    }else if(bool.equals("failed")){
                        Toast.makeText(SujetEnCours.this, "Echec", Toast.LENGTH_SHORT).show();
                    }
                }
                run.save();
                run.setTabTime();
                getInfoTaches(selectedItem);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            System.out.println(error);
        });
        requestQueue.add(jsonobjectRequest);
    }

    public void NoClick(View view) {
        lL.setVisibility(View.GONE);
    }

    public void FinirActiviteEnCours(View view) {
        btn = (Button) view;
        lL.setVisibility(View.VISIBLE);
    }

    public void Finir(){

        String url = "http://192.168.56.1/paulcornu/StopActivite.php?classe_name="+getIntent().getStringExtra("classe")+"&prenom="+getIntent().getStringExtra("prenom")+"&name="+getIntent().getStringExtra("nom")+"&NomActivite="+getIntent().getStringExtra("sujet");
        JsonObjectRequest jsonobjectRequest = new JsonObjectRequest(Request.Method.POST,
                url, null, response -> {
            try {
                JSONArray jsonArray = response.getJSONArray("Results");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonobject = jsonArray.getJSONObject(i);
                    String bool = jsonobject.optString("Response");
                    if(bool.equals("success")){
                        Intent intent = new Intent(SujetEnCours.this, Success.class);
                        intent.putExtra("classe", getIntent().getStringExtra("classe"));
                        intent.putExtra("nom", getIntent().getStringExtra("nom"));
                        intent.putExtra("prenom", getIntent().getStringExtra("prenom"));
                        intent.putExtra("uri", getIntent().getStringExtra("uri"));
                        intent.putExtra("onNotifications", "onNotifications");
                        startActivity(intent);
                        finish();
                        Toast.makeText(SujetEnCours.this, "Activite Arrété", Toast.LENGTH_SHORT).show();
                    }else if(bool.equals("failed")){
                        Toast.makeText(SujetEnCours.this, "Echec", Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            System.out.println(error);
        });
        requestQueue.add(jsonobjectRequest);
    }

    public void FinishAllTacheEnCours(){

        String[][] allTimeFinsh = accesLocal.StopAllActivite(getIntent().getStringExtra("sujet"));
        for (String[] t : allTimeFinsh){
            StopTache(t[0], Integer.parseInt(t[1]));
        }
        Finir();
    }

    public void UpdateTime() {
        run.setTabTime();
        Thread t = new Thread() {
            @Override
            public void run() {
                while (!isInterrupted()) {
                    try {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(((TextView) findViewById(R.id.stats)).getText().toString().equals("enCours")) {
                                    Integer newTime = run.getTimeOfTab(accesLocal.getID(getIntent().getStringExtra("sujet"), NomTacheEnCours));
                                    Integer Hours = (newTime / (60 * 60));
                                    Integer Minutes = (newTime / 60) % 60;
                                    timeView.setText(Hours + ":" + Minutes);
                                }
                            }
                        });

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        t.start();

    }
}
