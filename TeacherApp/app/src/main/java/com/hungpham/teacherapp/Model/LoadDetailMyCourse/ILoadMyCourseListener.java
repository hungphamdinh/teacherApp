package com.hungpham.teacherapp.Model.LoadDetailMyCourse;


import com.hungpham.teacherapp.Model.Entities.Doc;

import java.util.ArrayList;
import java.util.HashMap;

public interface ILoadMyCourseListener {
    void onLoadTutorMyCourse(HashMap<String, Object> studentMap);
    void onLoadDocMyCourse(ArrayList<Doc> docList);
    void offlineStatus(String msg);
    void onlineStatus(String msg);
    void updateToken(String msg);
}
