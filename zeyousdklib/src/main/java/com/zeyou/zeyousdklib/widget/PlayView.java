package com.zeyou.zeyousdklib.widget;

import android.app.Activity;

import com.zeyou.zeyousdklib.WatchLive;

/**
 * Created by Administrator on 2017/5/26.
 */

public interface PlayView {

    public abstract void handleScale(int width, int height);
//    public abstract Param getParam();

    public abstract void setEventCallback(WatchLive.WatchEventCallback callback);

    public abstract void playVideo();

    public abstract void pauseVideo();

    public abstract void resumeVideo();


    public abstract boolean isPlaying();

    public abstract void releaseM(boolean cleartargetstate);

    public abstract void src(String url);

    public abstract String src();

    public abstract void stopPlayback();

    public abstract int getWidth();

    public abstract int getHeight();

    public abstract int getCurrentPosition();

    public abstract int getDuration();

    public abstract void seekTo(int position);


    public class Param {
        public int bufferDelay;
    }

}
