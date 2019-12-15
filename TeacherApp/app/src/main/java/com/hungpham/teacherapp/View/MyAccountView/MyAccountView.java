package com.hungpham.teacherapp.View.MyAccountView;

import java.util.HashMap;

public interface MyAccountView {
    void updateInform(HashMap<String, Object> informMap);
    void onSuccess(String msg);
    void onError(String msg);
    void onLoading(int percent);
}
