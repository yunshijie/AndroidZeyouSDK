package com.zeyou.uilibs.chat;


import com.zeyou.uilibs.BasePresenter;
import com.zeyou.uilibs.BaseView;
import com.zeyou.uilibs.util.emoji.InputUser;

import java.util.List;

/**
 * 观看页的接口类
 */
public class ChatContract {

    public interface ChatView extends BaseView<ChatPresenter> {
/*
void notifyDataChanged(ChatServer.ChatInfo data);

void notifyDataChanged(List<ChatServer.ChatInfo> list);
*/

        void showToast(String content);

        void clearChatData();

        void performSend(String content, int chatEvent);
    }

    public interface ChatPresenter extends BasePresenter {

        void showChatView(boolean emoji, InputUser user, int limit);

        void sendChat(String text);

        void sendQuestion(String content);

        void onLoginReturn();

        void onFreshData();

        void showSurvey(String surveyid);

    }


}
