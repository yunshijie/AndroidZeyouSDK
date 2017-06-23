package com.zeyou.uilibs.watch;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.zeyou.media.IMediaController;
import com.zeyou.media.demo.VSeekBar;
import com.zeyou.uilibs.player.MyPlayView;
import com.zeyou.uilibs.util.IFragmentBackHandler;
import com.zeyou.uilibs.util.ZeyouUtil;
import com.zeyou.upplayer.R;
import com.zeyou.zeyousdklib.widget.ContainerLayout;
import com.zeyou.zeyousdklib.widget.PlayView;

import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * 观看回放的Fragment
 */
public class WatchPlaybackFragment extends Fragment implements WatchContract.PlaybackView, View.OnClickListener, IMediaController, IFragmentBackHandler {

    WatchContract.PlaybackPresenter mPresenter;
    ContainerLayout rl_video_container;//视频区容器
    ImageView iv_play, btn_changescaletype;
    SeekBar seekbar;
    TextView tv_current_time, tv_end_time;
    ProgressBar progressBar;
    MyPlayView videoView;

    public static boolean isShowToast = true;

    public static WatchPlaybackFragment newInstance() {
        WatchPlaybackFragment articleFragment = new WatchPlaybackFragment();
        return articleFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.watch_playback_fragment, null);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        rl_video_container = (ContainerLayout) getView().findViewById(R.id.rl_video_container);
        btn_changescaletype = (ImageView) getView().findViewById(R.id.btn_change_scale_type);
        btn_changescaletype.setOnClickListener(this);
        progressBar = (ProgressBar) getView().findViewById(R.id.pb);
        iv_play = (ImageView) getView().findViewById(R.id.iv_play);
        iv_play.setOnClickListener(this);
        getView().findViewById(R.id.iv_fullscreen).setOnClickListener(this);
        View root = getView();

        videoView = (MyPlayView) root.findViewById(R.id.uvv_vido);
        initVideoView(root);

