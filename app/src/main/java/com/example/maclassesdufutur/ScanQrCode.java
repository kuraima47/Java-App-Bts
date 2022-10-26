package com.example.maclassesdufutur;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.StringBufferInputStream;

public class ScanQrCode extends AppCompatActivity {

    private CodeScanner mCodeScanner;
    TextView textView;
    TextView InfosScan;
    TextView InfosScan2;
    TextView ScanVerif;
    Button buttonYes;
    Button buttonNo;
    String sujet = "";
    String status = "";
    String ActionButton = "";
    Integer IdTache;
    String NomActivite = "";
    RequestQueue requestQueue;
    String TacheEnCours = "";
    String ActiviteEnCours = "";
    Boolean statsActivite;
    String statsTaches = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scanqrcode);
        Intent intent3 = getIntent();
        sujet = intent3.getStringExtra("sujet");
        status = intent3.getStringExtra("status");
        setupPermissions();
        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        textView = findViewById(R.id.tv_textViewScan);
        InfosScan = findViewById(R.id.tv_Scaninfos);
        InfosScan2 = findViewById(R.id.tv_Scaninfos2);
        ScanVerif = findViewById(R.id.tv_ScanVerifText);
        buttonNo = findViewById(R.id.ButtonNo);
        buttonYes = findViewById(R.id.ButtonYess);
        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run()
                    {
                        textView.setText(result.getText());
                        if(status.equals("tache")){
                            String[] informationsQrCode = result.getText().split("|");
                            if(informationsQrCode[0].equals("activite")){
                                Toast.makeText(ScanQrCode.this, "ceci n'est pas une tâche", Toast.LENGTH_SHORT).show();
                            }else if(informationsQrCode[0].equals("tache")){

                                getNomTacheEnCours(sujet);
                                if(TacheEnCours.equals(informationsQrCode[2])){
                                    textView.setText("Tache identique");
                                    InfosScan.setVisibility(View.GONE);
                                    InfosScan2.setVisibility(View.VISIBLE);
                                    InfosScan2.setText(" Tâche : "+ informationsQrCode[2] +" \\n Numéro Tâches : " + Integer.parseInt(informationsQrCode[1]));
                                    ScanVerif.setVisibility(View.VISIBLE);
                                    ScanVerif.setText("Voulez vous arreter la tâche " + Integer.parseInt(informationsQrCode[1]) + " ?");
                                    buttonNo.setVisibility(View.VISIBLE);
                                    buttonNo.setText("Non");
                                    buttonYes.setVisibility(View.VISIBLE);
                                    buttonYes.setText("Oui");
                                    ActionButton = "StopTache";
                                    IdTache = Integer.parseInt(informationsQrCode[1]);
                                }else{
                                    if(Integer.parseInt(informationsQrCode[1]) > 1){
                                        findStatusTache(Integer.parseInt(informationsQrCode[1])-1);
                                        if(statsTaches.equals("new")){
                                            //Affiche Demande pour scanner la premiere tache
                                            textView.setText("Scanner la tâche précédentes");
                                            InfosScan.setVisibility(View.GONE);
                                            InfosScan2.setVisibility(View.GONE);
                                            ScanVerif.setVisibility(View.GONE);
                                            buttonNo.setVisibility(View.GONE);
                                            buttonYes.setVisibility(View.GONE);
                                        }else if(statsTaches.equals("enCours")){
                                            //Affiche demande pour fermer la tache
                                            textView.setText("Scan Trouvé");
                                            InfosScan.setVisibility(View.GONE);
                                            InfosScan2.setVisibility(View.VISIBLE);
                                            InfosScan2.setText(" Tâche : "+ informationsQrCode[2] +" \\n Numéro Tâches : " + Integer.parseInt(informationsQrCode[1]));
                                            ScanVerif.setVisibility(View.VISIBLE);
                                            ScanVerif.setText("Voulez vous arreter la tâche en cours et démarrer la tâche " + Integer.parseInt(informationsQrCode[1]) + " ?");
                                            buttonNo.setVisibility(View.VISIBLE);
                                            buttonNo.setText("Non");
                                            buttonYes.setVisibility(View.VISIBLE);
                                            buttonYes.setText("Oui");
                                            ActionButton = "Stop&StartTache";
                                            IdTache = Integer.parseInt(informationsQrCode[1]);
                                        }else if(statsTaches.equals("terminer") || statsTaches.equals("incomplet")){
                                            // demande de confirmation pour debuter la page
                                            textView.setText("Scan Trouvé");
                                            InfosScan.setVisibility(View.GONE);
                                            InfosScan2.setVisibility(View.VISIBLE);
                                            InfosScan2.setText(" Tâche : "+ informationsQrCode[2] +" \\n Numéro Tâches : " + Integer.parseInt(informationsQrCode[1]));
                                            ScanVerif.setVisibility(View.VISIBLE);
                                            ScanVerif.setText("Voulez vous démarrer la tâche " + Integer.parseInt(informationsQrCode[1]) + " ?");
                                            buttonNo.setVisibility(View.VISIBLE);
                                            buttonNo.setText("Non");
                                            buttonYes.setVisibility(View.VISIBLE);
                                            buttonYes.setText("Oui");
                                            ActionButton = "StartTache";
                                            IdTache = Integer.parseInt(informationsQrCode[1]);
                                        }
                                    }else{
                                        //afficher fenetre pour demarrer la tache
                                        textView.setText("Scan Trouvé");
                                        InfosScan.setVisibility(View.GONE);
                                        InfosScan2.setVisibility(View.VISIBLE);
                                        InfosScan2.setText(" Tâche : "+ informationsQrCode[2] +" \\n Numéro Tâches : " + Integer.parseInt(informationsQrCode[1]));
                                        ScanVerif.setVisibility(View.VISIBLE);
                                        ScanVerif.setText("Voulez vous démarrer la tâche " + Integer.parseInt(informationsQrCode[1]) + " ?");
                                        buttonNo.setVisibility(View.VISIBLE);
                                        buttonNo.setText("Non");
                                        buttonYes.setVisibility(View.VISIBLE);
                                        buttonYes.setText("Oui");
                                        ActionButton = "newTache";
                                        IdTache = Integer.parseInt(informationsQrCode[1]);
                                    }
                                }
                            }
                        }
                        else if(status.equals("Activite")){
                            String[] informationsQrCode = result.getText().split("|");
                            if(informationsQrCode[0].equals("tache")){
                                Toast.makeText(ScanQrCode.this, "ceci n'est pas une Activité", Toast.LENGTH_SHORT).show();
                            }else if(informationsQrCode[0].equals("activite")){

                                getNomActiviteEnCours();
                                if(ActiviteEnCours.equals(informationsQrCode[1])){
                                    textView.setText("Scan Trouvé");
                                    InfosScan.setVisibility(View.VISIBLE);
                                    InfosScan.setText(" Activity: " + informationsQrCode[1] + " \\n Nombre de Tâches : " + informationsQrCode[2] + " \\n Professeur : " + informationsQrCode[3] + " \\n Matière : " + informationsQrCode[4]);
                                    InfosScan2.setVisibility(View.GONE);
                                    ScanVerif.setVisibility(View.VISIBLE);
                                    ScanVerif.setText("Voulez vous arreter l'activite en cours et démarrer l'activite " + "Nom" + " ?");
                                    buttonNo.setVisibility(View.VISIBLE);
                                    buttonNo.setText("Non");
                                    buttonYes.setVisibility(View.VISIBLE);
                                    buttonYes.setText("Oui");
                                    ActionButton = "StopActivite";
                                }else {
                                    findStatusActivite();
                                    if (statsActivite) {
                                        textView.setText("Scan Trouvé");
                                        InfosScan.setVisibility(View.VISIBLE);
                                        InfosScan.setText(" Activity: " + informationsQrCode[1] + " \\n Nombre de Tâches : " + informationsQrCode[2] + " \\n Professeur : " + informationsQrCode[3] + " \\n Matière : " + informationsQrCode[4]);
                                        InfosScan2.setVisibility(View.GONE);
                                        ScanVerif.setVisibility(View.VISIBLE);
                                        ScanVerif.setText("Voulez vous arreter l'activite en cours et démarrer l'activite " + "Nom" + " ?");
                                        buttonNo.setVisibility(View.VISIBLE);
                                        buttonNo.setText("Non");
                                        buttonYes.setVisibility(View.VISIBLE);
                                        buttonYes.setText("Oui");
                                        ActionButton = "Stop&StartActivite";
                                        NomActivite = informationsQrCode[1];
                                    }else
                                    {
                                        textView.setText("Scan Trouvé");
                                        InfosScan.setVisibility(View.VISIBLE);
                                        InfosScan.setText(" Activity: " + informationsQrCode[1] + " \\n Nombre de Tâches : " + informationsQrCode[2] + " \\n Professeur : " + informationsQrCode[3] + " \\n Matière : " + informationsQrCode[4]);
                                        InfosScan2.setVisibility(View.GONE);
                                        ScanVerif.setVisibility(View.VISIBLE);
                                        ScanVerif.setText("Voulez vous démarrer l'activite " + "Nom" + " ?");
                                        buttonNo.setVisibility(View.VISIBLE);
                                        buttonNo.setText("Non");
                                        buttonYes.setVisibility(View.VISIBLE);
                                        buttonYes.setText("Oui");
                                        ActionButton = "StartActivite";
                                        NomActivite = informationsQrCode[1];
                                    }
                                }
                            }
                        }
                    }
                });
            }
        });
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        });

    }

    public void getNomTacheEnCours(String sujetActivite){
        String url = "http://localhost/paulcornu/getTacheEnCours.php?classe_name="+getIntent().getStringExtra("classe")+"&prenom="+getIntent().getStringExtra("prenom")+"&name="+getIntent().getStringExtra("nom")+"&NomActivite="+sujetActivite;
        JsonObjectRequest jsonobjectRequest = new JsonObjectRequest(Request.Method.POST,
                url, null, response -> {
            try {
                JSONArray jsonArray = response.getJSONArray("Results");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonobject = jsonArray.getJSONObject(i);
                    TacheEnCours = jsonobject.optString("tache");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {

        });
        requestQueue.add(jsonobjectRequest);

    }

    public void getNomActiviteEnCours(){
        String url = "http://localhost/paulcornu/GetEnCours.php?classe_name="+getIntent().getStringExtra("classe")+"&prenom="+getIntent().getStringExtra("prenom")+"&name="+getIntent().getStringExtra("nom");
        JsonObjectRequest jsonobjectRequest = new JsonObjectRequest(Request.Method.POST,
                url, null, response -> {
            try {
                JSONArray jsonArray = response.getJSONArray("Results");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonobject = jsonArray.getJSONObject(i);
                    ActiviteEnCours = jsonobject.optString("sujet");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {

        });
        requestQueue.add(jsonobjectRequest);
    }

    public void findStatusActivite() {
        String url = "http://localhost/paulcornu/FindActiviteEnCours.php?classe_name="+getIntent().getStringExtra("classe")+"&prenom="+getIntent().getStringExtra("prenom")+"&name="+getIntent().getStringExtra("nom");
        JsonObjectRequest jsonobjectRequest = new JsonObjectRequest(Request.Method.POST,
                url, null, response -> {
            try {
                JSONArray jsonArray = response.getJSONArray("Results");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonobject = jsonArray.getJSONObject(i);
                    String a = jsonobject.optString("Response");
                    if(response.equals("true"))
                        statsActivite = true;
                    else if(response.equals("false"))
                        statsActivite = false;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {

        });
        requestQueue.add(jsonobjectRequest);
    }

    public void findStatusTache(int numtache){
        String url = "http://localhost/paulcornu/FindStatsTache.php?classe_name="+getIntent().getStringExtra("classe")+"&prenom="+getIntent().getStringExtra("prenom")+"&name="+getIntent().getStringExtra("nom")+"&idTache="+numtache+"&NomActivite="+sujet;
        JsonObjectRequest jsonobjectRequest = new JsonObjectRequest(Request.Method.POST,
                url, null, response -> {
            try {
                JSONArray jsonArray = response.getJSONArray("Results");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonobject = jsonArray.getJSONObject(i);
                    statsTaches = jsonobject.optString("tache");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {

        });
        requestQueue.add(jsonobjectRequest);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }

    private void setupPermissions(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            makeRequest();
        }
    }

    private void makeRequest() {
        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, 101);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        while (requestCode == 101) {
            if ((grantResults == null) || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "you need to accept the camera permissions for use this app", Toast.LENGTH_SHORT).show();
            } else {
                //Successful
            }
        }
    }

    public void demarrerTache(String sujetActivite, int numTache){
        //changer le status de la tache avec le nom de l'activité correspondant
        String url = "http://localhost/paulcornu/StartTache.php?classe_name="+getIntent().getStringExtra("classe")+"&prenom="+getIntent().getStringExtra("prenom")+"&name="+getIntent().getStringExtra("nom")+"&tacheNum="+numTache;
        JsonObjectRequest jsonobjectRequest = new JsonObjectRequest(Request.Method.POST,
                url, null, response -> {
            try {
                JSONArray jsonArray = response.getJSONArray("Results");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonobject = jsonArray.getJSONObject(i);
                    String a = jsonobject.optString("Response");
                    if(a.equals("failed"))
                        Toast.makeText(ScanQrCode.this, "echec pour démarrer la tâche", Toast.LENGTH_SHORT).show();
                    else if(a.equals("success"))
                        Toast.makeText(ScanQrCode.this, "tâche démarré avec succès", Toast.LENGTH_SHORT).show();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {

        });
        requestQueue.add(jsonobjectRequest);
    }

    public void terminerTache(){
        //terminer la tache de l'activité en cours
        String url = "http://localhost/paulcornu/StopTache.php?classe_name="+getIntent().getStringExtra("classe")+"&prenom="+getIntent().getStringExtra("prenom")+"&name="+getIntent().getStringExtra("nom")+"&Activite="+sujet;
        JsonObjectRequest jsonobjectRequest = new JsonObjectRequest(Request.Method.POST,
                url, null, response -> {
            try {
                JSONArray jsonArray = response.getJSONArray("Results");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonobject = jsonArray.getJSONObject(i);
                    String a = jsonobject.optString("Response");
                    if(a.equals("failed"))
                        Toast.makeText(ScanQrCode.this, "echec pour arreter la tâche", Toast.LENGTH_SHORT).show();
                    else if(a.equals("success"))
                        Toast.makeText(ScanQrCode.this, "tâche terminé avec succès", Toast.LENGTH_SHORT).show();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {

        });
        requestQueue.add(jsonobjectRequest);
    }

    public void demarrerActivite(String sujetActivite){
        //change le status de l'activité rentré en paramètre en "enCours"
        String url = "http://localhost/paulcornu/StartActivite.php?classe_name="+getIntent().getStringExtra("classe")+"&prenom="+getIntent().getStringExtra("prenom")+"&name="+getIntent().getStringExtra("nom")+"&activiteName="+sujetActivite;
        JsonObjectRequest jsonobjectRequest = new JsonObjectRequest(Request.Method.POST,
                url, null, response -> {
            try {
                JSONArray jsonArray = response.getJSONArray("Results");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonobject = jsonArray.getJSONObject(i);
                    String a = jsonobject.optString("Response");
                    if(a.equals("failed"))
                        Toast.makeText(ScanQrCode.this, "echec pour démarrer l'activite", Toast.LENGTH_SHORT).show();
                    else if(a.equals("success"))
                        Toast.makeText(ScanQrCode.this, "activite démarré avec succès", Toast.LENGTH_SHORT).show();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {

        });
        requestQueue.add(jsonobjectRequest);
    }

    public void terminerActivite(){
        //cherche activité en cours pour l'élève et change son status
        String url = "http://localhost/paulcornu/StopActivite.php?classe_name="+getIntent().getStringExtra("classe")+"&prenom="+getIntent().getStringExtra("prenom")+"&name="+getIntent().getStringExtra("nom");
        JsonObjectRequest jsonobjectRequest = new JsonObjectRequest(Request.Method.POST,
                url, null, response -> {
            try {
                JSONArray jsonArray = response.getJSONArray("Results");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonobject = jsonArray.getJSONObject(i);
                    String a = jsonobject.optString("Response");
                    if(a.equals("failed"))
                        Toast.makeText(ScanQrCode.this, "echec pour arreter l'activite", Toast.LENGTH_SHORT).show();
                    else if(a.equals("success"))
                        Toast.makeText(ScanQrCode.this, "activite terminé avec succès", Toast.LENGTH_SHORT).show();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, error -> {

        });
        requestQueue.add(jsonobjectRequest);
    }

    public void Leave(View view) {
        if(status.equals("tache"))
            goActivityEnCours();
        else if(status.equals("Activite"))
            goHome();

    }

    public void goHome(){
        Intent intent = getIntent();
        Intent intent2 = new Intent(ScanQrCode.this, Success.class);
        intent2.putExtra("classe", intent.getStringExtra("classe"));
        intent2.putExtra("nom", intent.getStringExtra("nom"));
        intent2.putExtra("prenom", intent.getStringExtra("prenom"));
        intent2.putExtra("uri", intent.getStringExtra("uri"));
        startActivity(intent2);
        finish();
    }

    public void goActivityEnCours(){
        Intent intent5 = getIntent();
        Intent intent6 = new Intent(ScanQrCode.this, SujetEnCours.class);
        intent6.putExtra("classe", intent5.getStringExtra("classe"));
        intent6.putExtra("nom", intent5.getStringExtra("nom"));
        intent6.putExtra("prenom", intent5.getStringExtra("prenom"));
        intent6.putExtra("sujet", sujet);
        intent6.putExtra("uri", intent5.getStringExtra("uri"));
        startActivity(intent6);
        finish();
    }

    public void Yes(View view) {
        if(ActionButton.equals("newTache")){
            demarrerTache(sujet, IdTache);
        }else if(ActionButton.equals("Stop&StartTache")){
            terminerTache();
            demarrerTache(sujet, IdTache);
        }else if(ActionButton.equals("StartTache")){
            demarrerTache(sujet, IdTache);
        }else if(ActionButton.equals("Stop&StartActivite")){
            terminerActivite();
            demarrerActivite(NomActivite);
        }else if(ActionButton.equals("StartActivite")){
            demarrerActivite(NomActivite);
        }else if(ActionButton.equals("StopTache")){
            terminerTache();
        }else if(ActionButton.equals("StopActivite")){
            terminerActivite();
        }
        textView.setText("Scan something...");
        InfosScan.setVisibility(View.GONE);
        InfosScan2.setVisibility(View.GONE);
        ScanVerif.setVisibility(View.GONE);
        buttonNo.setVisibility(View.GONE);
        buttonYes.setVisibility(View.GONE);
    }

    public void No(View view) {
        textView.setText("Scan something...");
        InfosScan.setVisibility(View.GONE);
        InfosScan2.setVisibility(View.GONE);
        ScanVerif.setVisibility(View.GONE);
        buttonNo.setVisibility(View.GONE);
        buttonYes.setVisibility(View.GONE);
    }
}
