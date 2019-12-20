package com.hungpham.teacherapp.View.MyCourseList;


import com.hungpham.teacherapp.Adapter.StaffAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public interface IMyListCourseView {
    void onDisplayStudent(HashMap<String, Object> map, StaffAdapter.StaffViewHolder holder);
    void onDisplayCourse(HashMap<String, Object> map, StaffAdapter.StaffViewHolder holder,int pos);
    void onDisplayOnline(String msg, StaffAdapter.StaffViewHolder holder);
    void onDisplayOffline(String msg, StaffAdapter.StaffViewHolder holder);
    void onNullItem(String msg, StaffAdapter.StaffViewHolder holder);
    void onLoadDataToClick(ArrayList<String> dataList, StaffAdapter.StaffViewHolder holder);
}
