package com.example.learnenglishta;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.learnenglishta.Common.Common;
import com.example.learnenglishta.Model.Doc;
import com.example.learnenglishta.Model.User;
import com.example.learnenglishta.Notification.Token;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.sinch.android.rtc.calling.Call;

import java.util.ArrayList;

public class StudentDetailActivity extends BaseActivity {
    private DatabaseReference userRef;
    private FirebaseDatabase database;
    private TextView txtUsername,txtTitle,txtCountry,txtEmail,txtExp,txtCourseDoc;
    private String studentId;
    private String userId;
    private String courseId;
    private Button btnChat;
    private Button btnCall;
    private ArrayList<String> listChatID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_detail_acitivity);
        database=FirebaseDatabase.getInstance();
        userRef =database.getReference("User");
        txtUsername=(TextView)findViewById(R.id.txtUserNameTutor);
        txtEmail=(TextView)findViewById(R.id.txtEmailTutor);
        txtTitle=(TextView)findViewById(R.id.txtTitleTutorDetail);
        txtCountry=(TextView)findViewById(R.id.txtCountryTutor);
        txtCourseDoc=(TextView)findViewById(R.id.txtCourseDocDetail);
        btnChat=(Button)findViewById(R.id.btnMessage);
        btnCall=(Button)findViewById(R.id.btnCallTutor);
        if (getIntent() != null)
            listChatID = getIntent().getStringArrayListExtra("ChatID");
        if (!listChatID.isEmpty() && listChatID != null) {
            if (Common.isConnectedToInternet(this)) {
                studentId =listChatID.get(0);
                userId=listChatID.get(1);
                courseId=listChatID.get(2);
                getDetailTutor(studentId);
                getCourseDoc(courseId);
                updateToken(FirebaseInstanceId.getInstance().getToken());
            } else {
                Toast.makeText(StudentDetailActivity.this, "Check your connection", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        onClickChat();
        onClickCall();
    }
    private void updateToken(String token){
        DatabaseReference tokenRef=FirebaseDatabase.getInstance().getReference("Tokens");
        Token newToken=new Token(token);
        tokenRef.child(userId).setValue(newToken);
    }
    private void getCourseDoc(String courseId){
        DatabaseReference docRef=FirebaseDatabase.getInstance().getReference("Doc");
        docRef.orderByChild("courseId").equalTo(courseId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnap:dataSnapshot.getChildren()) {
                    Doc doc = childSnap.getValue(Doc.class);
                    txtCourseDoc.setText(doc.getDocName());
                    openCourseDoc(doc);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void openCourseDoc(Doc doc) {
        txtCourseDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(doc.getDocUrl());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
    }

    private void onClickCall() {
        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callButtonClicked(studentId);
            }
        });
    }

    private void onClickChat() {
        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(StudentDetailActivity.this, MainChatActivity.class);
                intent.putStringArrayListExtra("ChatID",listChatID);
                startActivity(intent);
            }
        });
    }

    private void callButtonClicked(String userName) {
        //String userName = mCallName.getText().toString();
        if (userName.isEmpty()) {
            Toast.makeText(this, "Please enter a userRef to call", Toast.LENGTH_LONG).show();
            return;
        }

        Call call = getSinchServiceInterface().callUserVideo(userName);
        String callId = call.getCallId();

        Intent callScreen = new Intent(this, CallScreenActivity.class);
        callScreen.putExtra(SinchService.CALL_ID, callId);
        startActivity(callScreen);
    }
    private void getDetailTutor(String tutorId) {
        userRef.child(tutorId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User student=dataSnapshot.getValue(User.class);
//                Picasso.with(getBaseContext()).load(curentFood.getImage()).into(foodImage);
//                collapsingToolbarLayout.setTitle(curentFood.getName());
                txtTitle.setText(student.getUsername());
                txtUsername.setText(student.getUsername());
                txtEmail.setText(student.getEmail());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onServiceConnected() {
//        TextView userName = (TextView) findViewById(R.id.loggedInName);
//        userName.setText(getSinchServiceInterface().getUserName());
    }

    private void stopButtonClicked() {
        if (getSinchServiceInterface() != null) {
            getSinchServiceInterface().stopClient();
        }
        finish();
    }
}
