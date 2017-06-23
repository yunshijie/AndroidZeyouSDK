package com.zeyou.uilibs.watch;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;
import android.text.style.ImageSpan;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.zeyou.media.IMediaController;
import com.zeyou.media.demo.VSeekBar;
import com.zeyou.uilibs.player.MyPlayView;
import com.zeyou.uilibs.util.IFragmentBackHandler;
import com.zeyou.uilibs.util.ZeyouUtil;
import com.zeyou.upplayer.R;
import com.zeyou.zeyousdklib.WatchLive;
import com.zeyou.zeyousdklib.widget.ContainerLayout;
import com.zeyou.zeyousdklib.widget.PlayView;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * 观看直播的Fragment
 */
public class WatchLiveFragment extends Fragment implements WatchContract.LiveView, View.OnClickListener, IMediaController ,IFragmentBackHandler{

    private WatchContract.LivePresenter mPresenter;

    private ImageView clickStart;
    private ImageView clickOrientation;
    private RadioButton radioButtonShowSD;
    private RadioButton radioButtonShowHD;
    private RadioButton radioButtonShowUHD;
    private RadioGroup radioChoose;
    private TextView fragmentDownloadSpeed;
    private ProgressDialog mProcessDialog;
    private ContainerLayout mContainerLayout;
    private ImageView btn_change_scaletype;
    private ImageView btnChangePlayStatus;
    ImageView btn_danmaku;
    private Dialog alertDialog;
    private String lotteryStr;
    private MyPlayView videoView;

    private Context context;
    /***
     * 控制显示Toast
     */
    public static boolean isShowToast = true;

    public static WatchLiveFragment newInstance() {
        return new WatchLiveFragment();
    }

    @Override
    public void setPresenter(WatchContract.LivePresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.context = activity;
        /** 初始化Dialog*/
        mProcessDialog = new ProgressDialog(activity);
        mProcessDialog.setCancelable(true);
        mProcessDialog.setCanceledOnTouchOutside(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.watch_live_fragment, container, false);
        initView(root);
        initStatue();
        return root;
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().setResult(Activity.RESULT_OK);
        mPresenter.stopWatch();
    }

    private void initView(View root) {
        clickStart = (ImageView) root.findViewById(R.id.click_rtmp_watch);
        clickStart.setOnClickListener(this);
        clickOrientation = (ImageView) root.findViewById(R.id.click_rtmp_orientation);
        clickOrientation.setOnClickListener(this);
        radioChoose = (RadioGroup) root.findViewById(R.id.radio_choose);
        radioChoose.setOnCheckedChangeListener(checkListener);
        radioButtonShowSD = (RadioButton) root.findViewById(R.id.radio_btn_sd);
        radioButtonShowHD = (RadioButton) root.findViewById(R.id.radio_btn_hd);
        radioButtonShowUHD = (RadioButton) root.findViewById(R.id.radio_btn_uhd);
        mContainerLayout = (ContainerLayout) root.findViewById(R.id.rl_container);
        fragmentDownloadSpeed = (TextView) root.findViewById(R.id.fragment_download_speed);

        videoView = (MyPlayView) root.findViewById(R.id.uvv_vido);
        initVideoView(root);

        btn_danmaku = (ImageView) root.findViewById(R.id.btn_danmaku);
        btn_danmaku.setImageResource(R.drawable.zeyou_icon_danmaku_close);
        btn_danmaku.setOnClickListener(this);
        btnChangePlayStatus = (ImageView) root.findViewById(R.id.btn_change_audio);
        btnChangePlayStatus.setOnClickListener(this);
        btn_change_scaletype = (ImageView) root.findViewById(R.id.btn_change_scaletype);
        btn_change_scaletype.setOnClickListener(this);
        root.findViewById(R.id.image_action_back).setOnClickListener(this);


        if (mPresenter != null) {
            mPresenter.start();
        }

    }

    private void initStatue() {
        if (mPresenter != null) {
            if (mPresenter.getCurrentPixel() == WatchLive.DPI_DEFAULT) {
                btnChangePlayStatus.setBackground(getResources().getDrawable(R.drawable.audio_close));
            } else if (mPresenter.getCurrentPixel() == WatchLive.DPI_AUDIO) {
                btnChangePlayStatus.setBackground(getResources().getDrawable(R.drawable.audio_open));
            }
            setScaleButtonText(mPresenter.getScaleType());
        }
    }

    @Override
    public Activity getmActivity() {
        return this.getActivity();
    }

    @Override
    public ContainerLayout getWatchLayout() {
        return mContainerLayout;
    }

    @Override
    public void setPlayPicture(boolean state) {
        if (state) {
            clickStart.setBackgroundResource(R.drawable.zeyou_icon_live_play);
        } else {
            clickStart.setBackgroundResource(R.drawable.zeyou_icon_live_pause);
        }
    }

