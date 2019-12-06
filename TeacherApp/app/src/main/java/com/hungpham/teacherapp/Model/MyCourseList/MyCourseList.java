package com.hungpham.teacherapp.Model.MyCourseList;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hungpham.teacherapp.Adapter.StaffAdapter;
import com.hungpham.teacherapp.Model.Entities.Course;
import com.hungpham.teacherapp.Model.Entities.Request;
import com.hungpham.teacherapp.Model.Entities.User;

import java.util.ArrayList;
import java.util.HashMap;

public class MyCourseList {
    IMyCourseListListener myCourseListListener;
    public MyCourseList(IMyCourseListListener myCourseListListener){
        this.myCourseListListener=myCourseListListener;
    }
    public void loadCourse(HashMap<String,Object>tutorMap,HashMap<String,Object>posMap){
        DatabaseReference courseRef=FirebaseDatabase.getInstance().getReference("Course");
        courseRef.orderByChild("tutorPhone").equalTo(posMap.get("userId").toString()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot courseSnapshot:dataSnapshot.getChildren()){
                    Course course=courseSnapshot.getValue(Course.class);
                    HashMap<String,Object>courseMap=new HashMap<>();
                    courseMap.put("courseName",course.getCourseName());
                    courseMap.put("courseSchedule",course.getSchedule());
                    courseMap.put("courseImage",course.getImage());
                    String key=courseSnapshot.getKey();
                    String userId=posMap.get("userId").toString();
                    myCourseListListener.onLoadCourseMyCourse(courseMap);
                    checkRequest(key,tutorMap,userId);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void checkRequest(String courseID,HashMap<String,Object>tutorMap,String userId){
        DatabaseReference requestRef=FirebaseDatabase.getInstance().getReference("Requests");
        requestRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot childRequest:dataSnapshot.getChildren()){
                    Request request=childRequest.getValue(Request.class);
                    if(request.getCourseId().equals(courseID)){
                        loadStudent(request.getPhone(),tutorMap);
                        onClickItem(request,userId);
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void onClickItem(Request request,String userId) {
        String tutorID=request.getPhone();
        String userID=userId;
        ArrayList<String> listIntent=new ArrayList<>();
        listIntent.add(tutorID);
        listIntent.add(userID);
        listIntent.add(request.getCourseId());
        myCourseListListener.onLoadDataToClick(listIntent);
    }

    public void loadStudent(String tutorPhone, HashMap<String,Object>studentMap){
        DatabaseReference studentRef= FirebaseDatabase.getInstance().getReference("User");
        studentRef.child(tutorPhone).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User student=dataSnapshot.getValue(User.class);
                if(student.getStatus().equals("offline")){
                    myCourseListListener.offlineStatus("Học viên hiện không hoạt dộng");
                }
                else{
                    myCourseListListener.onlineStatus("Học viên hiện đang hoạt động");
                }
                studentMap.put("studentName",student.getUsername());
                studentMap.put("studentMail",student.getEmail());
                studentMap.put("studentImage",student.getAvatar());
                myCourseListListener.onLoadStudentMyCourse(studentMap);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
