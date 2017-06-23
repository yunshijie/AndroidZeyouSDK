package com.zeyou.uilibs.broadcast;


import com.zeyou.uilibs.BasePresenter;
import com.zeyou.uilibs.BaseView;
import com.zeyou.uilibs.util.emoji.InputUser;

/**
 * 发直播的View接口类
 */
public class BroadcastContract {

    interface BraodcastView extends BaseView<Presenter> {
        void showChatView(boolean emoji, InputUser user, int limit);
    }

    interface View extends BaseView<Presenter> {

//        CameraFilterView getCameraView();

        void initCamera(int piexl_type);

        void setStartBtnImage(boolean start);

        void setFlashBtnImage(boolean open);

        void setAudioBtnImage(boolean open);

        void setFlashBtnEnable(boolean enable);

        void setCameraBtnEnable(boolean enable);

        void showMsg(String msg);

        void setSpeedText(String text);
    }

    interface Presenter extends BasePresenter {
        void onstartBtnClick();

        void initBroadcast();

        void startBroadcast();

        void stopBroadcast();

        void finishBroadcast();

        void changeFlash();

        void changeCamera();

        void changeAudio();

        void onPause();

        void onDestory();

        void onResume();
    }
}