    @Override
    public void setDownSpeed(String text) {
        fragmentDownloadSpeed.setText(text);
    }

    @Override
    public void showLoading(boolean isShow) {
        if (mProcessDialog != null) {
            mProcessDialog.dismiss();
            if (isShow) {
                mProcessDialog.show();
            }
        }
    }


    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.click_rtmp_watch) {
            if (!TextUtils.isEmpty(videoView.src())&& !videoView.isPlaying()) {
                start();
            }
            mPresenter.onWatchBtnClick();
        } else if (i == R.id.click_rtmp_orientation) {
            mPresenter.changeOriention();
        } else if (i == R.id.btn_change_scaletype) {
            mPresenter.setScaleType();
        } else if (i == R.id.image_action_back) {

            if (getActivity().getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                mPresenter.changeOriention();
            } else {
                getActivity().finish();
            }
        } else if (i == R.id.btn_change_audio) {
            if (mPresenter.getCurrentPixel() == WatchLive.DPI_DEFAULT) {
                mPresenter.onSwitchPixel(WatchLive.DPI_AUDIO);
                btnChangePlayStatus.setBackground(getResources().getDrawable(R.drawable.audio_open));
            } else if (mPresenter.getCurrentPixel() == WatchLive.DPI_AUDIO) {
                mPresenter.onSwitchPixel(WatchLive.DPI_DEFAULT);
                btnChangePlayStatus.setBackground(getResources().getDrawable(R.drawable.audio_close));
            }
        } else if (i == R.id.btn_danmaku) {


        }
    }

    @Override
    public void showToast(String message) {
        if (isShowToast && isAdded())
            Toast.makeText(getmActivity(), message, Toast.LENGTH_SHORT).show();
    }

    /**
     * 切换分辨率
     *
     * @param map 0 : 无效不可用  1 ：有效可用
     */
    @Override
    public void showRadioButton(HashMap map) {
        if (map == null)
            return;
        Iterator iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            String key = (String) entry.getKey();
            Integer value = (Integer) entry.getValue();
            switch (key) {
                case "Audio":
                    if (value == 1)
                        btnChangePlayStatus.setVisibility(View.VISIBLE);
                    else
                        btnChangePlayStatus.setVisibility(View.GONE);
                    break;
                case "SD":
                    if (value == 1)
                        radioButtonShowSD.setVisibility(View.VISIBLE);
                    else
                        radioButtonShowSD.setVisibility(View.GONE);
                    break;
                case "HD":
                    if (value == 1)
                        radioButtonShowHD.setVisibility(View.VISIBLE);
                    else
                        radioButtonShowHD.setVisibility(View.GONE);
                    break;
                case "UHD":
                    if (value == 1)
                        radioButtonShowUHD.setVisibility(View.VISIBLE);
                    else
                        radioButtonShowUHD.setVisibility(View.GONE);
                    break;
            }
        }
    }

    @Override
    public void setScaleButtonText(int type) {
        switch (type) {
            case WatchLive.FIT_DEFAULT:
                btn_change_scaletype.setBackground(getResources().getDrawable(R.drawable.fit_default));
                break;
            case WatchLive.FIT_CENTER_INSIDE:
                btn_change_scaletype.setBackground(getResources().getDrawable(R.drawable.fit_center));
                break;
            case WatchLive.FIT_X:
                btn_change_scaletype.setBackground(getResources().getDrawable(R.drawable.fit_x));
                break;
            case WatchLive.FIT_Y:
                btn_change_scaletype.setBackground(getResources().getDrawable(R.drawable.fit_y));
                break;
            case WatchLive.FIT_XY:
                btn_change_scaletype.setBackground(getResources().getDrawable(R.drawable.fit_xy));
                break;
        }
    }

    @Override
    public void showDialogStatus(int level, final boolean isLottery, String[] nameList) {
        if (alertDialog != null) {
            alertDialog.cancel();
        }
        switch (level) {
            case 1:
                alertDialog = new AlertDialog.Builder(this.getActivity())
                        .setTitle("抽奖")
                        .setMessage("抽奖中~~~~~")
                        .create();
                alertDialog.show();
                break;
            case 2:
                if (isLottery) {
                    lotteryStr = "恭喜您,中奖了";
                } else
                    lotteryStr = "有点遗憾,这次没中";
                alertDialog = new AlertDialog.Builder(this.getActivity())
                        .setTitle(lotteryStr)
                        .setItems(nameList, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setPositiveButton("下一步", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (isLottery) {
                                    showDialogStatus(3, isLottery, null);
                                }
                            }
                        }).create();
                alertDialog.show();
                break;
            case 3:
                LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
                View submitView = layoutInflater.inflate(R.layout.submit_lottery, null);
                final EditText submitNickname = (EditText) submitView.findViewById(R.id.lottery_submit_nickname);
                final EditText submitPhone = (EditText) submitView.findViewById(R.id.lottery_submit_phone);

                alertDialog = new AlertDialog.Builder(this.getActivity())
                        .setTitle("请提供您的信息")
                        .setView(submitView)
                        .setPositiveButton("提交领奖信息", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                final String submitNicknameStr = submitNickname.getText().toString();
                                final String submitPhoneStr = submitPhone.getText().toString();
                                if (!TextUtils.isEmpty(submitNicknameStr) && !TextUtils.isEmpty(submitPhoneStr)) {
                                    if (ZeyouUtil.IsPhone(submitPhoneStr)) {
                                        mPresenter.submitLotteryInfo(submitNicknameStr, submitPhoneStr);
                                    } else {
                                        showToast("手机号填写错误");
                                        return;
                                    }
                                } else {
                                    showToast("请填写信息");
                                    return;
                                }
                            }
                        }).create();
                alertDialog.show();
                break;
            default:

        }

    }


    private SpannableStringBuilder createSpannable(Drawable drawable) {
        String text = "bitmap";
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(text);
        ImageSpan span = new ImageSpan(drawable);//ImageSpan.ALIGN_BOTTOM);
        spannableStringBuilder.setSpan(span, 0, text.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableStringBuilder.append("图文混排");
        spannableStringBuilder.setSpan(new BackgroundColorSpan(Color.parseColor("#8A2233B1")), 0, spannableStringBuilder.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        return spannableStringBuilder;
    }

    @Override
    public int changeOrientation() {
        if (this.getActivity().getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            this.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            this.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        return getActivity().getRequestedOrientation();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public RadioGroup.OnCheckedChangeListener checkListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int i) {
            if (i == R.id.radio_btn_sd) {
                mPresenter.onSwitchPixel(WatchLive.DPI_SD);
            } else if (i == R.id.radio_btn_hd) {
                mPresenter.onSwitchPixel(WatchLive.DPI_HD);
            } else if (i == R.id.radio_btn_uhd) {
                mPresenter.onSwitchPixel(WatchLive.DPI_UHD);
            } else {
            }
        }
    };

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.onDestory();
    }

    @Override
    public PlayView getPlayView() {
        return videoView;
    }

    @Override
    public boolean onBackPressed() {

        if (getActivity().getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            mPresenter.changeOriention();
        } else {
            getActivity().finish();
        }
        return true;
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
    AudioManager audioManager;
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
//                    if (newPosition >= 0) {
//                        videoView.seekTo((int) newPosition);
//                        newPosition = -1;
//                    }
                    break;
                case MESSAGE_HIDE_CONTOLL:
//                    seekTxt.setVisibility(View.GONE);
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

        itemView = root.findViewById(R.id.rl_media_controller);
        brightness_layout = (LinearLayout) root.findViewById(R.id.brightness_layout);
        brightness_seek = (VSeekBar) root.findViewById(R.id.brightness_seek);
        sound_layout = (LinearLayout) root.findViewById(R.id.sound_layout);
        sound_seek = (VSeekBar) root.findViewById(R.id.sound_seek);

        videoView.setMediaController(this);

        sound_seek.setEnabled(false);
        brightness_seek.setEnabled(false);
        detector = new GestureDetector(getActivity(), new PlayGestureListener());
        mMaxVolume = ((AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE))
                .getStreamMaxVolume(AudioManager.STREAM_MUSIC);

        mContainerLayout.setOnTouchListener(new View.OnTouchListener() {
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


        videoView.setOnInfoListener(new IMediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(IMediaPlayer mp, int what, int extra) {

                Log.e("setOnInfoListener", what + "");
                switch (what) {
                    case IMediaPlayer.MEDIA_INFO_BUFFERING_START:
                        //开始缓冲
                        if (mProcessDialog != null && !mProcessDialog.isShowing()) {

                            mProcessDialog.show();
                        }
                        break;
                    case IMediaPlayer.MEDIA_INFO_BUFFERING_END:
                        //开始播放

                        mProcessDialog.dismiss();
                        break;

                    case IMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
//                        statusChange(STATUS_PLAYING);

                        mProcessDialog.dismiss();
                        break;

                    case IMediaPlayer.MEDIA_INFO_AUDIO_RENDERING_START:

                        mProcessDialog.dismiss();
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
                setPlayPicture(true);

                videoView.stopPlayback();

                return false;
            }
        });
    }

    private boolean isFirstClick = true;
    private void start() {

        setPlayPicture(false);
        if(isFirstClick) {
            itemView.setVisibility(View.GONE);
            mProcessDialog.show();
            isFirstClick = false;
        }
    }

    private void pause() {
        setPlayPicture(true);
        videoView.pause();
    }

    private void reStart() {
        setPlayPicture(false);
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
        mProcessDialog.dismiss();
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
                volumeControl = e1.getX() < mContainerLayout.getMeasuredWidth() * 0.5;
                firstTouch = false;
            }

            if (seek) {

            } else {
                float percent = y / mContainerLayout.getHeight();
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
