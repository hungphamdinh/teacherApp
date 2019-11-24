package com.example.learnenglishta.Fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.learnenglishta.Interface.ItemClickListener;
import com.example.learnenglishta.Model.Course;
import com.example.learnenglishta.Model.Request;
import com.example.learnenglishta.Model.User;
import com.example.learnenglishta.R;
import com.example.learnenglishta.StudentDetailActivity;
import com.example.learnenglishta.ViewHolder.StaffViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by User on 2/28/2017.
 */

public class MyCourseFragment extends Fragment {
    private Context context;
    private String userPhone;
    public MyCourseFragment(){}
    public MyCourseFragment(Context context, String userPhone){
        this.context=context;
        this.userPhone=userPhone;
    }
    private static final String TAG = "MyCourseFragment";
    private RecyclerView recyclerMenu;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference courseRef;
    private FirebaseDatabase database;
    private FirebaseRecyclerAdapter<Course, StaffViewHolder> adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tutor,container,false);
        database= FirebaseDatabase.getInstance();
        courseRef =database.getReference("Course");
        recyclerMenu=(RecyclerView)view.findViewById(R.id.listOrderRecycler);
        recyclerMenu.setHasFixedSize(true);
        layoutManager= new LinearLayoutManager(getContext());
        recyclerMenu.setLayoutManager(layoutManager);
        loadListTutor();
        return view;
    }
    private void loadListTutor() {
        adapter=new FirebaseRecyclerAdapter<Course, StaffViewHolder>
                (Course.class,R.layout.my_course_layout,
                        StaffViewHolder.class,
                        courseRef.orderByChild("tutorPhone").equalTo(userPhone)) {
            @Override
            protected void populateViewHolder(StaffViewHolder viewHolder, final Course model, int position) {
                viewHolder.txtCourseName.setText(model.getCourseName());
                viewHolder.txtDescript.setText(model.getDescript());
                viewHolder.txtSchedule.setText(model.getSchedule());
                checkRequest(adapter.getRef(position).getKey(),viewHolder);

            }

        };
        adapter.notifyDataSetChanged();
        recyclerMenu.setAdapter(adapter);
    }
    private void checkRequest(String courseID,StaffViewHolder viewHolder){
        DatabaseReference requestRef=FirebaseDatabase.getInstance().getReference("Requests");
        requestRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot childRequest:dataSnapshot.getChildren()){
                    Request request=childRequest.getValue(Request.class);
                    if(request.getCourseId().equals(courseID)){
                        loadStudent(request.getPhone(),viewHolder);
                        onClickItem(request, viewHolder);
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void onClickItem(Request request, StaffViewHolder viewHolder) {
        viewHolder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                Intent intent=new Intent(context, StudentDetailActivity.class);
                String studentID=request.getPhone();
                String userID=userPhone;
                ArrayList<String> listIntent=new ArrayList<>();
                listIntent.add(studentID);
                listIntent.add(userID);
                intent.putStringArrayListExtra("ChatID",listIntent);
                //intent.putExtra("tutorID",adapter.getRef(position).getKey());
                startActivity(intent);                    }
        });
    }

    private void loadStudent(String studentPhone, StaffViewHolder viewHolder){
        DatabaseReference studentRef=FirebaseDatabase.getInstance().getReference("User");
        studentRef.child(studentPhone).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User student=dataSnapshot.getValue(User.class);
                viewHolder.txtName.setText(student.getUsername());
                viewHolder.txtEmail.setText(student.getEmail());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
