package com.example.maclassesdufutur;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.maclassesdufutur.Adapter.MessageAdapter;
import com.example.maclassesdufutur.Adapter.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MessageActivity extends AppCompatActivity {

    de.hdodenhof.circleimageview.CircleImageView imageView;
    TextView username;

    FirebaseUser fuser;
    DatabaseReference reference;

    Intent intent;

    MessageAdapter messageAdapter;
    List<Chat> mchats;

    RecyclerView recyclerView;

    ImageButton btn_send;
    EditText txt_send;

    ValueEventListener seenListener;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        btn_send = findViewById(R.id.btnSend);
        txt_send = findViewById(R.id.sendText);
        recyclerView = findViewById(R.id.Recycler_View);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);


        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbarMess);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent6 = getIntent();
                Intent intent7 = new Intent( MessageActivity.this, messagerie.class);
                intent7.putExtra("classe", intent6.getStringExtra("classe"));
                intent7.putExtra("nom", intent6.getStringExtra("nom"));
                intent7.putExtra("prenom", intent6.getStringExtra("prenom"));
                intent7.putExtra("uri", intent6.getStringExtra("uri"));
                intent7.putExtra("onNotifications", intent6.getStringExtra("onNotifications"));
                startActivity(intent7);

                finish();
            }
        });

        imageView = findViewById(R.id.profil_image);
        username = findViewById(R.id.UserName);
        intent = getIntent();
        String userId = intent.getStringExtra("userId");
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance("https://paulcornu-efa15-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Users").child(userId);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                username.setText(user.getUsername());
                imageView.setImageResource(R.drawable.pdp);
                readMessage(fuser.getUid(), userId, user.getImageUrl());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        seenMessage(userId);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = txt_send.getText().toString();
                if(!msg.equals(""))
                    sendMessage(fuser.getUid(), userId, msg);
                else
                    Toast.makeText(MessageActivity.this, "You can't send empty message", Toast.LENGTH_SHORT).show();
                txt_send.setText("");
            }
        });

    }

    private void setSupportActionBar(Toolbar toolbar) {
    }

    private void seenMessage(String userid){
        reference = FirebaseDatabase.getInstance("https://paulcornu-efa15-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Chats");
        seenListener = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Chat chat = dataSnapshot.getValue(Chat.class);
                    if(chat.getReceiver().equals(fuser.getUid()) && chat.getSender().equals(userid)){
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("isseen", true);
                        dataSnapshot.getRef().updateChildren(hashMap);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void sendMessage(String sender, String receiver, String message){
        DatabaseReference reference = FirebaseDatabase.getInstance("https://paulcornu-efa15-default-rtdb.europe-west1.firebasedatabase.app/").getReference();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);
        hashMap.put("isseen", false);
        hashMap.put("createdAt",new Date().getTime());
        System.out.println(new Date().getTime());
        reference.child("Chats").push().setValue(hashMap);
    }

    private void readMessage(String myid, String userid, String imageUrl){
        mchats = new ArrayList<>();

        reference = FirebaseDatabase.getInstance("https://paulcornu-efa15-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Chats");
        Query q = reference.orderByChild("createdAt");
        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mchats.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Chat chat = dataSnapshot.getValue(Chat.class);
                    assert chat != null;
                    if(chat.getReceiver().equals(myid) && chat.getSender().equals(userid) || chat.getReceiver().equals(userid) && chat.getSender().equals(myid)){
                        mchats.add(chat);
                    }

                    messageAdapter = new MessageAdapter(MessageActivity.this, mchats, imageUrl);
                    recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}