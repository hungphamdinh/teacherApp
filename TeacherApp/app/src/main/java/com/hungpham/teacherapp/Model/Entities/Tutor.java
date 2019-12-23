package com.hungpham.teacherapp.Model.Entities;

public class Tutor {
    private String email;
    private String username;
    private String password;
    private String experience;
    private String status;

    public int getCkWork() {
        return ckWork;
    }

    public void setCkWork(int ckWork) {
        this.ckWork = ckWork;
    }

    private int ckWork;
    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    String avatar;
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Tutor(){}
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }
}
