package com.hungpham.teacherapp.Presenter.MyCourseList;


import com.hungpham.teacherapp.Model.Entities.Course;
import com.hungpham.teacherapp.Model.Entities.Request;
import com.hungpham.teacherapp.Model.MyCourseList.IMyCourseListAdaperListener;
import com.hungpham.teacherapp.Model.MyCourseList.MyCourseListCallAdapter;
import com.hungpham.teacherapp.View.MyCourseList.IMyCourseAdapterView;

import java.util.ArrayList;

public class MyCourseListAdapterPre implements IMyCourseListAdaperListener {
    private IMyCourseAdapterView myCourseAdapterView;
    private MyCourseListCallAdapter mainInterator;
    public MyCourseListAdapterPre(IMyCourseAdapterView myCourseAdapterView){
        this.myCourseAdapterView=myCourseAdapterView;
        this.mainInterator=new MyCourseListCallAdapter(this);
    }
    public void setAdapter(String userPhone){
        mainInterator.loadTutor(userPhone);
    }


    @Override
    public void callAdapter(ArrayList<Course> courses) {
        myCourseAdapterView.callAdapter(courses);
    }
}