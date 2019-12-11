package com.hungpham.teacherapp.Presenter.Login;

public interface IUserLoginListener {
    void onLoginSucess(String status);
    void onLoginError(String status);}
