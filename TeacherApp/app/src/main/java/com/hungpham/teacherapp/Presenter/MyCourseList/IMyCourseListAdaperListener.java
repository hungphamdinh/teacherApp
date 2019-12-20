package com.hungpham.teacherapp.Presenter.MyCourseList;


import com.hungpham.teacherapp.Model.Entities.Course;

import java.util.ArrayList;

public interface IMyCourseListAdaperListener {
    void callAdapter(ArrayList<Course> courses,ArrayList<String>keys);
    void onError(String msg);

}
