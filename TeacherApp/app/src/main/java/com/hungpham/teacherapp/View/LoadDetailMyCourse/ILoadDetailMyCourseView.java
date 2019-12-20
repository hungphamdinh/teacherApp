package com.hungpham.teacherapp.View.LoadDetailMyCourse;


import com.hungpham.teacherapp.Model.Entities.Doc;

import java.util.ArrayList;
import java.util.HashMap;

public interface ILoadDetailMyCourseView {
    void onDisplayStudent(HashMap<String, Object> map);
    void onDisplayDoc(ArrayList<Doc> docList);
    void onDisplayTutorTest(ArrayList<Doc>docArrayList,ArrayList<String>docKey);
    void onNullItem(String msg);
    void onDisplayOnline(String msg);
    void onDisplayOffline(String msg);
    void onUpdateToken(String msg);
    void onAddTestSuccess(String msg);
    void onAddTestFailed(String msg);
    void onLoadTitle(String title);
}
