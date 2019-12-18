package com.hungpham.teacherapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.hungpham.teacherapp.Interface.ItemClickListener;
import com.hungpham.teacherapp.Model.Entities.Chat;
import com.hungpham.teacherapp.Model.Entities.User;
import com.hungpham.teacherapp.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;


public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ChatViewHolder> {
    private Context context;
    private ArrayList<Chat> chat;
    public static final int MSG_LEFT=0;
    public DatabaseReference chatRef;
    public FirebaseDatabase database;
    private HashMap<String,Object> id;
    public static final int MSG_RIGHT=1;
    public ArrayList<String>keys;
    public MessageAdapter(Context context, ArrayList<Chat> chat, HashMap<String,Object>id,ArrayList<String>keys) {
        this.context = context;
        this.chat = chat;
        this.id=id;
        this.keys=keys;
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
        if(chat.get(position).getSender().equals(id.get("userId").toString())) {
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
        if(chatItem.getReciever().equals(id.get("userId"))&&chatItem.getSender().equals(id.get("studentId"))){
            HashMap<String, Object> map = new HashMap<>();
            map.put("seen", true);
            chatRef.child(keys.get(position)).updateChildren(map);

        }
        if(chatItem.isSeen()==true) {
            holder.seen.setVisibility(View.VISIBLE);
        }
        else {
            holder.seen.setVisibility(View.GONE);
        }

        loadData(holder);
        //onCheckSeen(holder);
    }

    private void loadData(@NonNull ChatViewHolder holder) {
        DatabaseReference receiverRef= FirebaseDatabase.getInstance().getReference("User");
        receiverRef.child(id.get("studentId").toString()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User tutor=dataSnapshot.getValue(User.class);
                if(tutor.getStatus().equals("offline")){
                    holder.status.setVisibility(View.INVISIBLE);
                }
                else {
                    holder.status.setVisibility(View.VISIBLE);
                }
                Glide.with(context)
                        .load(tutor.getAvatar())
                        .centerCrop()
                        // .placeholder(R.drawable.loading_spinner)
                        .into(holder.profileImage);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    @Override
    public int getItemCount() {
        return chat.size();
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView showMessage;
        public ImageView profileImage,status;
        public TextView seen;
        private ItemClickListener itemClickListener;
        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            showMessage=(TextView)itemView.findViewById(R.id.showMessage);
            profileImage=(ImageView)itemView.findViewById(R.id.profileImage);
            status=(ImageView)itemView.findViewById(R.id.imgStatusChat);
            seen=(TextView)itemView.findViewById(R.id.txtSeen);
            itemView.setOnClickListener(this);
        }
        public void setItemClickListener(ItemClickListener itemClickListener){
            this.itemClickListener=itemClickListener;
        }

        @Override
        public void onClick(View view) {
            itemClickListener.onClick(view,getAdapterPosition(),false);
        }
    }

}
