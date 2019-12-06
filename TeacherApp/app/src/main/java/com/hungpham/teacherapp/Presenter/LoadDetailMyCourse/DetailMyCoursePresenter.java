package com.hungpham.teacherapp.Presenter.LoadDetailMyCourse;


import com.hungpham.teacherapp.Model.Entities.Doc;
import com.hungpham.teacherapp.Model.LoadDetailMyCourse.ILoadMyCourseListener;
import com.hungpham.teacherapp.Model.LoadDetailMyCourse.LoadMyCourse;
import com.hungpham.teacherapp.View.LoadDetailMyCourse.ILoadDetailMyCourseView;

import java.util.ArrayList;
import java.util.HashMap;

public class DetailMyCoursePresenter implements ILoadMyCourseListener,IDetailMyCoursePresenter {
    private ILoadDetailMyCourseView loadView;
    private LoadMyCourse mainInterator;
    public DetailMyCoursePresenter(ILoadDetailMyCourseView loadView){
        this.loadView=loadView;
        mainInterator=new LoadMyCourse(this);

    }
    public void loadDetailMyCourse(String studentId,String courseId){
        HashMap<String,Object>tutorMap=new HashMap<>();
        mainInterator.getDetailStudent(studentId,tutorMap);
        mainInterator.loadCourseDoc(courseId);
    }
    public void setToken(String token,String userId){
        mainInterator.updateToken(userId,token);
    }

    @Override
    public void onLoadTutorMyCourse(HashMap<String, Object> studentMap) {
        loadView.onDisplayStudent(studentMap);
    }

    @Override
    public void onLoadDocMyCourse(ArrayList<Doc> docList) {
        loadView.onDisplayDoc(docList);
    }

    @Override
    public void offlineStatus(String msg) {
        loadView.onDisplayOffline(msg);
    }

    @Override
    public void onlineStatus(String msg) {
        loadView.onDisplayOnline(msg);
    }

    @Override
    public void updateToken(String msg) {
        loadView.onUpdateToken(msg);
    }
}
