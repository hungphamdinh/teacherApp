package com.hungpham.teacherapp.Presenter.LoadDetailMyCourse;

public interface IDetailMyCoursePresenter {
    void loadDetailMyCourse(String tutorId, String courseId);
    void setToken(String token, String userid);
}
