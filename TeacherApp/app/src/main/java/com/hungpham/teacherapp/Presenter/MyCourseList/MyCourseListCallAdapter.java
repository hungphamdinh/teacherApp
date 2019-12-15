package com.hungpham.teacherapp.Presenter.MyCourseList;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hungpham.teacherapp.Model.Entities.Course;

import java.util.ArrayList;

public class MyCourseListCallAdapter {
    private IMyCourseListAdaperListener myCourseListAdaperListener;
    public MyCourseListCallAdapter(IMyCourseListAdaperListener myCourseListAdaperListener){
        this.myCourseListAdaperListener=myCourseListAdaperListener;
    }
    public void loadTutor(String userPhone){
        DatabaseReference courseRef= FirebaseDatabase.getInstance().getReference("Course");
        courseRef.orderByChild("tutorPhone").equalTo(userPhone).addValueEventListener(new ValueEventListener() {
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
