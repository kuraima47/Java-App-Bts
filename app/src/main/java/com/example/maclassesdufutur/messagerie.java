package com.example.maclassesdufutur;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.maclassesdufutur.Fragments.ChatsFrag;
import com.example.maclassesdufutur.Fragments.UserFrag;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.ref.Reference;
import java.util.ArrayList;

public class messagerie extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    TextView UserName;
    RunTime run;
    de.hdodenhof.circleimageview.CircleImageView imageView;
    DatabaseReference reference;
    FirebaseUser fuser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.messagerie);
        imageView = findViewById(R.id.profil_image);
        UserName = findViewById(R.id.UserName);
        run = new RunTime(getApplicationContext());
        drawerLayout = findViewById(R.id.drawer_messagerie);
        navigationView = findViewById(R.id.nav_view);
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar3);
        navigationView.bringToFront();
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);
        UserName.setText(getIntent().getStringExtra("prenom") + " " + getIntent().getStringExtra("nom"));
        if(!getIntent().getStringExtra("uri").equals("null") && !getIntent().getStringExtra("uri").equals("") && !getIntent().getStringExtra("uri").equals(" ")){
            Bitmap image = BitmapFactory.decodeFile((getIntent().getStringExtra("uri")));
            imageView.setImageBitmap(image);
        }else{
            imageView.setImageDrawable(getDrawable(R.drawable.pdp));
        }
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        final TabLayout tabLayout = findViewById(R.id.TabLayout);
        final ViewPager viewPager = findViewById(R.id.viewPager);


        reference = FirebaseDatabase.getInstance("https://paulcornu-efa15-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
                int unread = 0;
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Chat chat = dataSnapshot.getValue(Chat.class);

                    if(chat.getReceiver().equals(fuser.getUid()) && !chat.isIsseen()){
                        unread++;
                    }
                }
                if(unread == 0){
                    viewPagerAdapter.addFragment(new ChatsFrag(), "Chats");
                }else{
                    viewPagerAdapter.addFragment(new ChatsFrag(), "("+unread+")"+"Chats");
                }
                viewPagerAdapter.addFragment(new UserFrag(), "Users");
                viewPager.setAdapter(viewPagerAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        tabLayout.setupWithViewPager(viewPager);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId ()) {
            case R.id.nav_home:
                Intent intent6 = getIntent();
                Intent intent7 = new Intent( messagerie.this, Success.class);
                intent7.putExtra("classe", intent6.getStringExtra("classe"));
                intent7.putExtra("nom", intent6.getStringExtra("nom"));
                intent7.putExtra("prenom", intent6.getStringExtra("prenom"));
                intent7.putExtra("uri", intent6.getStringExtra("uri"));
                intent7.putExtra("onNotifications", intent6.getStringExtra("onNotifications"));
                startActivity(intent7);
                finish();
                break;
            case R.id.nav_Settings:
                Intent intent5 = getIntent();
                Intent intent4 = new Intent( messagerie.this, Settings.class);
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
                Intent intent2 = new Intent( messagerie.this, Infos.class);
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
                Intent intent3 = new Intent( messagerie.this, MainActivity.class);
                startActivity(intent3);

                finish();
                Toast.makeText( this,"Disconnect", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_messagerie:
                break;
        }
        drawerLayout.closeDrawer (GravityCompat.START);
        return true;
    }

    class ViewPagerAdapter extends FragmentPagerAdapter{

        private ArrayList<Fragment> fragments;
        private ArrayList<String> titles;

        public ViewPagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
            this.fragments = new ArrayList<>();
            this.titles = new ArrayList<>();
        }


        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        public void addFragment(Fragment fragment, String title){
            fragments.add(fragment);
            titles.add(title);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }
}
