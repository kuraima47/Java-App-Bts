package com.example.maclassesdufutur;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public String img = "";
    Spinner spinnerClasses;
    ArrayList<String> classesList = new ArrayList<>();
    ArrayAdapter<String> classesAdaptater;
    Spinner spinnerEleves;
    ArrayList<String> elevesList = new ArrayList<>();
    ArrayAdapter<String> elevesAdaptater;
    String name = "";
    String prenomEleve = "";
    String classeEleve = "";
    FirebaseAuth auth;
    DatabaseReference reference;

    public String getName(){
        return name;
    }

    public String getPassword(){
        return password;
    }

    public String getPrenom(){
        return prenomEleve;
    }

    public String getClasse(){
        return classeEleve;
    }

    RequestQueue requestQueue;
    EditText eTpassword;
    String password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();

        eTpassword = findViewById(R.id.etPassword);
        requestQueue = Volley.newRequestQueue(this);
        spinnerClasses = findViewById(R.id.spinnerClasses);
        spinnerClasses.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        spinnerEleves = findViewById(R.id.spinnerEleves);

        spinnerEleves.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        String url = "http://192.168.56.1/paulcornu/classes.php";
        JsonObjectRequest jsonobjectRequest = new JsonObjectRequest(Request.Method.POST,
                url, null, response -> {
                    try {
                        JSONArray jsonArray = response.getJSONArray("classes");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonobject = jsonArray.getJSONObject(i);
                            String ClassesName = jsonobject.optString("Classe");
                            classesList.add(ClassesName);
                            classesAdaptater = new ArrayAdapter<>(MainActivity.this,
                                    android.R.layout.simple_spinner_item, classesList);
                            classesAdaptater.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnerClasses.setAdapter(classesAdaptater);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {

                });
        requestQueue.add(jsonobjectRequest);
        spinnerClasses.setOnItemSelectedListener(this);
    }

    public void Login(View view) {

        password = eTpassword.getText().toString().trim();
        String[] tab = spinnerEleves.getSelectedItem().toString().split(" ");
        String nom = tab[0];
        String prenom = tab[1];
        name = nom;
        prenomEleve = prenom;
        String URL = "http://192.168.56.1/paulcornu/login.php";

        if(!password.equals("")){
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.equals("success")) {
                        getURIdb();
                    } else if (response.equals("failure")) {
                        Toast.makeText(MainActivity.this, "Invalid Login/Password", Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(MainActivity.this, error.toString().trim(), Toast.LENGTH_SHORT).show();
                }
            }){
                @NonNull
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> data = new HashMap<>();
                    data.put("nom", nom);
                    data.put("prenom", prenom);
                    data.put("password", password);
                    data.put("classe", (String) spinnerClasses.getSelectedItem());
                    return data;
                }
            };
            RequestQueue requestQueue1 = Volley.newRequestQueue(getApplicationContext());
            requestQueue1.add(stringRequest);
        }else{
            Toast.makeText(MainActivity.this, "Champ ne peut être vide.", Toast.LENGTH_SHORT).show();
        }

    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(parent.getId() == R.id.spinnerClasses) {
            elevesList.clear();
                String selectedClasses = (String) parent.getSelectedItem();
                classeEleve = selectedClasses;
                String url = "http://192.168.56.1/paulcornu/populate_classe?classe_name=" + selectedClasses;
                requestQueue = Volley.newRequestQueue(this);
                JsonObjectRequest jsonobjectRequest = new JsonObjectRequest(Request.Method.POST,
                        url, null, response -> {
                    try {
                        JSONArray jsonArray = response.getJSONArray("eleves");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonobject = jsonArray.getJSONObject(i);
                            String ElevesName = jsonobject.optString("eleve_nom") + " " +jsonobject.optString("eleve_prenom");
                            elevesList.add(ElevesName);
                            elevesAdaptater = new ArrayAdapter<>(MainActivity.this,
                                    android.R.layout.simple_spinner_item, elevesList);
                            elevesAdaptater.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnerEleves.setAdapter(elevesAdaptater);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {

                });
                requestQueue.add(jsonobjectRequest);
                spinnerEleves.setOnItemSelectedListener(this);

        }
        ((TextView) parent.getChildAt(0)).setTextColor(getColor(R.color.white));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    public void getURIdb() {
        String uRL = "http://192.168.56.1/paulcornu/getURI?classe_name="+getClasse()+"&name="+getName()+"&prenom="+getPrenom();
        JsonObjectRequest jsonobjectRequest = new JsonObjectRequest(Request.Method.POST,
                uRL, null, response -> {
            try {
                JSONArray jsonArray = response.getJSONArray("Results");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonobject = jsonArray.getJSONObject(i);
                    if (jsonobject.optString("Uri").equals(null) || jsonobject.optString("Uri").equals("") || jsonobject.optString("Uri").equals(" ")) {
                        img = "null";
                    } else {
                        img = jsonobject.optString("Uri");
                    }
                    System.out.println(getName()+getPrenom()+getClasse()+"@paulcornu.com");
                    LoginFireBase(getPrenom() + " " + getName(),getName()+getPrenom()+getClasse()+"@paulcornu.com" ,getPassword());
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {

        });

        RequestQueue requestQueue2 = Volley.newRequestQueue(getApplicationContext());
        requestQueue2.add(jsonobjectRequest);
    }

    private void Register(String username, String email, String password){
        System.out.println("coucou");
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    FirebaseUser firebaseUser = auth.getCurrentUser();
                    String userId = firebaseUser.getUid();

                    reference = FirebaseDatabase.getInstance("https://paulcornu-efa15-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Users").child(userId);
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("id", userId);
                    hashMap.put("username", username);
                    hashMap.put ("imageURL", "default");
                    reference.setValue (hashMap).addOnCompleteListener(new OnCompleteListener<Void>(){
                        @Override
                        public void onComplete(@NonNull Task<Void> task){
                            if(task.isSuccessful()) {
                                Intent intent = new Intent(MainActivity.this, Success.class);
                                intent.putExtra("classe", getClasse());
                                intent.putExtra("nom", getName());
                                intent.putExtra("prenom", getPrenom());
                                intent.putExtra("uri", img);
                                intent.putExtra("onNotifications", "true");
                                startActivity(intent);
                                finish();
                            }
                        }
                    });
                }
            }
        });
    }

    private void LoginFireBase(String username, String email, String password){

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Intent intent = new Intent(MainActivity.this, Success.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("classe", getClasse());
                    intent.putExtra("nom", getName());
                    intent.putExtra("prenom", getPrenom());
                    intent.putExtra("uri", img);
                    intent.putExtra("onNotifications", "true");
                    startActivity(intent);
                    finish();

                }else{
                    Register(username, email, password);

                }
            }
        });
    }


}