package com.hungpham.teacherapp.Presenter.LoadDetailMyCourse;


import com.hungpham.teacherapp.Model.Entities.Doc;

import java.util.ArrayList;
import java.util.HashMap;

public interface ILoadMyCourseListener {
    void onLoadTutorMyCourse(HashMap<String, Object> studentMap);
    void onLoadDocMyCourse(ArrayList<Doc> docList);
    void onLoadTutorTest(ArrayList<Doc>docArrayList,ArrayList<String>key);
    void offlineStatus(String msg);
    void onlineStatus(String msg);
    void updateToken(String msg);
    void onNullItem(String msg);
    void addTestSuccess(String msg);
    void addTestFailed(String msg);
    void onLoadTitle(String title);
}
