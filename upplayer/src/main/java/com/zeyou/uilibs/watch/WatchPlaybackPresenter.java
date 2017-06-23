package com.zeyou.uilibs.watch;

import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.Toast;


import com.zeyou.uilibs.Param;
import com.zeyou.uilibs.chat.ChatContract;
import com.zeyou.uilibs.util.ZeyouUtil;
import com.zeyou.uilibs.util.emoji.InputUser;
import com.zeyou.upplayer.R;
import com.zeyou.zeyousdklib.WatchLive;
import com.zeyou.zeyousdklib.WatchPlayback;
import com.zeyou.zeyousdklib.ZeyouSDK;
import com.zeyou.zeyousdklib.utils.CostUtil;
import com.zeyou.zeyousdklib.utils.ErrorCode;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * 观看回放的Presenter
 */
public class WatchPlaybackPresenter implements WatchContract.PlaybackPresenter, ChatContract.ChatPresenter {
    private static final String TAG = "PlaybackPresenter";
    private Param param;
    WatchContract.PlaybackView playbackView;
    WatchContract.DocumentView documentView;
    WatchContract.WatchView watchView;
    ChatContract.ChatView chatView;
    private int limit = 5;
    private int pos = 0;

    private WatchPlayback watchPlayback;
//    private zeyouPPT ppt;

    int[] scaleTypeList = new int[]{WatchLive.FIT_DEFAULT, WatchLive.FIT_CENTER_INSIDE, WatchLive.FIT_X, WatchLive.FIT_Y, WatchLive.FIT_XY};
    int currentPos = 0;
    private int scaleType = WatchLive.FIT_DEFAULT;

    private int playerCurrentPosition = 0;
    private long playerDuration;
    private String playerDurationTimeStr = "00:00:00";

