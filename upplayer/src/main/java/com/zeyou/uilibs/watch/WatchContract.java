package com.zeyou.uilibs.watch;

import android.app.Activity;
import android.widget.SeekBar;


import com.zeyou.uilibs.BasePresenter;
import com.zeyou.uilibs.BaseView;
import com.zeyou.uilibs.util.emoji.InputUser;
import com.zeyou.zeyousdklib.widget.ContainerLayout;
import com.zeyou.zeyousdklib.widget.PlayView;

import java.util.HashMap;

/**
 * 观看页的接口类
 */
public class WatchContract {

    interface WatchView extends BaseView<WatchPresenter> {

        void setShowDetail(boolean isShow);

        void showChatView(boolean emoji, InputUser user, int limit);

        void showNotice(String content);

        void dismissNotice();

        void showSignIn(String signId, int startTime);

        void dismissSignIn();

//        void showSurvey(Survey survey);

        void dismissSurvey();
    }

    interface DocumentView extends BaseView<BasePresenter> {
        void showDoc(String docUrl);

//        void rePainBoard(MessageServer.MsgInfo msgInfo);
    }

    interface DetailView extends BaseView<BasePresenter> {
    }

    interface LiveView extends BaseView<LivePresenter> {
        Activity getmActivity();

        ContainerLayout getWatchLayout();

        void setPlayPicture(boolean state);

        void setDownSpeed(String text);

        void showLoading(boolean isShow);

        int changeOrientation();

        void showToast(String message);

        void showRadioButton(HashMap map);

        void setScaleButtonText(int type);

        void showDialogStatus(int level, boolean isLottery, String[] nameList);

//        void addDanmu(String danmu);
        PlayView getPlayView();


        void showProcessingKnock();
        void hideProcessingKnock();
        void showKnock();
        void hideKnock();
        void showPwd();
        void hidePwd();
    }


    interface PlaybackView extends BaseView<PlaybackPresenter> {
        Activity getmActivity();

        void setPlayIcon(boolean isStop);

        void setProgressLabel(String currentTime, String max);

        void setSeekbarMax(int max);

        void setSeekbarCurrentPosition(int position);

        void showProgressbar(boolean show);

        void showToast(String message);

        ContainerLayout getContainer();

        void setScaleTypeText(int type);

        int changeScreenOri();
        PlayView getPlayView();


        void showProcessingKnock();
        void hideProcessingKnock();
        void showKnock();
        void hideKnock();
        void showPwd();
        void hidePwd();
    }

    interface PlaybackPresenter extends BasePresenter {
        void onFragmentStop();

        void onFragmentDestory();

        void onPlayClick();

        void startPlay();

        void paushPlay();

        void stopPlay();

        void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser);

        void onStopTrackingTouch(SeekBar seekBar);

        int changeScaleType();

        int changeScreenOri();

        void knockDoor(String msg);
        void checkPwd(String pwd);
    }

    interface LivePresenter extends BasePresenter {

        void initWatch();

        void startWatch();

        void stopWatch();

        void onWatchBtnClick();

        void onSwitchPixel(int pixel);// 切换分辨率

        int setScaleType();

        int changeOriention();

        void onDestory();

        void submitLotteryInfo(String nickname, String phone);

        int getCurrentPixel();

        int getScaleType();

        void knockDoor(String msg);
        void checkPwd(String pwd);
    }

    interface WatchPresenter extends BasePresenter {

        void signIn(String signId);

//        void submitSurvey(Survey survey, String result);
    }
}
