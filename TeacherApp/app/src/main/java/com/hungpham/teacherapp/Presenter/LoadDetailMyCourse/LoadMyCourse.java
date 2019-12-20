package com.hungpham.teacherapp.Presenter.LoadDetailMyCourse;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hungpham.teacherapp.Model.Entities.Course;
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
        ArrayList<Doc>docList=new ArrayList<>();
        docRef.orderByChild("courseId").equalTo(courseId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                docList.clear();
                for (DataSnapshot childSnap:dataSnapshot.getChildren()) {
                    Doc doc = childSnap.getValue(Doc.class);
                    if(doc==null){
                        loadCourseListener.onNullItem("Không có bài test");
                    }
                    else {
                        if (doc.getType().compareTo("tutorTest") != 0) {
                            docList.add(doc);
                            loadCourseListener.onLoadDocMyCourse(docList);
                            loadTitle(courseId);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void loadTitle(String courseId){
        DatabaseReference courseRef=FirebaseDatabase.getInstance().getReference("Course");
        courseRef.child(courseId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Course course=dataSnapshot.getValue(Course.class);
                loadCourseListener.onLoadTitle(course.getCourseName());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void loadTutorTest(String courseId){
        DatabaseReference docRef=FirebaseDatabase.getInstance().getReference("Doc");
        ArrayList<Doc>docList=new ArrayList<>();
        ArrayList<String>docKey=new ArrayList<>();
        docRef.orderByChild("courseId").equalTo(courseId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                docList.clear();
                docKey.clear();
                for (DataSnapshot childSnap:dataSnapshot.getChildren()) {

                    Doc doc = childSnap.getValue(Doc.class);
                    if(doc.getType().equals("tutorTest")&&doc.getStatus()==1) {
                        docList.add(doc);
                        docKey.add(childSnap.getKey());
                        loadCourseListener.onLoadTutorTest(docList,docKey);
                    }
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
    public void addTest(String courseId,HashMap<String,Object>edtMap){
        DatabaseReference testRef=FirebaseDatabase.getInstance().getReference("Doc");
        String formPatter = "https://+forms\\.+gle+/+[a-zA-Z0-9._-]+";
        //https://docs.google.com/forms/u/0/
        String formName = "Test GV+/+[a-zA-Z0-9._-]+";
        HashMap<String,Object>dataMap=new HashMap<>();
        dataMap.put("courseId",courseId);
        dataMap.put("docName",edtMap.get("docName"));
        dataMap.put("docUrl",edtMap.get("docUrl"));
        dataMap.put("status",1);
        dataMap.put("type","tutorTest");
        if(edtMap.get("docName").equals("")||edtMap.get("docUrl").equals("")||!edtMap.get("docUrl").toString().trim().matches(formPatter)
                ||!edtMap.get("docName").toString().trim().matches(formName)) {
            loadCourseListener.addTestFailed("Vui lòng kiểm tra lại dữ liệu");
        }
        else {
            testRef.push().setValue(dataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        loadCourseListener.addTestSuccess("Thêm bài kiểm tra thành công");
                    } else {
                        loadCourseListener.addTestFailed("Thất bại");
                    }
                }
            });
        }
    }
    public void  setStatus(String status,String userPhone){
        HashMap<String,Object>map=new HashMap<>();
        map.put("status",status);
        DatabaseReference userRef=FirebaseDatabase.getInstance().getReference("Tutor");
        userRef.child(userPhone).updateChildren(map);
    }
}
