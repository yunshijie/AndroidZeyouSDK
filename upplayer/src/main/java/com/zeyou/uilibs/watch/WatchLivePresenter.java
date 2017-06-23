package com.zeyou.uilibs.watch;

import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.zeyou.uilibs.Param;
import com.zeyou.uilibs.chat.ChatContract;
import com.zeyou.uilibs.util.emoji.InputUser;
import com.zeyou.upplayer.R;
import com.zeyou.zeyousdklib.WatchLive;
import com.zeyou.zeyousdklib.ZeyouSDK;
import com.zeyou.zeyousdklib.utils.CostUtil;
import com.zeyou.zeyousdklib.utils.ErrorCode;

import java.util.List;

import tv.danmaku.ijk.media.player.IMediaPlayer;


/**
 * 观看直播的Presenter
 */
public class WatchLivePresenter implements WatchContract.WatchPresenter, WatchContract.LivePresenter, ChatContract.ChatPresenter {
    private static final String TAG = "WatchLivePresenter";
    private Param params;
    private WatchContract.LiveView liveView;

    WatchContract.DocumentView documentView;
    WatchContract.WatchView watchView;
    ChatContract.ChatView chatView;
    ChatContract.ChatView questionView;

    public boolean isWatching = false;
    private WatchLive watchLive;

    int[] scaleTypes = new int[]
            {WatchLive.FIT_DEFAULT, WatchLive.FIT_CENTER_INSIDE, WatchLive.FIT_X, WatchLive.FIT_Y, WatchLive.FIT_XY};
    int currentPos = 0;
    private int scaleType = WatchLive.FIT_DEFAULT;
//    private MessageServer.MsgInfo messageServer;

    public WatchLivePresenter(WatchContract.LiveView liveView, WatchContract.DocumentView documentView, ChatContract.ChatView chatView, ChatContract.ChatView questionView, WatchContract.WatchView watchView, Param param) {
        this.params = param;
        this.liveView = liveView;
        this.documentView = documentView;
        this.watchView = watchView;
        this.questionView = questionView;
        this.chatView = chatView;
        this.watchView.setPresenter(this);
        this.liveView.setPresenter(this);
        this.chatView.setPresenter(this);
        this.questionView.setPresenter(this);
//        this.messageServer = new MessageServer.MsgInfo();
    }

    @Override
    public void start() {
        initWatch();
    }


    @Override
    public void showChatView(boolean emoji, InputUser user, int limit) {
        watchView.showChatView(emoji, user, limit);
    }

    @Override
    public void sendChat(String text) {
//        getWatchLive().sendChat(text, new zeyouSDK.RequestCallback() {
//            @Override
//            public void onSuccess() {
//            }
//
//            @Override
//            public void onError(int errorCode, String reason) {
//                chatView.showToast(reason);
//            }
//        });
    }

    @Override
    public void sendQuestion(String content) {
//        if (TextUtils.isEmpty(params.userZeyouId)) {
//            Toast.makeText(liveView.getmActivity(), R.string.zeyou_login_first, Toast.LENGTH_SHORT).show();
//            return;
//        }
//        getWatchLive().sendQuestion(content, params.userZeyouId, new zeyouSDK.RequestCallback() {
//            @Override
//            public void onSuccess() {
//            }
//
//            @Override
//            public void onError(int errorCode, String reason) {
//                questionView.showToast(reason);
//            }
//        });
    }

    @Override
    public void onLoginReturn() {
        initWatch();
    }

    @Override
    public void onFreshData() {

    }

    @Override
    public void showSurvey(String surveyid) {
        if (TextUtils.isEmpty(params.userZeyouId)) {
            liveView.showToast("请先登录！");
            return;
        }
//        zeyouSDK.getInstance().getSurveyInfo(surveyid, new SurveyDataSource.SurveyInfoCallback() {
//            @Override
//            public void onSuccess(Survey survey) {
//                watchView.showSurvey(survey);
//            }
//
//            @Override
//            public void onError(int errorCode, String errorMsg) {
//                chatView.showToast(errorMsg);
//            }
//        });
    }

