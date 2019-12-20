package com.hungpham.teacherapp.Presenter.MyCourseList;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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
    public void loadCourse(HashMap<String,Object>posMap,ArrayList<Course>courses,ArrayList<String>keys){
        DatabaseReference courseRef=FirebaseDatabase.getInstance().getReference("Course");
        int pos= (int) posMap.get("pos");
        courseRef.orderByChild("tutorPhone").equalTo(posMap.get("userId").toString()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot courseSnapshot:dataSnapshot.getChildren()){
                    Course course=courseSnapshot.getValue(Course.class);

                    if (courses.get(pos).getStatus()==1) {
                            HashMap<String, Object> courseMap = new HashMap<>();
                            courseMap.put("courseName", course.getCourseName());
                            courseMap.put("courseSchedule", course.getSchedule());
                            courseMap.put("courseImage", course.getImage());
                            //String key = courseSnapshot.getKey();
                            String userId = posMap.get("userId").toString();
                            myCourseListListener.onLoadCourseMyCourse(courseMap,pos);
                            checkRequest(userId,keys.get(pos));
                    }
                    else {
                        myCourseListListener.onNullItem("Khóa học rỗng");
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void checkRequest(String userId,String keyItem){
        DatabaseReference requestRef=FirebaseDatabase.getInstance().getReference("Requests");
        requestRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot childRequest:dataSnapshot.getChildren()){
                    Request request=childRequest.getValue(Request.class);
                    if(request.getCourseId().equals(keyItem)){
                        loadStudent(request.getPhone());
                        onClickItem(request,userId,keyItem);
                    }


                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void onClickItem(Request request,String userId,String coursId) {
        String tutorID=request.getPhone();
        String userID=userId;
        ArrayList<String> listIntent=new ArrayList<>();
        listIntent.add(tutorID);
        listIntent.add(userID);
        listIntent.add(coursId);
        myCourseListListener.onLoadDataToClick(listIntent);
    }

    public void loadStudent(String tutorPhone){

        DatabaseReference studentRef= FirebaseDatabase.getInstance().getReference("User");
        studentRef.child(tutorPhone).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String,Object>studentMap=new HashMap<>();
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
