package com.example.maclassesdufutur;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Infos extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final int PICK_IMAGE = 100;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    RequestQueue requestQueue;
    String classe = "";
    String nom = "";
    String prenom = "";
    String uriPath = "";
    de.hdodenhof.circleimageview.CircleImageView imageView;


    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(Infos.this,new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE
            },1);

        }
        super.onCreate(savedInstanceState);



        setContentView(R.layout.infopage);
        imageView = findViewById(R.id.profilePic);
        requestQueue = Volley.newRequestQueue(this);

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

        TextView nm = findViewById(R.id.nameText);
        TextView prnom = findViewById(R.id.prenomText);
        TextView clsse = findViewById(R.id.classeText);

        if(!uriPath.equals("null") && !uriPath.equals("") && !uriPath.equals(" ")){
            Bitmap image = BitmapFactory.decodeFile((uriPath));
            imageView.setImageBitmap(image);
        }else{
            imageView.setImageDrawable(getDrawable(R.drawable.pdp));
        }
        nm.setText(nom);
        prnom.setText(prenom);
        clsse.setText(classe);
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==1){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED){

            }
            else{
                Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_LONG).show();
            }
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_Settings:
                Intent intent5 = getIntent();
                Intent intent4 = new Intent( Infos.this, Settings.class);
                intent4.putExtra("classe", intent5.getStringExtra("classe"));
                intent4.putExtra("nom", intent5.getStringExtra("nom"));
                intent4.putExtra("prenom", intent5.getStringExtra("prenom"));
                intent4.putExtra("uri", uriPath);
                intent4.putExtra("onNotifications", intent5.getStringExtra("onNotifications"));
                startActivity(intent4);

                finish();
                break;
            case R.id.infos:
                break;
            case R.id.nav_home:
                Intent intent = getIntent();
                Intent intent2 = new Intent( Infos.this, Success.class);
                intent2.putExtra("classe", intent.getStringExtra("classe"));
                intent2.putExtra("nom", intent.getStringExtra("nom"));
                intent2.putExtra("prenom", intent.getStringExtra("prenom"));
                intent2.putExtra("uri", uriPath);
                intent2.putExtra("onNotifications", intent.getStringExtra("onNotifications"));
                startActivity(intent2);

                finish();
                break;
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                Intent intent3 = new Intent( Infos.this, MainActivity.class);
                startActivity(intent3);

                finish();
                Toast.makeText( this,"Disconnect", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_messagerie:
                Intent intent8 = getIntent();
                Intent intent9 = new Intent( Infos .this, messagerie.class);
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

    public void Edit(View view) {
        //Go re-faire Instagram
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = this.getContentResolver().query(selectedImage, filePathColumn,null,null,null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String imgPath = cursor.getString(columnIndex);
            cursor.close();
            sendPthToBDD(imgPath);
            Bitmap image = BitmapFactory.decodeFile((imgPath));
            imageView.setImageBitmap(image);
        }
    }

    private void sendPthToBDD(String path) {
        String URL = "http://192.168.56.1/paulcornu/SaveUri.php";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.equals("success")) {
                        Toast.makeText(Infos.this, "Save Success", Toast.LENGTH_SHORT).show();
                        uriPath = path;
                    } else if (response.equals("failed")) {
                        Toast.makeText(Infos.this, "Save failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(Infos.this, error.toString().trim(), Toast.LENGTH_SHORT).show();
                }
            }){
                @NonNull
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> data = new HashMap<>();
                    data.put("nom", nom);
                    data.put("prenom", prenom);
                    data.put("Uri", path);
                    data.put("classe", classe);
                    return data;
                }
            };
            RequestQueue requestQueue1 = Volley.newRequestQueue(getApplicationContext());
            requestQueue1.add(stringRequest);
    }

}
