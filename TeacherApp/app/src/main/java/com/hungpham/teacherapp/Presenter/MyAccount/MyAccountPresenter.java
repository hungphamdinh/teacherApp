package com.hungpham.teacherapp.Presenter.MyAccount;



import com.hungpham.teacherapp.View.MyAccountView.MyAccountView;

import java.util.HashMap;

public class MyAccountPresenter implements MyAccountListener {
    private MyAccountView myAccountView;
    private MyAccount mainInterator;
    public MyAccountPresenter(MyAccountView myAccountView){
        this.myAccountView=myAccountView;
        this.mainInterator=new MyAccount(this);
    }
    public void loadData(String phoneKey,HashMap<String,Object>map){
        mainInterator.updateTutor(phoneKey,map);
    }
    public void setOnClick(String phoneKey,HashMap<String,Object>map){
        mainInterator.setOnClick(phoneKey,map);
    }
    @Override
    public void updateInform(HashMap<String, Object> informMap) {
        myAccountView.updateInform(informMap);
    }

    @Override
    public void onSuccess(String msg) {
        myAccountView.onSuccess(msg);
    }

    @Override
    public void onError(String msg) {
        myAccountView.onError(msg);
    }

    @Override
    public void onLoading(int percent) {
        myAccountView.onLoading(percent);
    }
}
