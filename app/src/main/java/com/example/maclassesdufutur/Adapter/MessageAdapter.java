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

import com.example.maclassesdufutur.Chat;
import com.example.maclassesdufutur.MessageActivity;
import com.example.maclassesdufutur.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;

    private Context context;
    private List<Chat> mChats;
    private String imgUrl;
    FirebaseUser fuser;



    public MessageAdapter(Context context, List<Chat> mChats, String imgUrl){
        this.context = context;
        this.mChats = mChats;
        this.imgUrl = imgUrl;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView showMessage;
        public ImageView profil_Image;
        private TextView txt_seen;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            showMessage = itemView.findViewById(R.id.viewMessage);
            profil_Image = itemView.findViewById(R.id.profilimage);
            txt_seen = itemView.findViewById(R.id.txt_seen);

        }
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(viewType == MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_itemright, parent, false);
            return new MessageAdapter.ViewHolder(view);
        }
        else{
            View view = LayoutInflater.from(context).inflate(R.layout.chat_itemleft, parent, false);
            return new MessageAdapter.ViewHolder(view);
        }
    }



    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {

        Chat chat = mChats.get(position);

        holder.showMessage.setText(chat.getMessage());

        holder.profil_Image.setImageResource(R.drawable.pdp);

        if(position == mChats.size()-1){
            if(chat.isIsseen()){
                holder.txt_seen.setText("Vu");
            }
            else{
                holder.txt_seen.setText("");
            }
        }else{
            holder.txt_seen.setVisibility(View.GONE);
        }


    }

    @Override
    public int getItemCount() {
        return mChats.size();
    }

    @Override
    public int getItemViewType(int position) {
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        if(mChats.get(position).getSender().equals(fuser.getUid())){
            return MSG_TYPE_RIGHT;
        }else{
            return MSG_TYPE_LEFT;
        }
    }
}
