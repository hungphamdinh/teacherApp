package com.hungpham.teacherapp.Fragment;


import android.content.Context;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hungpham.teacherapp.Adapter.StaffAdapter;
import com.hungpham.teacherapp.Model.Course;
import com.hungpham.teacherapp.R;
import com.hungpham.teacherapp.ViewHolder.StaffViewHolder;
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
    private StaffAdapter staffAdapter;
    private ArrayList<Course> courseList;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_course,container,false);
        database= FirebaseDatabase.getInstance();
        courseRef =database.getReference("Course");
        recyclerMenu=(RecyclerView)view.findViewById(R.id.listOrderRecycler);
        recyclerMenu.setHasFixedSize(true);
        layoutManager= new LinearLayoutManager(getContext());
        recyclerMenu.setLayoutManager(layoutManager);
        //loadListTutor();
        loadTutor();
        return view;
    }

    private void loadTutor(){
        courseRef.orderByChild("tutorPhone").equalTo(userPhone).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                courseList=new ArrayList<>();
                for(DataSnapshot childSnap:dataSnapshot.getChildren()){
                    Course courseCk=childSnap.getValue(Course.class);
                    if(courseCk.getIsBuy().equals("true")){
                        courseList.add(courseCk);
                        staffAdapter=new StaffAdapter(context,courseList,userPhone);
                        staffAdapter.notifyDataSetChanged();
                        recyclerMenu.setAdapter(staffAdapter);
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