    @Override
    public void onSwitchPixel(int level) {
        if (getWatchLive().getDefinition() == level) {
            liveView.showToast("已经切过了!!!");
            return;
        }
        if (liveView.getmActivity().isFinishing()) {
            return;
        }
        if (isWatching) {
            stopWatch();
        }
        getWatchLive().setDefinition(level);
        /** 停止观看 不能立即重连 要延迟一秒重连*/
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isWatching && !liveView.getmActivity().isFinishing()) {
                    startWatch();
                }
            }
        }, 500);
    }


    @Override
    public int setScaleType() {
        scaleType = scaleTypes[(++currentPos) % scaleTypes.length];
        getWatchLive().setScaleType(scaleType);
        liveView.setScaleButtonText(scaleType);
        return scaleType;
    }

    @Override
    public int changeOriention() {
        int ori = liveView.changeOrientation();
        if (ori == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            watchView.setShowDetail(true);
        } else {
            watchView.setShowDetail(false);
        }
        return ori;
    }

    @Override
    public void onDestory() {
        getWatchLive().destory();
    }

    @Override
    public void submitLotteryInfo(String nickname, String phone) {
//        if (!TextUtils.isEmpty(messageServer.id) && !TextUtils.isEmpty(messageServer.lottery_id)) {
//            zeyouSDK.getInstance().submitLotteryInfo(messageServer.id, messageServer.lottery_id, nickname, phone, new zeyouSDK.RequestCallback() {
//                @Override
//                public void onSuccess() {
//                    liveView.showToast("信息提交成功");
//                }
//
//                @Override
//                public void onError(int errorCode, String reason) {
//
//                }
//            });
//        }
    }

    @Override
    public int getCurrentPixel() {
        return getWatchLive().getDefinition();
    }

    @Override
    public int getScaleType() {
        if (getWatchLive() != null) {
            return getWatchLive().getScaleType();
        }
        return -1;
    }

    private Handler handler = new Handler();
    private void processingKnockDoor() {
        liveView.showProcessingKnock();
        //                        2s获取一次申请状态
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ZeyouSDK.getInstance().doorState(params.userZeyouId, params.watchId, "", new ZeyouSDK.RequestCallback() {
                    @Override
                    public void onSuccess() {
                        liveView.hideProcessingKnock();
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
                                liveView.showKnock();
                                break;
                        }

                        liveView.showToast(errorCode+":"+reason);
                    }
                });
            }
        }, 2 * 1000);

    }
    @Override
    public void knockDoor(String msg) {
        if (TextUtils.isEmpty(msg)) {
            return;
        }

        ZeyouSDK.getInstance().knockDoor(params.userZeyouId, params.watchId, "", params.userName, msg, new ZeyouSDK.RequestCallback() {
            @Override
            public void onSuccess() {
                liveView.hideKnock();
                initWatch();
            }

            @Override
            public void onError(int errorCode, String reason) {

                liveView.showToast(errorCode+":"+reason);
            }
        });
    }

    @Override
    public void checkPwd(String pwd) {

        if (TextUtils.isEmpty(pwd)) {
            return;
        }
        params.roomPwd = pwd;
        Param.setParam(ZeyouSDK.getInstance().getContext(), params);
        initWatch();
    }

    @Override
    public void initWatch() {
        ZeyouSDK.getInstance().initWatch(params.watchId, params.userName, params.userCustomId, params.userZeyouId, params.key,params.roomPwd,params.user_type+"",params.ip,params.sys,params.addr,params.src,  getWatchLive(), new ZeyouSDK.RequestCallback() {
            @Override
            public void onSuccess() {
                liveView.showRadioButton(getWatchLive().getDefinitionAvailable());
                chatView.clearChatData();
                getChatHistory();
            }

            @Override
            public void onError(int errorCode, String msg) {


                switch (errorCode) {
                    case ErrorCode.REJECT_KNOCK_DOOR:

                        break;
                    case ErrorCode.PROCESSING_KNOCK_DOOR:
//                        2s获取一次申请状态
                        processingKnockDoor();
                        break;
                    case ErrorCode.NEED_KNOCK_DOOR:
                        liveView.showKnock();
                        break;
                    case ErrorCode.NEED_PASSWORD:
                        liveView.showPwd();
                        break;
                }

              liveView.showToast(errorCode+":"+msg);
            }

        });
    }


    @Override
    public void onWatchBtnClick() {
        if (isWatching) {
            stopWatch();
        } else {
            if (getWatchLive().isAvaliable()) {
                startWatch();
            } else {
                initWatch();
            }
        }
    }

    @Override
    public void startWatch() {
        getWatchLive().start();
        isWatching = true;
    }


    @Override
    public void stopWatch() {
        if (isWatching) {
            getWatchLive().stop();
            isWatching = false;
            liveView.setPlayPicture(!isWatching);
        }
    }

    public WatchLive getWatchLive() {
        if (watchLive == null) {
            WatchLive.Builder builder = new WatchLive.Builder()
                    .context(liveView.getmActivity())
                    .containerLayout(liveView.getWatchLayout())
//                    .bufferDelay(params.bufferSecond)
//                    .callback(new WatchCallback())
                    .playView(liveView.getPlayView());
//                    .messageCallback(new MessageEventCallback())
//                    .chatCallback(new ChatCallback());
            watchLive = builder.build();
        }
        return watchLive;
    }

    //签到
    @Override
    public void signIn(String signId) {
//        zeyouSDK.getInstance().performSignIn(params.watchId, params.userZeyouId, params.userName, signId, new zeyouSDK.RequestCallback() {
//            @Override
//            public void onSuccess() {
//                liveView.showToast("签到成功");
//                watchView.dismissSignIn();
//            }
//
//            @Override
//            public void onError(int errorCode, String errorMsg) {
//                liveView.showToast(errorMsg);
//            }
//        });
    }

    //提交问卷 需要先登录且watch已初始化完成
