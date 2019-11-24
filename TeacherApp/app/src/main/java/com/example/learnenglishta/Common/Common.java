package com.example.learnenglishta.Common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.learnenglishta.Model.Tutor;

public class Common {
  public static Tutor currentUser;
  public static final String UPDATE = "Update";
  public static final String DELETE= "Delete";
  public static final String USER_KEY = "User";
  public static final String PWD_KEY= "Password";
  public static boolean isConnectedToInternet(Context context){
    ConnectivityManager connectivityManager=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
    if(connectivityManager!=null){
      NetworkInfo[] infos=connectivityManager.getAllNetworkInfo();
      if(infos!=null){
        for (int i=0;i<infos.length;i++){
          if (infos[i].getState()==NetworkInfo.State.CONNECTED)
            return true;
        }
      }
    }
    return false;
  }

}
