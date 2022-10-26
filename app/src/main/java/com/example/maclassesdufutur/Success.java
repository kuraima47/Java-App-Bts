package com.example.maclassesdufutur;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsoluteLayout;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationBarItemView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class Success extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemSelectedListener {

    //variables

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ArrayList<String> EnCoursList = new ArrayList<>();
    ArrayList<TextView> textTime = new ArrayList<>();
    ArrayList<String> debutTimeList = new ArrayList<>();
    RequestQueue requestQueue;
    String classe = "";
    String nom = "";
    String prenom = "";
    TextView textEnCours;
    int numEnCours = 0;
    int numEnleve = 0;
    String textSujet = "";
    RunTime run;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainmenu);
        requestQueue = Volley.newRequestQueue(this);
        run = new RunTime(getApplicationContext());
        run.setTabTime();
        run.UpdateTime();
        textEnCours = findViewById(R.id.timeEnCours);
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
        initList();

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

    private void setSupportActionBar(Toolbar toolbar) {
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId ()) {
            case R.id.nav_home:
                break;
            case R.id.nav_Settings:
                Intent intent5 = getIntent();
                Intent intent4 = new Intent( Success.this, Settings.class);
                intent4.putExtra("classe", intent5.getStringExtra("classe"));
                intent4.putExtra("nom", intent5.getStringExtra("nom"));
                intent4.putExtra("prenom", intent5.getStringExtra("prenom"));
                intent4.putExtra("uri", intent5.getStringExtra("uri"));
                intent4.putExtra("onNotifications", intent5.getStringExtra("onNotifications"));
                startActivity(intent4);

                finish();
                break;
            case R.id.infos:
                Intent intent = getIntent();
                Intent intent2 = new Intent( Success.this, Infos.class);
                intent2.putExtra("classe", intent.getStringExtra("classe"));
                intent2.putExtra("nom", intent.getStringExtra("nom"));
                intent2.putExtra("prenom", intent.getStringExtra("prenom"));
                intent2.putExtra("uri", intent.getStringExtra("uri"));
                intent2.putExtra("onNotifications", intent.getStringExtra("onNotifications"));
                startActivity(intent2);

                finish();
                break;
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                Intent intent3 = new Intent( Success.this, MainActivity.class);
                startActivity(intent3);

                finish();
                Toast.makeText( this,"Disconnect", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_messagerie:
                Intent intent8 = getIntent();
                Intent intent9 = new Intent( Success.this, messagerie.class);
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

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    public void OpenDetailsEnCours(View view) {

        Button btn = (Button)view;
        StringBuilder sujetEnCours = new StringBuilder((String) btn.getText());
        sujetEnCours = sujetEnCours.deleteCharAt(0);
        sujetEnCours = sujetEnCours.deleteCharAt(0);
        Intent intent = getIntent();
        Intent intent2 = new Intent(Success.this, SujetEnCours.class);
        intent2.putExtra("classe", intent.getStringExtra("classe"));
        intent2.putExtra("nom", intent.getStringExtra("nom"));
        intent2.putExtra("prenom", intent.getStringExtra("prenom"));
        intent2.putExtra("uri", intent.getStringExtra("uri"));
        intent2.putExtra("sujet", sujetEnCours.toString());
        intent2.putExtra("onNotifications", intent.getStringExtra("onNotifications"));
        startActivity(intent2);

        finish();

    }

    public void UpdateTime(){
        //UpdateTime
        Thread t = new Thread() {
            @Override
            public void run() {
                while (!isInterrupted()) {
                    try {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Date curentDate = new Date();
                                Calendar calendar=Calendar.getInstance();
                                calendar.setTime(curentDate);
                                SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                try {
                                    for(int i = 0; i < numEnCours - numEnleve; i++){
                                        Date StartTime = s.parse(debutTimeList.get(i));
                                        Date EndTime = s.parse(s.format(curentDate));
                                        long differenceInMilliSeconds = StartTime.getTime() - EndTime.getTime();
                                            long Days = (differenceInMilliSeconds / (60 * 60 * 24 * 1000)) % 30;
                                            long Hours = (differenceInMilliSeconds / (60 * 60 * 1000)) % 24;
                                            long Minutes = (differenceInMilliSeconds / (60 * 1000)) % 60;
                                            long Seconds = (differenceInMilliSeconds / (1000)) % 60;
                                            String numDay = "";
                                            long one = 1;
                                            if(Days > 7)
                                                Days-=2;
                                        if(differenceInMilliSeconds > 0) {
                                            if (Days >= one) {
                                                numDay = "days ";

                                                textTime.get(i).setText("enCours :\n" + Days + numDay + Hours + ":" + Minutes + ":" + Seconds);
                                            }
                                            else if (Days == one) {
                                                numDay = "day ";
                                                textTime.get(i).setText("enCours :\n" + Days + numDay + Hours + ":" + Minutes + ":" + Seconds);
                                            } else if(Days < one){
                                                textTime.get(i).setText("enCours :\n" + Hours + ":" + Minutes + ":" + Seconds);
                                            }
                                        }else if(differenceInMilliSeconds <= 0){
                                            StopActivite(EnCoursList.get(i));
                                            EnCoursList.remove(i);
                                            textTime.remove(i);
                                            debutTimeList.remove(i);
                                            numEnleve++;
                                        }
                                    }
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        t.start();
    }

    public void initList(){
        getActiviteEnCours();
    }

    private void getActiviteNew() {

        String url = "http://192.168.56.1/paulcornu/getActiviteNew.php?classe_name="+getIntent().getStringExtra("classe")+"&prenom="+getIntent().getStringExtra("prenom")+"&name="+getIntent().getStringExtra("nom");
        JsonObjectRequest jsonobjectRequest = new JsonObjectRequest(Request.Method.POST,
                url, null, response -> {
            try {
                JSONArray jsonArray = response.getJSONArray("Results");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonobject = jsonArray.getJSONObject(i);
                    if(jsonobject.optString("nb").equals("1")) {
                        String infos = jsonobject.optString("sujet");
                        addNew(infos);
                    }
                    else if(jsonobject.optString("nb").equals("0")){
                        break;
                    }
                }
                getActiviteTerminer();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            getActiviteTerminer();
        });
        requestQueue.add(jsonobjectRequest);
    }

    private void addNew(String infos) {

        numEnCours++;
        numEnleve++;
        RelativeLayout constraintLayout = new RelativeLayout(this);
        RelativeLayout.LayoutParams cl = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        constraintLayout.setLayoutParams(cl);
        constraintLayout.setForegroundGravity(Gravity.CENTER);
        TextView tv = new TextView(this);
        LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
        linearParams.setMargins(0, 40, 40, 0);
        tv.setLayoutParams(linearParams);
        tv.requestLayout();
        tv.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
        tv.setTextColor(getColor(R.color.white));
        tv.setTextSize(15);
        tv.setText("new");
        tv.setTypeface(Typeface.DEFAULT_BOLD);
        Button button = new Button(this);
        RelativeLayout.LayoutParams btn = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,150);
        btn.setMargins(10,0,10,0);
        button.setLayoutParams(btn);
        button.setTextColor(getColor(R.color.white));
        button.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        button.setTextSize(18);
        button.setAllCaps(false);
        button.setTypeface(Typeface.DEFAULT_BOLD);
        button.setBackgroundColor(getColor(R.color.transparent));
        button.setText((numEnCours)+" "+infos);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                StartActivite(v);
            }
        });
        textTime.add(tv);
        constraintLayout.addView(button);
        constraintLayout.addView(tv);
        LinearLayout ln = findViewById(R.id.List);
        ln.addView(constraintLayout);

    }

    private void StartActivite(View view) {

        Button act = (Button) view;
        PopUp popUp = new PopUp(this);
        StringBuilder sujetEnCours = new StringBuilder((String) act.getText());
        sujetEnCours = sujetEnCours.deleteCharAt(0);
        sujetEnCours = sujetEnCours.deleteCharAt(0);
        textSujet = sujetEnCours.toString();
        popUp.setQuestion(sujetEnCours.toString());
        popUp.getYesButton().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DepartActivite(textSujet);
            }
        });
        popUp.getNoButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popUp.dismiss();
            }
        });
        popUp.build();
    }

    public void StopActivite(String suj){
        String url = "http://192.168.56.1/paulcornu/StopActivite.php?classe_name="+getIntent().getStringExtra("classe")+"&prenom="+getIntent().getStringExtra("prenom")+"&name="+getIntent().getStringExtra("nom")+"&NomActivite="+suj;
        JsonObjectRequest jsonobjectRequest = new JsonObjectRequest(Request.Method.POST,
                url, null, response -> {

            try {
                JSONArray jsonArray = response.getJSONArray("Results");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonobject = jsonArray.getJSONObject(i);
                    String bool = jsonobject.optString("Response");
                    if(bool.equals("success")){
                        Intent intent = getIntent();
                        Intent intent2 = new Intent(Success.this, Success.class);
                        intent2.putExtra("classe", intent.getStringExtra("classe"));
                        intent2.putExtra("nom", intent.getStringExtra("nom"));
                        intent2.putExtra("prenom", intent.getStringExtra("prenom"));
                        intent2.putExtra("uri", intent.getStringExtra("uri"));
                        intent2.putExtra("onNotifications", intent.getStringExtra("onNotifications"));
                        startActivity(intent2);

                        finish();
                        Toast.makeText(Success.this, "Activite Fini", Toast.LENGTH_SHORT).show();
                    }else if(bool.equals("failed")){
                        Toast.makeText(Success.this, "Echec", Toast.LENGTH_SHORT).show();
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

    private void DepartActivite(String textSujet) {
        String url = "http://192.168.56.1/paulcornu/StartActivite.php?classe_name="+getIntent().getStringExtra("classe")+"&prenom="+getIntent().getStringExtra("prenom")+"&name="+getIntent().getStringExtra("nom")+"&NomActivite="+textSujet;
        JsonObjectRequest jsonobjectRequest = new JsonObjectRequest(Request.Method.POST,
                url, null, response -> {
            try {
                JSONArray jsonArray = response.getJSONArray("Results");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonobject = jsonArray.getJSONObject(i);
                    String bool = jsonobject.optString("Response");
                    System.out.println(bool);
                    if(bool.equals("success")){
                        Intent intent = getIntent();
                        Intent intent2 = new Intent(Success.this, SujetEnCours.class);
                        intent2.putExtra("classe", intent.getStringExtra("classe"));
                        intent2.putExtra("nom", intent.getStringExtra("nom"));
                        intent2.putExtra("prenom", intent.getStringExtra("prenom"));
                        intent2.putExtra("uri", intent.getStringExtra("uri"));
                        intent2.putExtra("sujet", textSujet);
                        intent2.putExtra("onNotifications", intent.getStringExtra("onNotifications"));
                        startActivity(intent2);
                        finish();
                        Toast.makeText(Success.this, "Activite Démarré", Toast.LENGTH_SHORT).show();
                    }else if(bool.equals("failed")){
                        Toast.makeText(Success.this, "Echec", Toast.LENGTH_SHORT).show();
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

    private void getActiviteEnCours() {

        String url = "http://192.168.56.1/paulcornu/getActiviteEnCours.php?classe_name="+getIntent().getStringExtra("classe")+"&prenom="+getIntent().getStringExtra("prenom")+"&name="+getIntent().getStringExtra("nom");
        JsonObjectRequest jsonobjectRequest = new JsonObjectRequest(Request.Method.POST,
                url, null, response -> {
            try {
                JSONArray jsonArray = response.getJSONArray("Results");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonobject = jsonArray.getJSONObject(i);
                    if(jsonobject.optString("nb").equals("1")) {
                        String infos = jsonobject.optString("sujet");
                        String date = jsonobject.optString("date");
                        EnCoursList.add(infos);
                        addEnCours(infos, date);

                    }else if(jsonobject.optString("nb").equals("0")){
                        break;
                    }
                }
                getActiviteNew();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            getActiviteNew();
        });
        requestQueue.add(jsonobjectRequest);
    }

    private void addEnCours(String i, String date) {
        numEnCours++;
        debutTimeList.add(date);
        RelativeLayout constraintLayout = new RelativeLayout(this);
        RelativeLayout.LayoutParams cl = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                constraintLayout.setLayoutParams(cl);
                constraintLayout.setForegroundGravity(Gravity.CENTER);
        TextView tv = new TextView(this);
        LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
        linearParams.setMargins(0, 20, 40, 0);
        tv.setLayoutParams(linearParams);
        tv.requestLayout();
                tv.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                tv.setTextColor(getColor(R.color.white));
                tv.setTextSize(15);
                tv.setTypeface(Typeface.DEFAULT_BOLD);
        Button button = new Button(this);
        RelativeLayout.LayoutParams btn = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,150);
                btn.setMargins(10,0,10,0);
                button.setLayoutParams(btn);
                button.setTextColor(getColor(R.color.white));
                button.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                button.setTextSize(18);
                button.setAllCaps(false);
                button.setTypeface(Typeface.DEFAULT_BOLD);
                button.setBackgroundColor(getColor(R.color.transparent));
                button.setText((numEnCours)+" "+i);
                button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                OpenDetailsEnCours(v);
            }
        });
                textTime.add(tv);
                constraintLayout.addView(tv);
                constraintLayout.addView(button);
        LinearLayout ln = findViewById(R.id.LayoutSujet);
        ln.addView(constraintLayout);
        UpdateTime();
    }

    public void getActiviteTerminer(){
        String url = "http://192.168.56.1/paulcornu/getActiviteTerm.php?classe_name="+getIntent().getStringExtra("classe")+"&prenom="+getIntent().getStringExtra("prenom")+"&name="+getIntent().getStringExtra("nom");
        JsonObjectRequest jsonobjectRequest = new JsonObjectRequest(Request.Method.GET,
                url, null, response -> {
            try {
                JSONArray jsonArray = response.getJSONArray("Results");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonobject = jsonArray.getJSONObject(i);
                    if(jsonobject.optString("nb").equals("1")) {
                        String infos = jsonobject.optString("sujet");
                        String temps = jsonobject.optString("time");
                        addTerm(infos, temps);
                    }else if(jsonobject.optString("nb").equals("0")){
                        break;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {

        });
        requestQueue.add(jsonobjectRequest);
    }

    private void addTerm(String infos, String temps) {
        numEnCours++;
        numEnleve++;
        RelativeLayout constraintLayout = new RelativeLayout(this);
        RelativeLayout.LayoutParams cl = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        constraintLayout.setLayoutParams(cl);
        constraintLayout.setForegroundGravity(Gravity.CENTER);
        TextView tv = new TextView(this);
        LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
        linearParams.setMargins(0, 40, 40, 0);
        tv.setLayoutParams(linearParams);
        tv.requestLayout();
        tv.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
        tv.setTextColor(getColor(R.color.white));
        tv.setTextSize(15);
        tv.setText("  terminer : \n "+temps);
        tv.setTypeface(Typeface.DEFAULT_BOLD);
        TextView tv2 = new TextView(this);
        RelativeLayout.LayoutParams btn = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,150);
        btn.setMargins(10,50,10,0);
        tv2.setLayoutParams(btn);
        tv2.requestLayout();
        tv2.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        tv2.setTextColor(getColor(R.color.white));
        tv2.setTextSize(18);
        tv2.setText(numEnCours + " " + infos);
        tv2.setTypeface(Typeface.DEFAULT_BOLD);
        constraintLayout.addView(tv);
        constraintLayout.addView(tv2);
        LinearLayout ln = findViewById(R.id.List);
        ln.addView(constraintLayout);
    }

    public void Notify(View view) {
        if(getIntent().getStringExtra("onNotifications").equals("true")) {
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            Intent intent = new Intent(Success.this, Success.class);
            Intent intent2 = getIntent();
            intent.putExtra("classe", intent2.getStringExtra("classe"));
            intent.putExtra("nom", intent2.getStringExtra("nom"));
            intent.putExtra("prenom", intent2.getStringExtra("prenom"));
            intent.putExtra("uri", intent2.getStringExtra("uri"));
            intent.putExtra("onNotifications", intent2.getStringExtra("onNotifications"));
            PendingIntent pendingIntent = PendingIntent.getActivity(Success.this, 0, intent, 0);
            if (Build.VERSION.SDK_INT >= 26) {
                //When sdk version is larger than26
                String id = "channel_1";
                String description = "143";
                int importance = NotificationManager.IMPORTANCE_LOW;
                NotificationChannel channel = new NotificationChannel(id, description, importance);
//                     channel.enableLights(true);
//                     channel.enableVibration(true);//
                manager.createNotificationChannel(channel);
                Notification notification = new Notification.Builder(Success.this, id)
                        .setCategory(Notification.CATEGORY_MESSAGE)
                        .setSmallIcon(R.drawable.ic_round_warning_amber_24)
                        .setContentTitle("Important")
                        .setContentText("il ne vous reste plus que X jours à l'activite X")
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true)
                        .build();
                manager.notify(1, notification);
            } else {
                //When sdk version is less than26
                Notification notification = new NotificationCompat.Builder(Success.this)
                        .setContentTitle("Important")
                        .setContentText("il ne vous reste plus que X jours à l'activite X")
                        .setContentIntent(pendingIntent)
                        .setSmallIcon(R.drawable.ic_round_warning_amber_24)
                        .build();
                manager.notify(1, notification);
            }
        }
    }
}