//    @Override
//    public void submitSurvey(Survey survey, String result) {
//        if (survey == null)
//            return;
//        if (TextUtils.isEmpty(params.userZeyouId)) {
//            liveView.showToast("请先登录！");
//            return;
//        }
//        zeyouSDK.getInstance().submitSurveyInfo(params.userZeyouId, getWatchLive(), survey.surveyid, result, new zeyouSDK.RequestCallback() {
//            @Override
//            public void onSuccess() {
//                liveView.showToast("提交成功！");
//                watchView.dismissSurvey();
//            }
//
//            @Override
//            public void onError(int errorCode, String errorMsg) {
//                liveView.showToast(errorMsg);
//                if (errorCode == 10821)
//                    watchView.dismissSurvey();
//            }
//        });
//    }

    /**
     * 观看过程中事件监听
     */
    private class WatchCallback implements WatchLive.WatchEventCallback {
        @Override
        public void onError(int errorCode, String errorMsg) {
            switch (errorCode) {
                case WatchLive.WatchEventCallback.STATE_ERROR_COMMON:
                case WatchLive.WatchEventCallback.STATE_ERROR_INIT_VIDEO:
                default:
                    isWatching = false;
                    liveView.showLoading(false);
                    liveView.setPlayPicture(isWatching);
                    liveView.showToast(errorMsg);
                    break;

            }
        }

        @Override
        public void onStateChanged(int stateCode) {
            switch (stateCode) {
                case WatchLive.WatchEventCallback.STATE_START:
                    isWatching = true;
                    liveView.setPlayPicture(isWatching);
                    break;
                case  IMediaPlayer.MEDIA_INFO_BUFFERING_START:
                    Log.e(TAG, "STATE_BUFFER_START  ");
                    if (isWatching)
                        liveView.showLoading(true);
                    break;
                case  IMediaPlayer.MEDIA_INFO_BUFFERING_END:
                    liveView.showLoading(false);
                    break;
                case  WatchLive.WatchEventCallback.STATE_STOP:
                    isWatching = false;
                    liveView.setPlayPicture(isWatching);
                    break;
            }
        }

        @Override
        public void uploadSpeed(String kbps) {
            liveView.setDownSpeed("速率" + kbps + "/kbps");
        }

    }

    /**
     * 观看过程消息监听
     */
    /*private class MessageEventCallback implements MessageServer.Callback {
        @Override
        public void onEvent(MessageServer.MsgInfo messageInfo) {
            Log.e(TAG, "messageInfo.event : " + messageInfo.event);
            switch (messageInfo.event) {
                case MessageServer.EVENT_PPT_CHANGED://PPT翻页消息
                    documentView.showDoc(messageInfo.pptUrl);
                    break;
                case MessageServer.EVENT_DISABLE_CHAT://禁言
                    break;
                case MessageServer.EVENT_KICKOUT://踢出
                    break;
                case MessageServer.EVENT_OVER://直播结束
                    liveView.showToast("直播已结束");
                    stopWatch();
                    break;
                case MessageServer.EVENT_PERMIT_CHAT://解除禁言
                    break;
                case MessageServer.EVENT_START_LOTTERY://抽奖开始
                    liveView.showDialogStatus(1, false, null);
                    break;
                case MessageServer.EVENT_END_LOTTERY://抽奖结束
                    String[] nameList = null;
                    if (messageInfo != null && !TextUtils.isEmpty(messageInfo.id)) {
                        messageServer.id = messageInfo.id;
                        messageServer.lottery_id = messageInfo.lottery_id;
                    }
                    if (messageInfo.lists != null && messageInfo.lists.size() > 0) {
                        nameList = new String[messageInfo.lists.size()];
                        for (int i = 0; i < messageInfo.lists.size(); i++) {
                            nameList[i] = messageInfo.lists.get(i).nick_name;
                        }
                        liveView.showDialogStatus(2, messageInfo.isLottery, nameList);
                    }
                    break;
                case MessageServer.EVENT_NOTICE:
                    watchView.showNotice(messageInfo.content);
                    Log.e(TAG, "notice:" + messageInfo.content);
                    break;
                case MessageServer.EVENT_SIGNIN: //签到消息
                    if (!TextUtils.isEmpty(messageInfo.sign_id) && !TextUtils.isEmpty(messageInfo.sign_show_time)) {
                        watchView.showSignIn(messageInfo.sign_id, parseTime(messageInfo.sign_show_time, 30));
                    }
                    break;
                case MessageServer.EVENT_QUESTION: // 问答开关
                    liveView.showToast("问答功能已" + (messageInfo.status == 0 ? "关闭" : "开启"));
                    break;
                case MessageServer.EVENT_SURVEY://问卷
                    ChatServer.ChatInfo chatInfo = new ChatServer.ChatInfo();
                    chatInfo.event = "survey";
                    chatInfo.id = messageInfo.survey_id;
                    chatView.notifyDataChanged(chatInfo);
                    break;
            }

        }

        public int parseTime(String str, int defaultTime) {
            int currentTime = 0;
            try {
                currentTime = Integer.parseInt(str);
                if (currentTime == 0) {
                    return defaultTime;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return currentTime;
        }

        @Override
        public void onMsgServerConnected() {

        }

        @Override
        public void onConnectFailed() {
//            getWatchLive().connectMsgServer();
        }

        @Override
        public void onMsgServerClosed() {

        }
    }*/

    /*
    private class ChatCallback implements ChatServer.Callback {
        @Override
        public void onChatServerConnected() {
            Log.e(TAG, " ChatServerConnected");
        }

        @Override
        public void onConnectFailed() {
            Log.e(TAG, " onConnectFailed -----");
//            getWatchLive().connectChatServer();
        }

        @Override
        public void onChatMessageReceived(ChatServer.ChatInfo chatInfo) {
            Log.e(TAG, " onChatMessageReceived -----");
            switch (chatInfo.event) {
                case ChatServer.eventMsgKey:
                    chatView.notifyDataChanged(chatInfo);
                    liveView.addDanmu(chatInfo.msgData.text);
                    break;
                case ChatServer.eventOnlineKey:
                    chatView.notifyDataChanged(chatInfo);
                    break;
                case ChatServer.eventOfflineKey:
                    chatView.notifyDataChanged(chatInfo);
                    break;
                case ChatServer.eventQuestion:
                    questionView.notifyDataChanged(chatInfo);
                    break;
            }
        }

        @Override
        public void onChatServerClosed() {
            Log.e(TAG, " onChatServerClosed -----");
        }
    }*/

    private void getChatHistory() {
//        getWatchLive().acquireChatRecord(true, new ChatServer.ChatRecordCallback() {
//            @Override
//            public void onDataLoaded(List<ChatServer.ChatInfo> list) {
//                Log.e(TAG, "list->" + list.size());
//                chatView.notifyDataChanged(list);
//            }
//
//            @Override
//            public void onFailed(int errorcode, String messaage) {
//                Log.e(TAG, "onFailed->" + errorcode + ":" + messaage);
//            }
//        });
    }

}

