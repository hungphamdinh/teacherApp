package com.example.learnenglishta;


import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.learnenglishta.Adapter.MessageAdapter;
import com.example.learnenglishta.Common.Common;
import com.example.learnenglishta.Model.Chat;
import com.example.learnenglishta.Model.User;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.scaledrone.lib.Scaledrone;


import java.util.ArrayList;
import java.util.HashMap;

public class MainChatActivity extends AppCompatActivity {
    private EditText editText;
    private TextView txtName;
    private Scaledrone scaledrone;
    private MessageAdapter messageAdapter;
    private DatabaseReference reference;
    private RecyclerView messagesView;
    private FirebaseDatabase database;
    //private FirebaseUser fuser;
    private String tutorId;
    private String userId;
    private ImageButton btnSubmit;
    private ArrayList<Chat>chats;
    private ArrayList<String> listChatID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_chat);
        //fuser= FirebaseAuth.getInstance().getCurrentUser();
        editText = (EditText) findViewById(R.id.editText);
        txtName=(TextView)findViewById(R.id.txtNameChat);
        btnSubmit=(ImageButton) findViewById(R.id.btnSend);
        //  Log.d("UserId",fuser.getUid());
        messagesView = (RecyclerView) findViewById(R.id.messages_view);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(MainChatActivity.this);
        //linearLayoutManager.setStackFromEnd(true);
        messagesView.setHasFixedSize(true);
        messagesView.setLayoutManager(linearLayoutManager);
        if (getIntent() != null)
            listChatID = getIntent().getStringArrayListExtra("ChatID");
        if (!listChatID.isEmpty() && listChatID != null) {
            if (Common.isConnectedToInternet(this)) {
                tutorId=listChatID.get(0);
                userId=listChatID.get(1);
                accessToUser(userId,tutorId);
            } else {
                Toast.makeText(MainChatActivity.this, "Check your connection", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        onClickSend();
        //messageAdapter = new MessageAdapter(this);

//        messagesView.setAdapter(messageAdapter);

    }

    private void accessToUser(String senderId,String reciverId) {
        database=FirebaseDatabase.getInstance();
        reference= database.getReference("User");
        reference.child(reciverId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user=dataSnapshot.getValue(User.class);
                txtName.setText(user.getUsername());
//                if(user.getImage.equals("default")){
//
//                }
                readMessage(senderId,reciverId);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void onClickSend() {
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg=editText.getText().toString();
                if(!msg.equals("")) {
                    sendMessage(userId, tutorId, editText.getText().toString());
                }
                else {
                    Toast.makeText(MainChatActivity.this, "You can't send empty message", Toast.LENGTH_SHORT).show();
                }
                editText.setText("");
            }

        });
    }

    public void sendMessage(String sender, String reciever, String message){
        reference= FirebaseDatabase.getInstance().getReference();
        HashMap<Object,Object>hashMap=new HashMap<>();
        hashMap.put("sender",sender);
        hashMap.put("reciever",reciever);
        hashMap.put("message",message);
        reference.child("Chat").push().setValue(hashMap);

    }

    @Override
    protected void onStart() {
        super.onStart();

    }
    public void readMessage(final String myId, final String turId){
        chats=new ArrayList<>();
        DatabaseReference chatRef;
        chatRef=FirebaseDatabase.getInstance().getReference("Chat");
        chatRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                chats.clear();
                for(DataSnapshot childSnap:dataSnapshot.getChildren()){
                    Chat chatItem=childSnap.getValue(Chat.class);
//                    Log.d("reciever",chatItem.getReciever());
                    if(chatItem.getReciever().equals(myId)&&chatItem.getSender().equals(turId)
                            ||chatItem.getReciever().equals(turId)&&chatItem.getSender().equals(myId)){
                        chats.add(chatItem);

//                        Log.d("Size", String.valueOf(chats.size()));
                    }
                    messageAdapter=new MessageAdapter(MainChatActivity.this,chats,myId);
                    messageAdapter.notifyDataSetChanged();
                    messagesView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
