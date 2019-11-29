package com.hungpham.teacherapp.Model;

import java.util.List;

public class Request {
    public List<Order> course;
    public String name;
    public String phone;
    public String total;

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String courseId;
    public Request(){

    }

    public Request(List<Order> course, String name, String phone, String total) {
        this.course = course;
        this.name = name;
        this.phone = phone;
        this.total = total;
    }

    public List<Order> getCourse() {
        return course;
    }

    public void setCourse(List<Order> course) {
        this.course = course;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
