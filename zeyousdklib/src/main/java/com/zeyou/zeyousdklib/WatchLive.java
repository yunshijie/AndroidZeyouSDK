package com.zeyou.zeyousdklib;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Handler;
import android.os.Message;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;


import com.zeyou.zeyousdklib.data.WebinarInfo;

import com.zeyou.zeyousdklib.data.WebinarInfo.Stream;
import com.zeyou.zeyousdklib.utils.LogManager;
import com.zeyou.zeyousdklib.widget.ContainerLayout;
import com.zeyou.zeyousdklib.widget.ContainerLayout.ReFixListener;
import com.zeyou.zeyousdklib.widget.PlayView;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

public class WatchLive
        extends Watch {
    private static final String TAG = "WatchLive";
    public static final int DPI_DEFAULT = 0;
    public static final int DPI_SD = 1;
    public static final int DPI_HD = 2;
    public static final int DPI_UHD = 3;
    public static final int DPI_AUDIO = 4;
    private int definition = 0;
    private final Context context;
    private final ContainerLayout frameLayout;
//    private WatchEventCallback watchEventCallback;
    private WatchEventCallback upWatchEventCallback;
    private String mURL = "";
    private PlayView.Param mParam;

    public int getDefinition() {
        return this.definition;
    }

    public void setDefinition(int definition) {
        this.definition = definition;
    }

    public void setScaleType(int scaleType) {
        super.setScaleType(scaleType);
        handleScaleType();
    }

    protected void setWebinarInfo(WebinarInfo webinarInfo) {
        super.setWebinarInfo(webinarInfo);
    }

    public HashMap<String, Integer> getDefinitionAvailable() {
        if (this.webinarInfo == null) {
            return null;
        }
        HashMap<String, Integer> map = new HashMap();
        map.put("SD", Integer.valueOf(this.webinarInfo.SD.valid));
        map.put("HD", Integer.valueOf(this.webinarInfo.HD.valid));
        map.put("UHD", Integer.valueOf(this.webinarInfo.UHD.valid));
        map.put("Audio", Integer.valueOf(this.webinarInfo.A.valid));
        return map;
    }

    public WatchLive(Builder builder) {
        this.context = builder.context;

        this.frameLayout = builder.frameLayout;
        initContainerLayout();


//        this.watchEventCallback = builder.watchEventCallback;
        this.player = builder.playView;
//        this.mParam = player.getParam();
//        if (builder.bufferDelay > 0) {
//            mParam.bufferDelay = builder.bufferDelay;
//        }

        upWatchEventCallback = new WatchEventCallback() {
            @Override
            public void onError(int paramInt, String paramString) {
//                WatchLive.this.watchEventCallback.onError(paramInt, paramString);
            }

            @Override
            public void onStateChanged(int paramInt) {

                switch (paramInt) {
                    case WatchEventCallback.STATE_START:
                        WatchLive.this.videoHeight = player.getHeight();
                        WatchLive.this.videoWidth = player.getWidth();
//                        WatchLive.this.watchEventCallback.onStateChanged(WatchEventCallback.STATE_START);
                        WatchLive.this.setPlaying(true);
                        break;
                    case WatchEventCallback.STATE_STOP:
//                        WatchLive.this.watchEventCallback.onStateChanged(WatchEventCallback.STATE_STOP);
                        WatchLive.this.setPlaying(false);
                        break;
                }
            }

            @Override
            public void uploadSpeed(String paramString) {
//                WatchLive.this.watchEventCallback.uploadSpeed(paramString);
            }
        };

        player.setEventCallback(upWatchEventCallback);
    }

    private void initContainerLayout() {
        this.frameLayout.setReFix(new ReFixListener() {
            public void onReFix() {
                WatchLive.this.handleScaleType();
            }
        });
    }

    public static abstract interface WatchEventCallback {

        public static final int STATE_START = 20001;
        public static final int STATE_STOP = 20002;
        public static final int STATE_COMPLETE = 20003;

        public static final int STATE_ERROR_COMMON = 20201;
        public static final int STATE_ERROR_INIT_VIDEO = 20202;

        public abstract void onError(int paramInt, String paramString);

        public abstract void onStateChanged(int paramInt);

        public abstract void uploadSpeed(String paramString);
    }

    public static class Builder {
        private Context context;
        private ContainerLayout frameLayout;
//        private WatchEventCallback watchEventCallback;
        private PlayView playView;
        //缓冲区大小
        private int bufferDelay;

        public Builder context(Context context) {
            this.context = context;
            return this;
        }

        public Builder containerLayout(ContainerLayout frameLayout) {
            this.frameLayout = frameLayout;
            return this;
        }

//        public Builder callback(WatchEventCallback watchEventCallback) {
//            this.watchEventCallback = watchEventCallback;
//            return this;
//        }

        public Builder playView(PlayView view) {
            this.playView = view;
            return this;
        }

//     public Builder messageCallback(MessageServer.Callback messageCallback)
//     {
//       this.messageCallback = messageCallback;
//       return this;
//     }
//
//     public Builder chatCallback(ChatServer.Callback chatCallback)
//     {
//       this.chatCallback = chatCallback;
//       return this;
//     }

//        public Builder bufferDelay(int bufferDelay) {
//            if (bufferDelay > 0) {
//                this.bufferDelay = bufferDelay;
//            }
//            return this;
//        }

        public WatchLive build() {
            if (this.context == null) {
                return null;
            }
            return new WatchLive(this);
        }
    }


    private void setCurrentVideoDefinition(int definition) {
        switch (definition) {
            case 1:
                if (this.webinarInfo.SD.valid == 1) {
                    this.mURL = (this.webinarInfo.SD.value);
                }
                break;
            case 2:
                if (this.webinarInfo.HD.valid == 1) {
                    this.mURL = (this.webinarInfo.HD.value);
                }
                break;
            case 3:
                if (this.webinarInfo.UHD.valid == 1) {
                    this.mURL = (this.webinarInfo.UHD.value);
                }
                break;
            case 4:
                if (this.webinarInfo.A.valid == 1) {
                    this.mURL = (this.webinarInfo.A.value);
                }
                break;
            case 0:
                this.mURL = this.webinarInfo.rtmp_video;
//                mURL = "rtmp://live.hkstv.hk.lxdns.com/live/hks";
        }

    }


    public void start() {
        if (isPlaying()) {
            return;
        }
        if (!isAvaliable()) {
//            this.watchEventCallback.onError(WatchEventCallback.STATE_ERROR_COMMON, "未初始化视频信息!");
            return;
        }

        this.setCurrentVideoDefinition(this.definition);
//        if(this.definition == 4) {
//            this.player.setVisibility(4);
//        } else {
//            this.player.setVisibility(0);
//        }

        if (player.src() == null) {
            player.src(this.mURL);
        }
        player.playVideo();

        super.start();
    }


    public void destory() {
//     this.messageServer.disconnect();
//     this.chatServer.disconnect();
        player.releaseM(true);

        super.destory();
    }

    public void stop() {

        player.pauseVideo();
        setPlaying(false);
//        this.watchEventCallback.onStateChanged(WatchEventCallback.STATE_STOP);

    }

    public void sendChat(String content, ZeyouSDK.RequestCallback callback) {
//     this.chatServer.sendChat(content, callback);
    }

    public void sendQuestion(String content, String userId, ZeyouSDK.RequestCallback callback) {
//     this.chatServer.sendQuestion(userId, content, callback);
    }


    private void startAudioPlay(int sampleRate, int channelConfig, int audioFormat) {
//     if (this.mAudioPlay == null)
//     {
//       this.mAudioPlay = new AudioPlay();
//       this.mAudioPlay.init(sampleRate, channelConfig, audioFormat);
//     }
    }

    int videoWidth = 0;
    int videoHeight = 0;

    public void initWH(int width, int height) {
        if ((width == 0) || (height == 0)) {
            return;
        }

        this.videoWidth = width;
        this.videoHeight = height;
        handleScaleType();
    }

    private void handleScaleType() {
        int width = 0;
        int height = 0;
        switch (this.scaleType) {
            case 0:
                if ((this.frameLayout != null) && (this.videoWidth > 0) && (this.videoHeight > 0)) {
                    int frameWidth = this.frameLayout.getWidth();
                    int frameHeight = this.frameLayout.getHeight();
                    if ((Math.abs(frameWidth - this.videoWidth) < getDefinition()) && (Math.abs(frameHeight - this.videoHeight) < getDefinition())) {
                        width = frameWidth;
                        height = frameHeight;
                    } else {
                        float framePercent = frameWidth * 1.0F / frameHeight;
                        float videoPercent = this.videoWidth * 1.0F / this.videoHeight;
                        LogManager.innerLog("WatchLive", "framePercent:" + framePercent + "videoPercent:" + videoPercent);
                        if (framePercent < videoPercent) {
                            width = frameWidth;
                            height = width * this.videoHeight / this.videoWidth;
                        } else {
                            height = frameHeight;
                            width = this.videoWidth * height / this.videoHeight;
                        }
                    }
                }
                break;
            case 1:
                if ((this.videoWidth > 0) && (this.videoHeight > 0)) {
                    width = this.videoWidth;
                    height = this.videoHeight;
                }
                break;
            case 2:
                if ((this.frameLayout != null) && (this.videoWidth > 0) && (this.videoHeight > 0)) {
                    width = this.frameLayout.getWidth();
                    height = width * this.videoHeight / this.videoWidth;
                }
                break;
            case 3:
                if ((this.frameLayout != null) && (this.videoHeight > 0) && (this.videoWidth > 0)) {
                    height = this.frameLayout.getHeight();
                    width = this.videoWidth * height / this.videoHeight;
                }
                break;
            case 4:
                if ((this.frameLayout != null) && (this.videoWidth > 0) && (this.videoHeight > 0)) {
                    int frameWidth = this.frameLayout.getWidth();
                    int frameHeight = this.frameLayout.getHeight();
                    if ((Math.abs(frameWidth - this.videoWidth) < getDefinition()) && (Math.abs(frameHeight - this.videoHeight) < getDefinition())) {
                        width = frameWidth;
                        height = frameHeight;
                    } else {
                        float framePercent = frameWidth * 1.0F / frameHeight;
                        float videoPercent = this.videoWidth * 1.0F / this.videoHeight;
                        LogManager.innerLog("WatchLive", "framePercent:" + framePercent + "videoPercent:" + videoPercent);
                        if (framePercent < videoPercent) {
                            height = frameHeight;
                            width = this.videoWidth * height / this.videoHeight;
                        } else {
                            width = frameWidth;
                            height = width * this.videoHeight / this.videoWidth;
                        }
                    }
                }
                break;
        }
        if ((this.player != null) && (width > 0) && (height > 0)) {
            player.handleScale(width, height);
        }
        LogManager.innerLog("WatchLive", "videowidth:" + this.videoWidth + "videoheight:" + this.videoHeight + "fixwidth:" + width + "fixheight:" + height + " frameWidth：" + this.frameLayout

                .getWidth() + " frameHeight:" + this.frameLayout.getHeight());
    }
/*
   public void connectChatServer()
   {
     if (this.chatServer == null) {
       return;
     }
     if (!isAvaliable()) {
       return;
     }
     this.chatServer.connect();
   }

   public void disconnectChatServer()
   {
     if (this.chatServer == null) {
       return;
     }
     if (!isAvaliable()) {
       return;
     }
     this.chatServer.disconnect();
   }

   public void connectMsgServer()
   {
     if (this.messageServer == null) {
       return;
     }
     if (!isAvaliable()) {
       return;
     }
     this.messageServer.connect();
   }

   public void disconnectMsgServer()
   {
     if (this.messageServer == null) {
       return;
     }
     if (!isAvaliable()) {
       return;
     }
     this.messageServer.disconnect();
   }

   public void acquireChatRecord(boolean show_all, ChatServer.ChatRecordCallback chatRecordCallback)
   {
     this.chatServer.acquireChatRecord(show_all, chatRecordCallback);
   }*/
}