    private Timer timer;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (getWatchPlayback().isPlaying()) {

                playerDuration = getWatchPlayback().getDuration();
                playerDurationTimeStr = ZeyouUtil.converLongTimeToStr(playerDuration);

                playerCurrentPosition = getWatchPlayback().getCurrentPosition();
                playbackView.setSeekbarCurrentPosition((int) playerCurrentPosition);
                String playerCurrentPositionStr = ZeyouUtil.converLongTimeToStr(playerCurrentPosition);
                //playbackView.setProgressLabel(playerCurrentPositionStr + "/" + playerDurationTimeStr);
                playbackView.setProgressLabel(playerCurrentPositionStr, playerDurationTimeStr);
//                if (ppt != null) {
//                    String url = ppt.getPPT(playerCurrentPosition / 1000);
//                    documentView.showDoc(url);
//                }
            }
        }
    };

    public WatchPlaybackPresenter(WatchContract.PlaybackView playbackView, WatchContract.DocumentView documentView, ChatContract.ChatView chatView, WatchContract.WatchView watchView, Param param) {
        this.playbackView = playbackView;
        this.documentView = documentView;
        this.watchView = watchView;
        this.chatView = chatView;
        this.param = param;
        this.playbackView.setPresenter(this);
        this.chatView.setPresenter(this);
    }

    @Override
    public void start() {
        playbackView.setScaleTypeText(scaleType);
        initWatch();
    }

    private void initCommentData(int pos) {
//        watchPlayback.requestCommentHistory(param.watchId, limit, pos, new ChatServer.ChatRecordCallback() {
//            @Override
//            public void onDataLoaded(List<ChatServer.ChatInfo> list) {
//                chatView.notifyDataChanged(list);
//            }
//
//            @Override
//            public void onFailed(int errorcode, String messaage) {
//                playbackView.showToast(messaage);
//            }
//        });
    }

    private void processingKnockDoor() {
        playbackView.showProcessingKnock();
        //                        2s获取一次申请状态
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ZeyouSDK.getInstance().doorState(param.userZeyouId, param.watchId, "", new ZeyouSDK.RequestCallback() {
                    @Override
                    public void onSuccess() {
                        playbackView.hideProcessingKnock();
                        initWatch();
                    }

                    @Override
                    public void onError(int errorCode, String reason) {
                        switch (errorCode) {
                            case ErrorCode.REJECT_KNOCK_DOOR:

                                break;
                            case ErrorCode.PROCESSING_KNOCK_DOOR:
                                //  2s获取一次申请状态
                                processingKnockDoor();
                                break;
                            case ErrorCode.NEED_KNOCK_DOOR:
                                playbackView.showKnock();
                                break;
                        }

                        playbackView.showToast(errorCode+":"+reason);
                    }
                });
            }
        }, 5 * 1000);

    }

    @Override
    public void knockDoor(String msg) {
        if (TextUtils.isEmpty(msg)) {
            return;
        }

        ZeyouSDK.getInstance().knockDoor(param.userZeyouId, param.watchId, "", param.userName, msg, new ZeyouSDK.RequestCallback() {
            @Override
            public void onSuccess() {
                playbackView.hideKnock();
                initWatch();
            }

            @Override
            public void onError(int errorCode, String reason) {

                playbackView.showToast(errorCode+":"+reason);
            }
        });
    }

    @Override
    public void checkPwd(String pwd) {

        if (TextUtils.isEmpty(pwd)) {
            return;
        }
        param.roomPwd = pwd;
        Param.setParam(ZeyouSDK.getInstance().getContext(), param);
        initWatch();
    }

    private void initWatch() {
        ZeyouSDK.getInstance().initWatch(param.watchId, param.userName, param.userCustomId, param.userZeyouId, param.key, param.roomPwd, param.user_type + "", param.ip, param.sys, param.addr, param.src, getWatchPlayback(), new ZeyouSDK.RequestCallback() {
            @Override
            public void onSuccess() {

                playbackView.hidePwd();

                handlePosition();
                setPPT();
                pos = 0;
                initCommentData(pos);
                watchView.showNotice(getWatchPlayback().getNotice()); //显示公告
            }

            @Override
            public void onError(int errorCode, String reason) {

                switch (errorCode) {
                    case ErrorCode.REJECT_KNOCK_DOOR:

                        break;
                    case ErrorCode.PROCESSING_KNOCK_DOOR:
//                        2s获取一次申请状态
                        processingKnockDoor();
                        break;
                    case ErrorCode.NEED_KNOCK_DOOR:
                        playbackView.showKnock();
                        break;
                    case ErrorCode.NEED_PASSWORD:
                        playbackView.showPwd();
                        break;
                }

                playbackView.showToast(errorCode+":"+reason);
            }
        });
    }

    @Override
    public void onFragmentStop() {
        paushPlay();
    }

    @Override
    public void onFragmentDestory() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        stopPlay();

        getWatchPlayback().destory();
    }

    @Override
    public void startPlay() {
        if (!getWatchPlayback().isAvaliable())
            return;
        playbackView.setPlayIcon(false);
        getWatchPlayback().start();
    }


    @Override
    public void paushPlay() {
        getWatchPlayback().pause();
        playbackView.setPlayIcon(true);
    }

    @Override
    public void stopPlay() {
        getWatchPlayback().stop();
        playbackView.setPlayIcon(true);
    }

    @Override
    public void onPlayClick() {
        if (getWatchPlayback().isPlaying()) {
            paushPlay();
        } else {
            if (getWatchPlayback().isAvaliable()) {
                startPlay();
            } else {
                initWatch();
            }
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        Log.e(TAG, "ZeyouUtil.converLongTimeToStr(progress) " + ZeyouUtil.converLongTimeToStr(progress));
        Log.e(TAG, "playerDurationTimeStr " + playerDurationTimeStr);
        playbackView.setProgressLabel(ZeyouUtil.converLongTimeToStr(progress), playerDurationTimeStr);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        playerCurrentPosition = seekBar.getProgress();
        if (!getWatchPlayback().isPlaying()) {
            startPlay();
        }
        getWatchPlayback().seekTo(playerCurrentPosition);
    }

    @Override
    public int changeScaleType() {
        scaleType = scaleTypeList[(++currentPos) % scaleTypeList.length];
        getWatchPlayback().setScaleType(scaleType);
        playbackView.setScaleTypeText(scaleType);
        return scaleType;
    }


    @Override
    public int changeScreenOri() {
        int ori = playbackView.changeScreenOri();
        if (ori == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            watchView.setShowDetail(true);
        } else {
            watchView.setShowDetail(false);
        }
        return playbackView.getmActivity().getRequestedOrientation();
    }


    public WatchPlayback getWatchPlayback() {
        if (watchPlayback == null) {
            WatchPlayback.Builder builder = new WatchPlayback.Builder().context(playbackView.getmActivity()).containerLayout(playbackView.getContainer())

//                    .callback(new WatchLive.WatchEventCallback() {
//                @Override
//                public void onError(int errorCode, String errorMsg) {
//
//                    Toast.makeText(playbackView.getmActivity(), errorMsg, Toast.LENGTH_SHORT).show();
//                    playbackView.setPlayIcon(true);
//
//                    playbackView.showProgressbar(false);
//                    stopPlay();
//
//                    switch (errorCode) {
//                        case WatchLive.ERROR_CONNECT:
//
//                            break;
//                        default:
//                            break;
//                    }
//                }
//
//                @Override
//                public void onStateChanged(int stateCode) {
//                    switch (stateCode) {
//                        case WatchLive.WatchEventCallback.STATE_START:
//                            playbackView.showProgressbar(false);
//                            playbackView.setSeekbarMax((int) playerDuration);
//                            Log.e(TAG, "STATE_READY");
//                            break;
//                        case IMediaPlayer.MEDIA_INFO_BUFFERING_START:
//                            playbackView.showProgressbar(true);
//                            break;
//                        case IMediaPlayer.MEDIA_INFO_BUFFERING_END:
//                            break;
//                        case WatchLive.WatchEventCallback.STATE_STOP:
//                            playbackView.showProgressbar(false);
//                            Log.e(TAG, "STATE_ENDED");
//                            stopPlay();
//                            break;
//                    }
//                }
//
//                @Override
//                public void uploadSpeed(String kbps) {
//                }
//
//
//            })
                    .playView(playbackView.getPlayView());
            watchPlayback = builder.build();
        }
        return watchPlayback;
    }

    //每秒获取一下进度
    private void handlePosition() {
        if (timer != null)
            return;
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(0);
            }
        }, 1000, 1000);
    }

    //如果没有PPT业务，可忽略
    private void setPPT() {
//        if (ppt == null)
//            ppt = new zeyouPPT();
//        getWatchPlayback().setzeyouPPT(ppt);
    }

    @Override
    public void showChatView(boolean emoji, InputUser user, int limit) {
        watchView.showChatView(emoji, user, limit);
    }

    @Override
    public void sendChat(final String text) {
        if (TextUtils.isEmpty(param.userZeyouId)) {
            Toast.makeText(playbackView.getmActivity(), R.string.zeyou_login_first, Toast.LENGTH_SHORT).show();
            return;
        }
//        getWatchPlayback().sendComment(text, param.userZeyouId, new ZeyouSDK.RequestCallback() {
//            @Override
//            public void onSuccess() {
//                chatView.clearChatData();
//                initCommentData(pos = 0);
//            }
//
//            @Override
//            public void onError(int errorCode, String reason) {
//                playbackView.showToast(reason);
//            }
//        });
    }

    @Override
    public void sendQuestion(String content) {

    }

    @Override
    public void onLoginReturn() {

    }

    @Override
    public void onFreshData() {
        pos = pos + limit;
        initCommentData(pos);
    }

    @Override
    public void showSurvey(String surveyid) {

    }
}
