package com.hungpham.teacherapp.Notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;

import androidx.core.app.NotificationCompat;

import com.hungpham.teacherapp.View.Chat.MainChatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.ArrayList;

public class MyFirebaseMessaging extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        String sented=remoteMessage.getData().get("sented");
        DatabaseReference userRef= FirebaseDatabase.getInstance().getReference("Tutor");
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnap:dataSnapshot.getChildren()){
                    String userKey=childSnap.getKey();
                    if (userRef!=null&&sented.equals(userKey)){
                            sendNotification(remoteMessage,userKey);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void sendNotification(RemoteMessage remoteMessage,String keyUser) {
        String user=remoteMessage.getData().get("user");
        String icon=remoteMessage.getData().get("icon");
        String title=remoteMessage.getData().get("title");
        String body=remoteMessage.getData().get("body");
        RemoteMessage.Notification notification=remoteMessage.getNotification();
        int j=Integer.parseInt(user.replaceAll("[\\D]",""));
        DatabaseReference tutorRef=FirebaseDatabase.getInstance().getReference("Tutor");
        tutorRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child:dataSnapshot.getChildren()) {
                    if (child.getKey().equals(keyUser)) {

                        //int NOTIFICATION_ID = 234;
                        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                        String CHANNEL_ID = "my_channel_01";
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            CharSequence name = "my_channel";
                            String Description = "This is my channel";
                            int importance = NotificationManager.IMPORTANCE_HIGH;
                            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
                            mChannel.setDescription(Description);
                            mChannel.enableLights(true);
                            mChannel.setLightColor(Color.RED);
                            mChannel.enableVibration(true);
                            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                            mChannel.setShowBadge(false);
                            notificationManager.createNotificationChannel(mChannel);
                        }
                        Intent intent = new Intent(MyFirebaseMessaging.this, MainChatActivity.class);
                        ArrayList<String>chatList=new ArrayList<>();
                        chatList.add(user);
                        chatList.add(keyUser);
                        intent.putStringArrayListExtra("ChatId",chatList);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        PendingIntent pendingIntent = PendingIntent.getActivity(MyFirebaseMessaging.this, j, intent, PendingIntent.FLAG_ONE_SHOT);
                        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(MyFirebaseMessaging.this,CHANNEL_ID)
                                .setSmallIcon(Integer.parseInt(icon))
                                .setContentTitle(title)
                                .setContentText(body)
                                .setAutoCancel(true)
                                .setSound(defaultSound)
                                .setContentIntent(pendingIntent);
                    //    NotificationManager noti = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        int i = 0;
                        if (j > 0) {
                            i = j;
                        }
                        notificationManager.notify(i, builder.build());
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
