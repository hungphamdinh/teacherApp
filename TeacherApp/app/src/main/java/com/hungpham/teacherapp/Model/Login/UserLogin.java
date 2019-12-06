package com.hungpham.teacherapp.Model.Login;

import android.content.Context;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hungpham.teacherapp.Common.Common;
import com.hungpham.teacherapp.Model.Entities.Tutor;

public class UserLogin {
    private Context context;
    public IUserLoginListener userLoginListener;
    public UserLogin(IUserLoginListener userLoginListener,Context context){
        this.userLoginListener=userLoginListener;
        this.context=context;
    }
    public void isValidData(String phone,String password) {
        if (Common.isConnectedToInternet(context)) {
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Tutor");
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (phone.equals("") || password.equals("")) {
                        userLoginListener.onLoginError("Please check your username and password");
                    } else {
                        if (dataSnapshot.child(phone).exists()) {
                            Tutor uUser = dataSnapshot.child(phone).getValue(Tutor.class);
                            //uUser.set(phone);
                            //uUser.setUsername(uUser.getUsername());
                            if (password.equals(uUser.getPassword())) {
                                Common.currentUser = uUser;
                                userLoginListener.onLoginSucess("Success");
                            } else {
                                userLoginListener.onLoginError("WrongPass");
                            }

                        } else {
                            userLoginListener.onLoginError("PhoneNotExist");
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        else {
            userLoginListener.onLoginError("Please check your connection");
        }
    }


}
