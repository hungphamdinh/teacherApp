package com.hungpham.teacherapp.Presenter.MyCourseList;

import com.hungpham.teacherapp.Adapter.StaffAdapter;
import com.hungpham.teacherapp.Model.Entities.Course;
import com.hungpham.teacherapp.Model.MyCourseList.IMyCourseListListener;
import com.hungpham.teacherapp.Model.MyCourseList.MyCourseList;
import com.hungpham.teacherapp.View.MyCourseList.IMyListCourseView;

import java.util.ArrayList;
import java.util.HashMap;

public class MyCourseListPresenter implements IMyCourseListListener {
    private MyCourseList mainInterator;
    private IMyListCourseView courseView;
    private StaffAdapter.StaffViewHolder holder;
    private ArrayList<Course>courses;
    public MyCourseListPresenter(IMyListCourseView courseView, StaffAdapter.StaffViewHolder holder){
        mainInterator=new MyCourseList(this);
        this.courseView=courseView;
        this.holder=holder;
    }
    public void setCourseList(int pos,String userId){
        //HashMap<String,Object>courseMap=new HashMap<>();
        HashMap<String,Object>tutorMap=new HashMap<>();
        HashMap<String,Object>posMap=new HashMap<>();
        posMap.put("pos",pos);
        posMap.put("userId",userId);
        mainInterator.loadCourse(tutorMap,posMap);
    }


    @Override
    public void onLoadStudentMyCourse(HashMap<String, Object> studentMap) {
        courseView.onDisplayStudent(studentMap,holder);
    }

    @Override
    public void onLoadCourseMyCourse(HashMap<String, Object> courseMap) {
        courseView.onDisplayCourse(courseMap,holder);
    }

    @Override
    public void offlineStatus(String msg) {
        courseView.onDisplayOffline(msg,holder);
    }

    @Override
    public void onlineStatus(String msg) {
        courseView.onDisplayOnline(msg,holder);

    }

    @Override
    public void onLoadDataToClick(ArrayList<String> list) {
        courseView.onLoadDataToClick(list,holder);
    }
}
