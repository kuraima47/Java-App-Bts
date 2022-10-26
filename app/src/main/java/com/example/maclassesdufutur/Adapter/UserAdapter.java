package com.example.maclassesdufutur.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.maclassesdufutur.MessageActivity;
import com.example.maclassesdufutur.R;
import com.example.maclassesdufutur.Settings;
import com.example.maclassesdufutur.Success;
import com.example.maclassesdufutur.messagerie;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private Context context;
    private List<User> mUsers;

    public UserAdapter(Context context, List<User> mUsers){
        this.context = context;
        this.mUsers = mUsers;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView username;
        public ImageView profil_Image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.username);
            profil_Image = itemView.findViewById(R.id.userImage);

        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_item, parent, false);
        return new UserAdapter.ViewHolder(view);
    }



    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = mUsers.get(position);
        holder.username.setText(user.getUsername());
        holder.profil_Image.setImageResource(R.drawable.pdp);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = (Activity) context;
                Intent intent5 = activity.getIntent();
                Intent intent4 = new Intent( context, MessageActivity.class);
                intent4.putExtra("userId", user.getId());
                intent4.putExtra("classe", intent5.getStringExtra("classe"));
                intent4.putExtra("nom", intent5.getStringExtra("nom"));
                intent4.putExtra("prenom", intent5.getStringExtra("prenom"));
                intent4.putExtra("uri", intent5.getStringExtra("uri"));
                intent4.putExtra("onNotifications", intent5.getStringExtra("onNotifications"));
                activity.startActivity(intent4);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }
}
