package com.hungpham.teacherapp.Presenter.Chat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hungpham.teacherapp.Model.Entities.Chat;
import com.hungpham.teacherapp.Model.Entities.Tutor;
import com.hungpham.teacherapp.Model.Entities.User;
import com.hungpham.teacherapp.Notification.Client;
import com.hungpham.teacherapp.Notification.Data;
import com.hungpham.teacherapp.Notification.MyRespone;
import com.hungpham.teacherapp.Notification.Sender;
import com.hungpham.teacherapp.Notification.Token;
import com.hungpham.teacherapp.R;
import com.hungpham.teacherapp.Service.APIService;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserChat {
    private ChatListener chatListener;
    private boolean notify=true;
    public UserChat(ChatListener chatListener){
        this.chatListener=chatListener;
    }
    public void onClickSend(HashMap<String,Object> msgMap) {
                notify=true;
                String msg=msgMap.get("msg").toString();
                String userId=msgMap.get("userId").toString();
                String tutorId=msgMap.get("studentId").toString();
                if(!msg.equals("")) {
                    sendMessage(msgMap);
                    chatListener.onClickSendMsg(msgMap);
                }
                else {
                    chatListener.onError("You can't send empty message");
                }
     //           editText.setText("");
    }

    private void sendMessage(HashMap<String,Object>msgMap){
        String message=msgMap.get("msg").toString();
        String sender=msgMap.get("userId").toString();
        String reciever=msgMap.get("studentId").toString();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference();
        HashMap<Object,Object> hashMap=new HashMap<>();
        hashMap.put("sender",sender);
        hashMap.put("reciever",reciever);
        hashMap.put("message",message);
        hashMap.put("seen",false);
        reference.child("Chat").push().setValue(hashMap);
        final  String msg=message;
        reference=FirebaseDatabase.getInstance().getReference("Tutor").child(sender);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Tutor user=dataSnapshot.getValue(Tutor.class);
                HashMap<String,Object> listChat=new HashMap<>();
                listChat.put("reciever",reciever);
                listChat.put("sender",sender);
                listChat.put("userName",user.getUsername());
                listChat.put("msg",msg);
                if(notify){
                    sendNotification(listChat);
                    chatListener.sendMsg(msgMap);
                }
                notify=false;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void sendNotification(HashMap<String,Object>listChat) {
        APIService apiService= Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        String reciever=listChat.get("reciever").toString();
        String sender=listChat.get("sender").toString();
        String userName=listChat.get("userName").toString();
        String msg=listChat.get("msg").toString();
        DatabaseReference tokenRef=FirebaseDatabase.getInstance().getReference("Tokens");
        tokenRef.orderByKey().equalTo(reciever).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnap:dataSnapshot.getChildren()){
                    Token token=childSnap.getValue(Token.class);
                    Data data=new Data(sender, R.mipmap.ic_launcher,userName+": "+msg,"Tin nhắn mới",
                            reciever);
                    Sender send=new Sender(data,token.getToken());
                    apiService.sendNotification(send)
                            .enqueue(new Callback<MyRespone>() {
                                @Override
                                public void onResponse(Call<MyRespone> call, Response<MyRespone> response) {
                                    if(response.code()==200){
                                        if(response.body().success!=1){
                                            chatListener.onError("Failed");
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<MyRespone> call, Throwable t) {

                                }
                            });


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void accessToUser(HashMap<String,Object>idMap) {
        String senderId=idMap.get("senderId").toString();
        String reciverId=idMap.get("receiverId").toString();
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("User");
        reference.child(reciverId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user=dataSnapshot.getValue(User.class);
                chatListener.onAccesstoUser(user.getUsername());
                readMessage(senderId,reciverId);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void readMessage(final String myId, final String turId){
        ArrayList<Chat>chats=new ArrayList<>();
        ArrayList<String>keys=new ArrayList<>();
        DatabaseReference chatRef;
        chatRef=FirebaseDatabase.getInstance().getReference("Chat");
        chatRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                chats.clear();
                keys.clear();
                for(DataSnapshot childSnap:dataSnapshot.getChildren()){
                    Chat chatItem=childSnap.getValue(Chat.class);
                    if(chatItem.getReciever().equals(myId)&&chatItem.getSender().equals(turId)
                            ||chatItem.getReciever().equals(turId)&&chatItem.getSender().equals(myId)){
                        chats.add(chatItem);
                        keys.add(childSnap.getKey());
                        chatListener.readMsg(chats,keys);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void  setStatus(String status,String userPhone){
        HashMap<String,Object>map=new HashMap<>();
        map.put("status",status);
        DatabaseReference userRef=FirebaseDatabase.getInstance().getReference("Tutor");
        userRef.child(userPhone).updateChildren(map);
    }
}
