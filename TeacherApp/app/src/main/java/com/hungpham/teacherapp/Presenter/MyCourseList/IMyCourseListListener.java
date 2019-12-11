package com.hungpham.teacherapp.Presenter.MyCourseList;

import java.util.ArrayList;
import java.util.HashMap;

public interface IMyCourseListListener {
    void onLoadStudentMyCourse(HashMap<String, Object> studentMap);
    void onLoadCourseMyCourse(HashMap<String, Object> courseMap);
    void offlineStatus(String msg);
    void onlineStatus(String msg);
    void onLoadDataToClick(ArrayList<String> list);
}