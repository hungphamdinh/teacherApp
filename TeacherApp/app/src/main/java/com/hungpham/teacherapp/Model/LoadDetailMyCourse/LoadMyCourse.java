package com.hungpham.teacherapp.Model.LoadDetailMyCourse;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hungpham.teacherapp.Model.Entities.Doc;
import com.hungpham.teacherapp.Model.Entities.User;
import com.hungpham.teacherapp.Notification.Token;

import java.util.ArrayList;
import java.util.HashMap;

public class LoadMyCourse {
    private ILoadMyCourseListener loadCourseListener;
    public LoadMyCourse(ILoadMyCourseListener loadCourseListener){
        this.loadCourseListener=loadCourseListener;
    }
    public void getDetailStudent(String studentId, HashMap<String,Object>studentMap) {
        DatabaseReference studentRef= FirebaseDatabase.getInstance().getReference("User");
        studentRef.child(studentId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user=dataSnapshot.getValue(User.class);
                if(user.getStatus().equals("offline")){
                    loadCourseListener.offlineStatus("Học viên hiện không hoạt động");
                }
                else {
                    loadCourseListener.onlineStatus("Học viên hiện đang hoạt động");

                }
                studentMap.put("title",user.getUsername());
                studentMap.put("studentName",user.getUsername());
                studentMap.put("studentMail",user.getEmail());
                studentMap.put("studentImage",user.getAvatar());
                loadCourseListener.onLoadTutorMyCourse(studentMap);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void loadCourseDoc(String courseId){
        DatabaseReference docRef=FirebaseDatabase.getInstance().getReference("Doc");
        docRef.orderByChild("courseId").equalTo(courseId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Doc>docList=new ArrayList<>();
                for (DataSnapshot childSnap:dataSnapshot.getChildren()) {
                    Doc doc = childSnap.getValue(Doc.class);
                    docList.add(doc);
                    loadCourseListener.onLoadDocMyCourse(docList);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void updateToken(String userId,String token){
        DatabaseReference tokenRef=FirebaseDatabase.getInstance().getReference("Tokens");
        Token newToken=new Token(token);
        tokenRef.child(userId).setValue(newToken);
        loadCourseListener.updateToken("Token was updated");
    }
}
