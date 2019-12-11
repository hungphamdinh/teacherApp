package com.hungpham.teacherapp.Sinch;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hungpham.teacherapp.Interface.ItemClickListener;
import com.hungpham.teacherapp.Model.Entities.User;
import com.hungpham.teacherapp.R;
import com.hungpham.teacherapp.ViewHolder.StaffViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sinch.android.rtc.calling.Call;

public class PlaceCallActivity extends BaseActivity {

    private RecyclerView recyclerMenu;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference tutor;
    private FirebaseDatabase database;
    private FirebaseRecyclerAdapter<User, StaffViewHolder> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        database= FirebaseDatabase.getInstance();
        tutor =database.getReference("Tutor");
        recyclerMenu=(RecyclerView)findViewById(R.id.listTutorCall);
        recyclerMenu.setHasFixedSize(true);
        layoutManager= new LinearLayoutManager(PlaceCallActivity.this);
        recyclerMenu.setLayoutManager(layoutManager);
        loadListTutor();
        Button stopButton = (Button) findViewById(R.id.stopButton);
        stopButton.setOnClickListener(buttonClickListener);
    }

    @Override
    protected void onServiceConnected() {
        TextView userName = (TextView) findViewById(R.id.loggedInName);
        userName.setText(getSinchServiceInterface().getUserName());
    }

    private void stopButtonClicked() {
        if (getSinchServiceInterface() != null) {
            getSinchServiceInterface().stopClient();
        }
        finish();
    }

    private void callButtonClicked(String userName) {
        //String userName = mCallName.getText().toString();
        if (userName.isEmpty()) {
            Toast.makeText(this, "Please enter a tutor to call", Toast.LENGTH_LONG).show();
            return;
        }

        Call call = getSinchServiceInterface().callUserVideo(userName);
        String callId = call.getCallId();

        Intent callScreen = new Intent(this, CallScreenActivity.class);
        callScreen.putExtra(SinchService.CALL_ID, callId);
        startActivity(callScreen);
    }

    private View.OnClickListener buttonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.stopButton:
                    stopButtonClicked();
                    break;

            }
        }
    };
    private void loadListTutor() {
        adapter=new FirebaseRecyclerAdapter<User, StaffViewHolder>
                (User.class,R.layout.my_course_layout,
                        StaffViewHolder.class,
                        tutor) {
            @Override
            protected void populateViewHolder(StaffViewHolder viewHolder, final User model, int position) {
                viewHolder.txtName.setText(model.getUsername());
                viewHolder.txtEmail.setText(model.getEmail());

                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        //Toast.makeText(PlaceCallActivity.this,"Test",Toast.LENGTH_LONG).show();
                        callButtonClicked(model.getUsername());
                    }
                });
            }

        };
        adapter.notifyDataSetChanged();
        recyclerMenu.setAdapter(adapter);
    }
}

