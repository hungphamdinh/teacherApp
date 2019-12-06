package com.hungpham.teacherapp.Model.MyCourseList;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hungpham.teacherapp.Adapter.StaffAdapter;
import com.hungpham.teacherapp.Model.Entities.Course;
import com.hungpham.teacherapp.Model.Entities.Request;

import java.util.ArrayList;

public class MyCourseListCallAdapter {
    private IMyCourseListAdaperListener myCourseListAdaperListener;
    public MyCourseListCallAdapter(IMyCourseListAdaperListener myCourseListAdaperListener){
        this.myCourseListAdaperListener=myCourseListAdaperListener;
    }
    public void loadTutor(String userPhone){
        DatabaseReference request= FirebaseDatabase.getInstance().getReference("Course");
        request.orderByChild("tutorPhone").equalTo(userPhone).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Course>courseList=new ArrayList<>();
                for(DataSnapshot childSnap:dataSnapshot.getChildren()){
                    Course courseCk=childSnap.getValue(Course.class);
                    if(courseCk.getIsBuy().equals("true")){
                        courseList.add(courseCk);
                        myCourseListAdaperListener.callAdapter(courseList);
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