        seekbar = (SeekBar) root.findViewById(R.id.seekbar);
        tv_current_time = (TextView) getView().findViewById(R.id.tv_current_time);
        tv_end_time = (TextView) getView().findViewById(R.id.tv_end_time);
        getView().findViewById(R.id.image_action_back).setOnClickListener(this);
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mPresenter.onProgressChanged(seekBar, progress, fromUser);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                show();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mPresenter.onStopTrackingTouch(seekBar);
                show();
            }
        });
        mPresenter.start();
    }

    @Override
    public void setPresenter(WatchContract.PlaybackPresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public Activity getmActivity() {
        return getActivity();
    }

    @Override
    public void setPlayIcon(boolean isStop) {
        if (isStop) {
            iv_play.setImageResource(R.drawable.zeyou_icon_live_play);
        } else {
            iv_play.setImageResource(R.drawable.zeyou_icon_live_pause);
        }
    }

    @Override
    public void setProgressLabel(String currentTime, String max) {
        tv_current_time.setText(currentTime);
        if (!max.equals(tv_end_time.getText().toString())) {
            tv_end_time.setText(max);
            setSeekbarMax(videoView.getDuration());
        }
    }

    @Override
    public void setSeekbarMax(int max) {
        seekbar.setMax(max);
    }

    @Override
    public void setSeekbarCurrentPosition(int position) {
        seekbar.setProgress(position);
    }

    @Override
    public void showProgressbar(boolean show) {
        if (show)
            progressBar.setVisibility(View.VISIBLE);
        else
            progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showToast(String message) {
        if (isShowToast && isAdded())
            Toast.makeText(this.getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public ContainerLayout getContainer() {
        return rl_video_container;
    }

    @Override
    public void setScaleTypeText(int text) {
        switch (text) {
//            case WatchLive.FIT_DEFAULT:
//                btn_changescaletype.setBackground(getResources().getDrawable(R.drawable.fit_default));
//                break;
//            case WatchLive.FIT_CENTER_INSIDE:
//                btn_changescaletype.setBackground(getResources().getDrawable(R.drawable.fit_center));
//                break;
//            case WatchLive.FIT_X:
//                btn_changescaletype.setBackground(getResources().getDrawable(R.drawable.fit_x));
//                break;
//            case WatchLive.FIT_Y:
//                btn_changescaletype.setBackground(getResources().getDrawable(R.drawable.fit_y));
//                break;
//            case WatchLive.FIT_XY:
//                btn_changescaletype.setBackground(getResources().getDrawable(R.drawable.fit_xy));
//                break;
        }
    }

    @Override
    public int changeScreenOri() {
        if (getActivity().getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        return getActivity().getRequestedOrientation();
    }

    @Override
    public void onStop() {
        super.onStop();
        mPresenter.onFragmentStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.onFragmentDestory();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.image_action_back) {
            if (getActivity().getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                mPresenter.changeScreenOri();
            } else {

                getActivity().finish();
            }

        } else if (i == R.id.iv_play) {

            if (!TextUtils.isEmpty(videoView.src())&& !videoView.isPlaying()) {
                start();
            }
            mPresenter.onPlayClick();
        } else if (i == R.id.iv_fullscreen) {
            mPresenter.changeScreenOri();
        } else if (i == R.id.btn_change_scale_type) {
            mPresenter.changeScaleType();
        }
    }

    @Override
    public PlayView getPlayView() {
        return videoView;
    }


    //////////////////////////////////////IMediaController//////////////////////////////////////////////////////
    private static final int SET_VIEW_HIDE = 1;
    private static final int TIME_OUT = 5000;
    private static final int MESSAGE_SHOW_PROGRESS = 2;
    private static final int PAUSE_IMAGE_HIDE = 3;
    private static final int MESSAGE_SEEK_NEW_POSITION = 4;
    private static final int MESSAGE_HIDE_CONTOLL = 5;

    private boolean isShow;
    private boolean isDragging;
    private View itemView;
    private long newPosition = 1;
    AudioManager audioManager;
    private TextView seekTxt;
    private GestureDetector detector;
    private VSeekBar brightness_seek, sound_seek;
    private LinearLayout brightness_layout, sound_layout;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SET_VIEW_HIDE:
                    isShow = false;
                    itemView.setVisibility(View.GONE);
                    break;
                case MESSAGE_SHOW_PROGRESS:
//                    setProgress();
//                    if (!isDragging && isShow) {
//                        msg = obtainMessage(MESSAGE_SHOW_PROGRESS);
//                        sendMessageDelayed(msg, 1000);
//                    }
                    break;
                case PAUSE_IMAGE_HIDE:
//                    pauseImage.setVisibility(View.GONE);
                    break;
                case MESSAGE_SEEK_NEW_POSITION:
                    if (newPosition >= 0) {
                        videoView.seekTo((int) newPosition);
                        newPosition = -1;
                    }
                    break;
                case MESSAGE_HIDE_CONTOLL:
                    seekTxt.setVisibility(View.GONE);
                    brightness_layout.setVisibility(View.GONE);
                    sound_layout.setVisibility(View.GONE);
                    break;
            }
        }
    };

    private void initVideoView(View root) {

        isShow = true;
        isDragging = false;
        audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);

        itemView = root.findViewById(R.id.rl_actions_bottom);
        brightness_layout = (LinearLayout) root.findViewById(R.id.brightness_layout);
        brightness_seek = (VSeekBar) root.findViewById(R.id.brightness_seek);
        sound_layout = (LinearLayout) root.findViewById(R.id.sound_layout);
        sound_seek = (VSeekBar) root.findViewById(R.id.sound_seek);
        seekTxt = (TextView) root.findViewById(R.id.seekTxt);

        videoView.setMediaController(this);

        sound_seek.setEnabled(false);
        brightness_seek.setEnabled(false);
        detector = new GestureDetector(getActivity(), new PlayGestureListener());
        mMaxVolume = ((AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE))
                .getStreamMaxVolume(AudioManager.STREAM_MUSIC);

        rl_video_container.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (detector.onTouchEvent(event))
                    return true;

                // 处理手势结束
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_UP:
                        endGesture();
                        break;
                }
                return false;
            }
        });

        itemView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.e("custommedia", "event");

                Rect seekRect = new Rect();
                seekbar.getHitRect(seekRect);

                if ((event.getY() >= (seekRect.top - 50)) && (event.getY() <= (seekRect.bottom + 50))) {

                    float y = seekRect.top + seekRect.height() / 2;
                    //seekBar only accept relative x
                    float x = event.getX() - seekRect.left;
                    if (x < 0) {
                        x = 0;
                    } else if (x > seekRect.width()) {
                        x = seekRect.width();
                    }
                    MotionEvent me = MotionEvent.obtain(event.getDownTime(), event.getEventTime(),
                            event.getAction(), x, y, event.getMetaState());
                    return seekbar.onTouchEvent(me);

                }
                return false;
            }
        });

        videoView.setOnInfoListener(new IMediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(IMediaPlayer mp, int what, int extra) {

                Log.e("setOnInfoListener", what + "");
                switch (what) {
                    case IMediaPlayer.MEDIA_INFO_BUFFERING_START:
                        //开始缓冲
                        if (progressBar.getVisibility() == View.GONE)
                            progressBar.setVisibility(View.VISIBLE);
                        break;
                    case IMediaPlayer.MEDIA_INFO_BUFFERING_END:
                        //开始播放
                        progressBar.setVisibility(View.GONE);
                        break;

                    case IMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
//                        statusChange(STATUS_PLAYING);
                        progressBar.setVisibility(View.GONE);
                        break;

                    case IMediaPlayer.MEDIA_INFO_AUDIO_RENDERING_START:
                        progressBar.setVisibility(View.GONE);
                        break;
                }
                return false;
            }
        });

        videoView.setOnCompletionListener(new IMediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(IMediaPlayer mp) {

            }
        });
        videoView.setOnErrorListener(new IMediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(IMediaPlayer mp, int what, int extra) {

                Toast.makeText(getActivity(), "播放失败", Toast.LENGTH_SHORT).show();
                setPlayIcon(true);

                showProgressbar(false);
                videoView.stopPlayback();

                return false;
            }
        });
    }

    private void start() {

        if (videoView.getCurrentPosition() <= 0) {
            setPlayIcon(false);
            itemView.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    private void pause() {
        setPlayIcon(true);
        videoView.pause();
    }

    private void reStart() {
        setPlayIcon(false);
        videoView.start();
    }

    public int getScreenOrientation(Activity activity) {
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        int orientation;
        // if the device's natural orientation is portrait:
        if ((rotation == Surface.ROTATION_0
                || rotation == Surface.ROTATION_180) && height > width ||
                (rotation == Surface.ROTATION_90
                        || rotation == Surface.ROTATION_270) && width > height) {
            switch (rotation) {
                case Surface.ROTATION_0:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                    break;
                case Surface.ROTATION_90:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                    break;
                case Surface.ROTATION_180:
                    orientation =
                            ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
                    break;
                case Surface.ROTATION_270:
                    orientation =
                            ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
                    break;
                default:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                    break;
            }
        }
        // if the device's natural orientation is landscape or if the device
        // is square:
        else {
            switch (rotation) {
                case Surface.ROTATION_0:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                    break;
                case Surface.ROTATION_90:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                    break;
                case Surface.ROTATION_180:
                    orientation =
                            ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
                    break;
                case Surface.ROTATION_270:
                    orientation =
                            ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
                    break;
                default:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                    break;
            }
        }

        return orientation;
    }

    @Override
    public void hide() {
        if (isShow) {
            handler.removeMessages(MESSAGE_SHOW_PROGRESS);
            isShow = false;
            handler.removeMessages(SET_VIEW_HIDE);
            itemView.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean isShowing() {
        return isShow;
    }

    @Override
    public void setAnchorView(View view) {

    }

    @Override
    public void setEnabled(boolean enabled) {

    }

    @Override
    public void setMediaPlayer(MediaController.MediaPlayerControl player) {

    }

    @Override
    public void show(int timeout) {
        handler.sendEmptyMessageDelayed(SET_VIEW_HIDE, timeout);
    }

    @Override
    public void show() {
        isShow = true;
        progressBar.setVisibility(View.GONE);
        itemView.setVisibility(View.VISIBLE);
        handler.sendEmptyMessage(MESSAGE_SHOW_PROGRESS);
        show(TIME_OUT);
    }

    @Override
    public void showOnce(View view) {

    }


    public class PlayGestureListener extends GestureDetector.SimpleOnGestureListener {

        private boolean firstTouch;
        private boolean volumeControl;
        private boolean seek;

        @Override
        public boolean onDown(MotionEvent e) {
            firstTouch = true;
            handler.removeMessages(SET_VIEW_HIDE);
            //横屏下拦截事件
            if (getScreenOrientation((Activity) getActivity()) == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                return true;
            } else {
                return super.onDown(e);
            }
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            float x = e1.getX() - e2.getX();
            float y = e1.getY() - e2.getY();
            if (firstTouch) {
                seek = Math.abs(distanceX) >= Math.abs(distanceY);
                volumeControl = e1.getX() < rl_video_container.getMeasuredWidth() * 0.5;
                firstTouch = false;
            }

            if (seek) {
                onProgressSlide(-x / rl_video_container.getWidth(), e1.getX() / rl_video_container.getWidth());
            } else {
                float percent = y / rl_video_container.getHeight();
                if (volumeControl) {
                    onVolumeSlide(percent);
                } else {
                    onBrightnessSlide(percent);
                }
            }
            return super.onScroll(e1, e2, distanceX, distanceY);
        }
    }

    private int volume = -1;
    private float brightness = -1;
    private int mMaxVolume;

    /**
     * 手势结束
     */
    private void endGesture() {
        volume = -1;
        brightness = -1f;
        if (newPosition >= 0) {
            handler.removeMessages(MESSAGE_SEEK_NEW_POSITION);
            handler.sendEmptyMessage(MESSAGE_SEEK_NEW_POSITION);
        }
        handler.removeMessages(MESSAGE_HIDE_CONTOLL);
        handler.sendEmptyMessageDelayed(MESSAGE_HIDE_CONTOLL, 500);

    }

    /**
     * 滑动改变声音大小
     *
     * @param percent
     */
    private void onVolumeSlide(float percent) {
        if (volume == -1) {
            volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            if (volume < 0)
                volume = 0;
        }
//        hide();

        int index = (int) (percent * mMaxVolume) + volume;
        if (index > mMaxVolume)
            index = mMaxVolume;
        else if (index < 0)
            index = 0;

        // 变更声音
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, index, 0);

        if (sound_layout.getVisibility() == View.GONE)
            sound_layout.setVisibility(View.VISIBLE);
        // 变更进度条
        int i = (int) (index * 1.0f / mMaxVolume * 100);
//        if (i == 0) {
//            sound.setImageResource(R.mipmap.sound_mult_icon);
//        } else {
//            sound.setImageResource(R.mipmap.sound_open_icon);
//        }
        sound_seek.setProgress(i);
    }

    /**
     * 滑动跳转
     *
     * @param percent 移动比例
     * @param downPer 按下比例
     */
    private void onProgressSlide(float percent, float downPer) {
        long position = videoView.getCurrentPosition();
        long duration = videoView.getDuration();
        long deltaMax = Math.min(100 * 1000, duration - position);
        long delta = (long) (deltaMax * percent);

        newPosition = delta + position;
        if (newPosition > duration) {
            newPosition = duration;
        } else if (newPosition <= 0) {
            newPosition = 0;
            delta = -position;
        }
        int showDelta = (int) delta / 1000;
        Log.e("showdelta", ((downPer + percent) * 100) + "");
        if (showDelta != 0) {
            if (seekTxt.getVisibility() == View.GONE)
                seekTxt.setVisibility(View.VISIBLE);
            String current = ZeyouUtil.converLongTimeToStr(newPosition);
            seekTxt.setText(current + "/" + tv_end_time.getText());
        }
    }

    /**
     * 滑动改变亮度
     *
     * @param percent
     */
    private void onBrightnessSlide(float percent) {
        if (brightness < 0) {
            brightness = ((Activity) getActivity()).getWindow().getAttributes().screenBrightness;
            if (brightness <= 0.00f) {
                brightness = 0.50f;
            } else if (brightness < 0.01f) {
                brightness = 0.01f;
            }
        }
        Log.d(this.getClass().getSimpleName(), "brightness:" + brightness + ",percent:" + percent);
        WindowManager.LayoutParams lpa = ((Activity) getActivity()).getWindow().getAttributes();
        lpa.screenBrightness = brightness + percent;
        if (lpa.screenBrightness > 1.0f) {
            lpa.screenBrightness = 1.0f;
        } else if (lpa.screenBrightness < 0.01f) {
            lpa.screenBrightness = 0.01f;
        }

        if (brightness_layout.getVisibility() == View.GONE)
            brightness_layout.setVisibility(View.VISIBLE);

        brightness_seek.setProgress((int) (lpa.screenBrightness * 100));
        ((Activity) getActivity()).getWindow().setAttributes(lpa);

    }

    @Override
    public boolean onBackPressed() {

        if (getActivity().getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            mPresenter.changeScreenOri();
        } else {
            getActivity().finish();
        }

        return true;
    }

    private Dialog dialogProcKnock;
    private Dialog dialogKnock;
    private Dialog dialogPwd;

    @Override
    public void showProcessingKnock() {
        if (dialogProcKnock == null) {
            dialogProcKnock = new AlertDialog.Builder(this.getActivity())
                    .setTitle("敲门")
                    .setMessage("申请已提交，等待主播开门~~~~~")
                    .create();
        }

        if (!dialogProcKnock.isShowing()) {
            dialogProcKnock.show();
        }
    }

    @Override
    public void hideProcessingKnock() {
        if (dialogProcKnock!=null && dialogProcKnock.isShowing()) {
            dialogProcKnock.hide();
        }
    }

    @Override
    public void showKnock() {

        if(dialogKnock==null ) {

            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View submitView = layoutInflater.inflate(R.layout.submit_knock, null);
            final EditText submitNickname = (EditText) submitView.findViewById(R.id.edit_msg);

            dialogKnock = new AlertDialog.Builder(this.getActivity())
                    .setTitle("敲门")
                    .setView(submitView)
                    .setPositiveButton("提交", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            final String submitNicknameStr = submitNickname.getText().toString();
                            if (!TextUtils.isEmpty(submitNicknameStr) ) {
                                mPresenter.knockDoor(submitNicknameStr);
                            } else {
                                showToast("请填写信息");
                                return;
                            }
                        }
                    }).create();
        }

        if (!dialogKnock.isShowing()) {
            dialogKnock.show();
        }
    }

    @Override
    public void hideKnock() {
        if (dialogKnock!=null && dialogKnock.isShowing()) {
            dialogKnock.hide();
        }
    }

    @Override
    public void showPwd() {
        if(dialogPwd==null ) {

            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View submitView = layoutInflater.inflate(R.layout.submit_pwd, null);
            final EditText submitNickname = (EditText) submitView.findViewById(R.id.edit_pwd);

            dialogPwd = new AlertDialog.Builder(this.getActivity())
                    .setTitle("密码")
                    .setView(submitView)
                    .setPositiveButton("提交", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            final String submitNicknameStr = submitNickname.getText().toString();
                            if (!TextUtils.isEmpty(submitNicknameStr) ) {
                                mPresenter.checkPwd(submitNicknameStr);
                            } else {
                                showToast("请填写信息");
                               return;
                            }
                        }
                    }).create();
        }

        if (!dialogPwd.isShowing()) {
            dialogPwd.show();
        }
    }

    @Override
    public void hidePwd() {
        if (dialogPwd!=null && dialogPwd.isShowing()) {
            dialogPwd.hide();
        }
    }
}
