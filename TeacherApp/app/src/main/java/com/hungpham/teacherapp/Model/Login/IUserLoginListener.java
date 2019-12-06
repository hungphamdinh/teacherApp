package com.hungpham.teacherapp.Model.Login;

public interface IUserLoginListener {
    void onLoginSucess(String status);
    void onLoginError(String status);}
