package com.hungpham.teacherapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.hungpham.teacherapp.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyAccountActivity extends AppCompatActivity {
    private EditText edtUserNameAc,edtPhoneAc,edtEmailAc;
    private FirebaseDatabase databaseAc;
    private DatabaseReference userAc;
    private String phoneNumAC;
    private Button btnAddEmailAc;
    private User userAccount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
        edtUserNameAc=(EditText)findViewById(R.id.edtUsernameMyAc);
        edtPhoneAc=(EditText)findViewById(R.id.edtPhoneMyAc);
        edtEmailAc=(EditText)findViewById(R.id.edtEmailAc);
        btnAddEmailAc=(Button)findViewById(R.id.btnAddEmail);
        databaseAc= FirebaseDatabase.getInstance();
        userAc=databaseAc.getReference("User");
        btnAddEmailAc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailAc=edtEmailAc.getText().toString();
                userAc.orderByChild("email").equalTo(emailAc).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(final DataSnapshot dataSnapshot) {
                        for(DataSnapshot uniqueKeySnapshot : dataSnapshot.getChildren()){
                            if(dataSnapshot.exists()){
                                Toast.makeText(MyAccountActivity.this,"This email is exist",Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(MyAccountActivity.this,"Register ",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

    }

//    private void getDataFromAc(final String phoneNumAC) {
//        final DatabaseReference userArcChild=userAc.child(phoneNumAC);
//        final DatabaseReference userArcSecondChild=userArcChild.child("email");
//        userAc.child(phoneNumAC).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(final DataSnapshot dataSnapshot) {
//                userAccount=dataSnapshot.getValue(User.class);
//                //User userAccountEmail=dataSnapshot.getValue(User.class);
//                //edtEmailAc.setText(userAccountEmail.getEmail());
//                edtUserNameAc.setText(userAccount.getUsername());
//                edtPhoneAc.setText(phoneNumAC);
//                //edtEmailAc.setText(dataSnapshot.getValue(User.class).getEmail());
//                updateEmail(phoneNumAC);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//
//    }

    private void updateEmail() {


    }


}
