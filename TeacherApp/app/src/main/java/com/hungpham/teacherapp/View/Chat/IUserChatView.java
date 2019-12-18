package com.hungpham.teacherapp.View.Chat;


import com.hungpham.teacherapp.Model.Entities.Chat;

import java.util.ArrayList;
import java.util.HashMap;

public interface IUserChatView {
    void onClickSendMsg(HashMap<String, Object> msgMap);
    void onError(String msg);
    void readMsg(ArrayList<Chat> chats,ArrayList<String>keys);
    void onAccesstoUser(String tutorName);
}
