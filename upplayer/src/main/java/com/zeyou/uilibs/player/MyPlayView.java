package com.zeyou.uilibs.player;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.zeyou.media.IjkVideoView;
import com.zeyou.upplayer.R;
import com.zeyou.zeyousdklib.WatchLive;
import com.zeyou.zeyousdklib.widget.PlayView;

import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * Description 播放view
 */
public class MyPlayView extends IjkVideoView implements PlayView {

    public MyPlayView(Context context) {
        super(context);
    }

    public MyPlayView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyPlayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MyPlayView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void handleScale(int width, int height) {
        if (width > 0 && height > 0) {
            mVideoWidth = width;
            mVideoHeight = height;
            mRenderView.setVideoSize(mVideoWidth, mVideoHeight);
        }
    }

    @Override
    public void setEventCallback(WatchLive.WatchEventCallback callback) {
//控制逻辑使用IMediaController,不在依赖事件回调
    }

    @Override
    public void playVideo() {
        start();
    }

    @Override
    public void pauseVideo() {
        pause();
    }

    @Override
    public void resumeVideo() {
        resume();
    }

    @Override
    public void releaseM(boolean cleartargetstate) {
        release(cleartargetstate);
    }

    @Override
    public void src(String url) {

        setVideoPath(url);
    }

    @Override
    public String src() {
        return mMediaPlayer != null ? mMediaPlayer.getDataSource() : null;
    }
}
