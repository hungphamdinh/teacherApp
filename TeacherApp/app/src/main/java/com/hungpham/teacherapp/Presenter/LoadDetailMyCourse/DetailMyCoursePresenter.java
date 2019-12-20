package com.hungpham.teacherapp.Presenter.LoadDetailMyCourse;


import com.hungpham.teacherapp.Model.Entities.Doc;
import com.hungpham.teacherapp.View.LoadDetailMyCourse.ILoadDetailMyCourseView;

import java.util.ArrayList;
import java.util.HashMap;

public class DetailMyCoursePresenter implements ILoadMyCourseListener {
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
        mainInterator.loadTutorTest(courseId);
    }
    public void setToken(String token,String userId){
        mainInterator.updateToken(userId,token);
    }
    public void addTest(String courseId,HashMap<String,Object>edtMap){
        mainInterator.addTest(courseId,edtMap);
    }
    public void setStatus(String status,String phoneKey){
        mainInterator.setStatus(status,phoneKey);
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
    public void onLoadTutorTest(ArrayList<Doc> docArrayList, ArrayList<String> key) {
        loadView.onDisplayTutorTest(docArrayList,key);
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

    @Override
    public void onNullItem(String msg) {
        loadView.onNullItem(msg);
    }

    @Override
    public void addTestSuccess(String msg) {
        loadView.onAddTestSuccess(msg);
    }

    @Override
    public void addTestFailed(String msg) {
        loadView.onAddTestFailed(msg);
    }

    @Override
    public void onLoadTitle(String title) {
        loadView.onLoadTitle(title);
    }


}
