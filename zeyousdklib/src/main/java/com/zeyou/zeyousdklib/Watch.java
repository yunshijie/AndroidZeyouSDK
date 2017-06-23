package com.zeyou.zeyousdklib;

import com.zeyou.zeyousdklib.data.WebinarInfo;
import com.zeyou.zeyousdklib.widget.PlayView;


public abstract class Watch {
    public static final int STATE_CONNECTED = 20251;
    public static final int STATE_BUFFER_START = 20254;
    public static final int STATE_BUFFER_STOP = 20255;
    public static final int STATE_STOP = 20256;
    public static final int ERROR_NOT_INIT = 20202;
    public static final int ERROR_PARAM = 20203;
    public static final int ERROR_URL = 20204;
    public static final int ERROR_SWITCHDPI = 20205;
    public static final int ERROR_CONNECT = 20208;
    public static final int ERROR_STATE = 20209;
    public static final int FIT_DEFAULT = 0;
    public static final int FIT_CENTER_INSIDE = 1;
    public static final int FIT_X = 2;
    public static final int FIT_Y = 3;
    public static final int FIT_XY = 4;
    protected int scaleType = 0;
    protected int deviation = 10;
    private boolean playing = false;
    protected WebinarInfo webinarInfo;
    protected PlayView player;

    public boolean isPlaying() {
        return this.playing;
    }

    public void setPlaying(boolean playing) {
        this.playing = playing;
    }

    protected void setWebinarInfo(WebinarInfo webinarInfo) {
        this.webinarInfo = webinarInfo;
    }

    public WebinarInfo getWebinarInfo() {
        return webinarInfo;
    }

    public PlayView getPlayView() {
        return player;
    }


    public int getDeviation() {
        return this.deviation;
    }

    public void setDeviation(int deviation) {
        this.deviation = deviation;
    }

    public int getScaleType() {
        return this.scaleType;
    }

    public int getWatchLayout() {
        if (isAvaliable()) {
            return this.webinarInfo.layout;
        }
        return -1;
    }

    public String getNotice() {
        if (isAvaliable()) {
            return this.webinarInfo.notice.content;
        }
        return null;
    }

    public void setScaleType(int scaleType) {
        this.scaleType = scaleType;
    }

    public boolean isAvaliable() {
        if (this.webinarInfo != null) {
            return true;
        }
        return false;
    }

    private Callback callback;

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public static interface Callback {

        public void onStart();

        public void onDestory();
    }

    //////////////////////////子类必须实现的方法/////////////////////////////////////
    public void start() {

        if (callback != null) {
            callback.onStart();
        }
    }

    public abstract void stop();

    public void destory() {
        if (callback != null) {
            callback.onDestory();
        }
    }
}



