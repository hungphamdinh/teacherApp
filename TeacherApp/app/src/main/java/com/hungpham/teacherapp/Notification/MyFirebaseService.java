package com.hungpham.teacherapp.Notification;

import com.hungpham.teacherapp.Model.Entities.Tutor;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        DatabaseReference userRef=FirebaseDatabase.getInstance().getReference("Tutor");
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnap:dataSnapshot.getChildren()){
                    Tutor user=childSnap.getValue(Tutor.class);
                    String refreshToken= FirebaseInstanceId.getInstance().getToken();
                    if(userRef!=null){
                        updateToken(refreshToken,childSnap.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    private void updateToken(String refreshToken,String userKey){
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Tokens");
        Token token=new Token(refreshToken);
        ref.child(userKey).setValue(token);
    }
}
