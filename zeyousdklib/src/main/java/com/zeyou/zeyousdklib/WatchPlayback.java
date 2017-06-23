package com.zeyou.zeyousdklib;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;

import android.view.SurfaceView;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.zeyou.zeyousdklib.data.source.UserInfoRepository;
import com.zeyou.zeyousdklib.data.source.WebinarInfoRepository;
import com.zeyou.zeyousdklib.data.source.remote.UserInfoRemoteDataSource;
import com.zeyou.zeyousdklib.data.source.remote.WebinarInfoRemoteDataSource;
import com.zeyou.zeyousdklib.utils.ErrorCode;
import com.zeyou.zeyousdklib.utils.LogManager;
import com.zeyou.zeyousdklib.widget.ContainerLayout;
import com.zeyou.zeyousdklib.widget.ContainerLayout.ReFixListener;
import com.zeyou.zeyousdklib.widget.PlayView;

import java.util.List;

import okhttp3.internal.Util;


public class WatchPlayback
        extends Watch {
    private static final String TAG = "WatchPlayback";
    private ZeyouPPT ZeyouPPT;
    private final Context context;
    private final ContainerLayout frameLayout;
//    private WatchLive.WatchEventCallback watchEventCallback;
//   private zeyouPlayer player;
//   private zeyouPlayerListener mzeyouPlayerListener;

    private WatchLive.WatchEventCallback upWatchEventCallback;

    public void setScaleType(int scaleType) {
        super.setScaleType(scaleType);
        handleScaleType();
    }

    public boolean isPlaying() {

        return player.isPlaying();
    }

    public void setZeyouPPT(ZeyouPPT ZeyouPPT) {
        if (this.ZeyouPPT != null) {
            this.ZeyouPPT.destory();
        }
        this.ZeyouPPT = ZeyouPPT;
        this.ZeyouPPT.setWebinarInfo(this.webinarInfo);
        this.ZeyouPPT.initPPT();
    }

    public WatchPlayback(Builder builder) {
        this.context = builder.context;
        this.frameLayout = builder.frameLayout;
        refreshLayout();
//        this.watchEventCallback = builder.watchEventCallback;

        player = builder.playView;
        upWatchEventCallback = new WatchLive.WatchEventCallback() {
            @Override
            public void onError(int paramInt, String paramString) {
//                watchEventCallback.onError(paramInt, paramString);
            }

            @Override
            public void onStateChanged(int paramInt) {

                switch (paramInt) {
                    case WatchLive.WatchEventCallback.STATE_START:
                        videoHeight = player.getHeight();
                        videoWidth = player.getWidth();
//                        watchEventCallback.onStateChanged(WatchLive.WatchEventCallback.STATE_START);
                        setPlaying(true);
                        break;
                    case WatchLive.WatchEventCallback.STATE_STOP:
//                        watchEventCallback.onStateChanged(WatchLive.WatchEventCallback.STATE_STOP);
                        setPlaying(false);
                        break;
                }
            }

            @Override
            public void uploadSpeed(String paramString) {
//                watchEventCallback.uploadSpeed(paramString);
            }
        };

        player.setEventCallback(upWatchEventCallback);
    }

    private void refreshLayout() {
        this.frameLayout.setReFix(new ReFixListener() {
            public void onReFix() {
                WatchPlayback.this.handleScaleType();
            }
        });
    }

    public static class Builder {
        private Context context;
        private ContainerLayout frameLayout;
        //        private WatchLive.WatchEventCallback watchEventCallback;
        private PlayView playView;

        public Builder context(Context context) {
            this.context = context;
            return this;
        }

        public Builder playView(PlayView view) {
            this.playView = view;
            return this;
        }


        public Builder containerLayout(ContainerLayout frameLayout) {
            this.frameLayout = frameLayout;
            return this;
        }

//        public Builder callback(WatchLive.WatchEventCallback watchEventCallback) {
//            this.watchEventCallback = watchEventCallback;
//            return this;
//        }

        public WatchPlayback build() {
            return new WatchPlayback(this);
        }
    }

    public void start() {
        if (!isAvaliable()) {
//            if (this.watchEventCallback != null) {
//                this.watchEventCallback.onError(WatchLive.WatchEventCallback.STATE_ERROR_INIT_VIDEO, "请先初始化视频信息！");
//            }
            return;
        }


        if (this.player != null) {
            if (player.src() != null) {

                this.player.playVideo();
            } else {

                if (TextUtils.isEmpty(webinarInfo.default_video)) {
                    Toast.makeText(context, "未获取到视频源", Toast.LENGTH_SHORT).show();
                    return;
                }

                this.player.src(webinarInfo.default_video);
                this.player.playVideo();
            }
        }

        super.start();
    }

    public void pause() {
        if (this.player != null) {
            this.player.pauseVideo();
        }
    }

    public void stop() {
        player.stopPlayback();
    }


    public void destory() {
        player.releaseM(true);
    }



   /*
   private zeyouPlayer.RendererBuilder getRendererBuilder()
   {
     String userAgent = Util.getUserAgent(this.context, "ExoPlayerDemo");
     int contentType = -1;
     if (this.webinarInfo.video.endsWith("m3u8")) {
       contentType = 2;
     } else if (this.webinarInfo.video.endsWith("mp4")) {
       contentType = 3;
     } else {
       contentType = 1;
     }
     Uri contentUri = Uri.parse(this.webinarInfo.video);
     switch (contentType)
     {
     case 1:
       return new SmoothStreamingRendererBuilder(this.context, userAgent, contentUri.toString(), new SmoothStreamingTestMediaDrmCallback());
     case 2:
       return new HlsRendererBuilder(this.context, userAgent, contentUri.toString());
     case 3:
       return new ExtractorRendererBuilder(this.context, userAgent, contentUri);
     }
     throw new IllegalStateException("Unsupported type: " + contentType);
   }*/

    private void releasePlayer() {
        if (this.player != null) {
            this.player.releaseM(true);
            this.player = null;
        }
    }

    private boolean enableBackgroundAudio = false;

    private void onHidden() {
        if (!this.enableBackgroundAudio) {
            releasePlayer();
        } else {
//       this.player.setBackgrounded(true);
        }
    }


    public int getCurrentPosition() {
        if (this.player != null) {
            return this.player.getCurrentPosition();
        }
        return 0;
    }

    public long getDuration() {
        if (this.player != null) {
            return this.player.getDuration();
        }
        return 0L;
    }

    public void seekTo(int position) {
        if (this.player != null) {
            this.player.seekTo(position);
        }
    }

/*
   private class zeyouPlayerListener
     implements zeyouPlayer.Listener
   {
     private zeyouPlayerListener() {}

     public void onStateChanged(boolean playWhenReady, int playbackState)
     {
       if (WatchPlayback.this.watchEventCallback != null) {
         WatchPlayback.this.watchEventCallback.onStateChanged(playWhenReady, playbackState);
       }
     }

     public void onError(Exception e)
     {
       if (WatchPlayback.this.watchEventCallback != null) {
         WatchPlayback.this.watchEventCallback.onError(e);
       }
     }

     public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio)
     {
       if (WatchPlayback.this.watchEventCallback != null) {
         WatchPlayback.this.watchEventCallback.onVideoSizeChanged(width, height);
       }
       if ((width == 0) || (height == 0)) {
         return;
       }
       WatchPlayback.this.videoWidth = width;
       WatchPlayback.this.videoHeight = height;
       WatchPlayback.this.handleScaleType();
     }
   }
*/


    int videoWidth = 0;
    int videoHeight = 0;

    private void handleScaleType() {
        if ((this.videoWidth == 0) || (this.videoHeight == 0)) {
            return;
        }
        int width = 0;
        int height = 0;
        switch (this.scaleType) {
            case 0:
                if ((this.frameLayout != null) && (this.videoWidth > 0) && (this.videoHeight > 0)) {
                    int frameWidth = this.frameLayout.getWidth();
                    int frameHeight = this.frameLayout.getHeight();
                    float framePercent = frameWidth * 1.0F / frameHeight;
                    float videoPercent = this.videoWidth * 1.0F / this.videoHeight;
                    if (framePercent < videoPercent) {
                        width = frameWidth;
                        height = width * this.videoHeight / this.videoWidth;
                    } else {
                        height = frameHeight;
                        width = this.videoWidth * height / this.videoHeight;
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
                if (this.frameLayout != null) {
                    width = this.frameLayout.getWidth();
                    height = this.frameLayout.getHeight();
                }
                break;
        }
        if ((this.player != null) && (width > 0) && (height > 0)) {
            player.handleScale(width, height);
        }
        LogManager.innerLog("WatchPlayback", "videowidth:" + this.videoWidth + "videoheight:" + this.videoHeight + "fixwidth:" + width + "fixheight:" + height + " frameWidth：" + this.frameLayout

                .getWidth() + " frameHeight:" + this.frameLayout.getHeight());
    }

    public void sendComment(String content, String user_id, ZeyouSDK.RequestCallback callback) {
        if ((!isAvaliable()) || (TextUtils.isEmpty(this.webinarInfo.webinar_id))) {
            ZeyouSDK.ErrorCallback(callback, 260, ErrorCode.getErrorStr(260));
            return;
        }
        if (TextUtils.isEmpty(content)) {
            ZeyouSDK.ErrorCallback(callback, 263, ErrorCode.getErrorStr(263));
            return;
        }
        if (content.length() > 200) {
            ZeyouSDK.ErrorCallback(callback, 20001, "长度不超过200个字符");
            return;
        }
        UserInfoRepository userRepository = UserInfoRepository.getInstance(UserInfoRemoteDataSource.getInstance());
        userRepository.sendComment(this.webinarInfo.webinar_id, content, user_id, callback);
    }
/*
   public void requestCommentHistory(String webinar_id, int limit, int pos, final ChatServer.ChatRecordCallback callback)
   {
     if (checkCommentParameter(limit, pos, callback))
     {
       WebinarInfoRepository repository = WebinarInfoRepository.getInstance(WebinarInfoRemoteDataSource.getInstance());
       repository.getCommentHistory(this.webinarInfo.join_id, webinar_id, String.valueOf(limit), String.valueOf(pos), new ChatServer.ChatRecordCallback()
       {
         public void onDataLoaded(List<ChatServer.ChatInfo> list)
         {
           callback.onDataLoaded(list);
         }

         public void onFailed(int errorcode, String messaage)
         {
           callback.onFailed(errorcode, messaage);
         }
       });
     }
   }

   private boolean checkCommentParameter(int limit, int pos, ChatServer.ChatRecordCallback callback)
   {
     if (callback == null) {
       throw new IllegalArgumentException("call should not be null!");
     }
     if ((!isAvaliable()) || (TextUtils.isEmpty(this.webinarInfo.join_id)))
     {
       callback.onFailed(260, ErrorCode.getErrorStr(260));
       return false;
     }
     if (this.webinarInfo.status == 1)
     {
       callback.onFailed(262, ErrorCode.getErrorStr(262));
       return false;
     }
     if ((limit <= 0) || (pos < 0))
     {
       callback.onFailed(261, ErrorCode.getErrorStr(261));
       return false;
     }
     return true;
   }*/

//    public static abstract interface WatchEventCallback {
//        public abstract void onStartFailed(String paramString);
//
//        public abstract void onStateChanged(boolean paramBoolean, int paramInt);
//
//        public abstract void onError(Exception paramException);
//
//        public abstract void onVideoSizeChanged(int paramInt1, int paramInt2);
//    }
}



