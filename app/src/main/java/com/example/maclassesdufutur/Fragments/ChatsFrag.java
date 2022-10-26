package com.example.maclassesdufutur.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.maclassesdufutur.Adapter.User;
import com.example.maclassesdufutur.Adapter.UserAdapter;
import com.example.maclassesdufutur.Chat;
import com.example.maclassesdufutur.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatsFrag extends Fragment {

    private RecyclerView recyclerView;

    private UserAdapter userAdapter;
    private List<User> mUsers;

    FirebaseUser fuser;
    DatabaseReference reference;

    private List<String> userList;




    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chats_frag, container, false);

        recyclerView = view.findViewById(R.id.RecyclerViewChat);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        fuser = FirebaseAuth.getInstance().getCurrentUser();
        userList = new ArrayList<>();

        reference = FirebaseDatabase.getInstance("https://paulcornu-efa15-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();

                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Chat chat = dataSnapshot.getValue(Chat.class);
                    assert chat != null;
                    if(chat.getSender().equals(fuser.getUid()))
                        userList.add(chat.getReceiver());
                    if(chat.getReceiver().equals(fuser.getUid()))
                        userList.add(chat.getSender());
                }
                readsChat();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }

    private void readsChat() {

        mUsers = new ArrayList<>();
        reference = FirebaseDatabase.getInstance("https://paulcornu-efa15-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mUsers.clear();

                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    User user = dataSnapshot.getValue(User.class);

                    for(String id : userList) {
                        if (user.getId().equals(id)) {
                            if (mUsers.size() != 0) {
                                for (User user1 : mUsers) {
                                    if (!user.getId().equals(user1.getId())) {
                                        mUsers.add(user);
                                    }
                                }
                            } else {
                                mUsers.add(user);
                            }
                        }
                    }
                }

                userAdapter = new UserAdapter(getContext(), mUsers);
                recyclerView.setAdapter(userAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}