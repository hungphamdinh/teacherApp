package com.hungpham.teacherapp.Model.MyCourseList;


import com.hungpham.teacherapp.Model.Entities.Course;

import java.util.ArrayList;

public interface IMyCourseListAdaperListener {
    void callAdapter(ArrayList<Course> courses);
}
