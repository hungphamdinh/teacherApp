package com.example.learnenglishta.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.learnenglishta.Model.Chat;
import com.example.learnenglishta.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ChatViewHolder> {
    private Context context;
    private ArrayList<Chat> chat;
    public static final int MSG_LEFT=0;
    public DatabaseReference chatRef;
    public FirebaseDatabase database;
    private String userId;
    public static final int MSG_RIGHT=1;
    //private String imgUrl;
    public MessageAdapter(Context context, ArrayList<Chat> chat,String userId) {
        this.context = context;
        this.chat = chat;
        //this.imgUrl=imgUrl;
        this.userId=userId;
    }
    public MessageAdapter(){

    }
    @NonNull
    @Override
    public MessageAdapter.ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(viewType == MSG_RIGHT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_right, parent, false);
            return new MessageAdapter.ChatViewHolder(view);
        }
        else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_left, parent, false);
            return new MessageAdapter.ChatViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {

        database = FirebaseDatabase.getInstance();
        chatRef = database.getReference("Chat");
        if(chat.get(position).getSender().equals(userId)) {
            return MSG_RIGHT;
        }
        else{
            return MSG_LEFT;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        Chat chatItem=chat.get(position);
        holder.showMessage.setText(chatItem.getMessage());

    }

    @Override
    public int getItemCount() {
        return chat.size();
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder {
        public TextView showMessage;
        public ImageView profileImage;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            showMessage=(TextView)itemView.findViewById(R.id.showMessage);
            profileImage=(ImageView)itemView.findViewById(R.id.profileImage);
        }
    }

}
