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

public class UserFrag extends Fragment {

    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<User> mUser;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.user_frag, container, false);
        recyclerView = view.findViewById(R.id.RecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mUser = new ArrayList<>();
        readUsers();

        return view;
    }

    private void readUsers() {

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance("https://paulcornu-efa15-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Users");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mUser.clear();
                for (DataSnapshot snapshots : snapshot.getChildren()){
                    User user = snapshots.getValue(User.class);
                    assert firebaseUser != null;
                    assert user != null;
                    if(!user.getId().equals(firebaseUser.getUid())){
                        mUser.add(user);
                    }
                }
                userAdapter = new UserAdapter(getContext(), mUser);
                recyclerView.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}