package com.zeyou.uilibs.watch;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.zeyou.uilibs.Param;
import com.zeyou.uilibs.chat.ChatFragment;
import com.zeyou.uilibs.util.ActivityUtils;
import com.zeyou.uilibs.util.BackHandlerHelper;
import com.zeyou.uilibs.util.ExtendTextView;
import com.zeyou.uilibs.util.SignInDialog;
import com.zeyou.uilibs.util.SurveyPopu;
import com.zeyou.uilibs.util.ZeyouUtil;
import com.zeyou.uilibs.util.emoji.InputUser;
import com.zeyou.uilibs.util.emoji.InputView;
import com.zeyou.uilibs.util.emoji.KeyBoardManager;
import com.zeyou.upplayer.R;


/**
 * 观看页的Activity
 */
public class WatchActivity extends FragmentActivity implements WatchContract.WatchView {

    private FrameLayout contentDoc, contentDetail, contentChat, contentQuestion;
    private RadioGroup radio_tabs;
    private RadioButton questionBtn, chatBtn;
    private LinearLayout ll_detail;
    ExtendTextView tv_notice;
    private Param param;
    private int type;

    public WatchPlaybackFragment playbackFragment;
    public WatchLiveFragment liveFragment;
    public ChatFragment chatFragment;
    public ChatFragment questionFragment;


    InputView inputView;
    public int chatEvent = ChatFragment.CHAT_EVENT_CHAT;
    private SurveyPopu popu;
    private SignInDialog signInDialog;

    WatchContract.WatchPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN | WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.watch_activity);
        param = (Param) getIntent().getSerializableExtra("param");
        type = getIntent().getIntExtra("type", ZeyouUtil.WATCH_LIVE);
        liveFragment = (WatchLiveFragment) getSupportFragmentManager().findFragmentById(R.id.contentVideo);
        playbackFragment = (WatchPlaybackFragment) getSupportFragmentManager().findFragmentById(R.id.contentVideo);
        chatFragment = (ChatFragment) getSupportFragmentManager().findFragmentById(R.id.contentChat);
        DocumentFragment docFragment = (DocumentFragment) getSupportFragmentManager().findFragmentById(R.id.contentDoc);
        DetailFragment detailFragment = (DetailFragment) getSupportFragmentManager().findFragmentById(R.id.contentDetail);
        questionFragment = (ChatFragment) getSupportFragmentManager().findFragmentById(R.id.contentQuestion);
        initView();
        if (chatFragment == null) {
            chatFragment = ChatFragment.newInstance(type, false);
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    chatFragment, R.id.contentChat);
        }
        if (docFragment == null) {
            docFragment = DocumentFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    docFragment, R.id.contentDoc);
        }

        if (questionFragment == null && type == ZeyouUtil.WATCH_LIVE) {
            questionFragment = ChatFragment.newInstance(type, true);
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    questionFragment, R.id.contentQuestion);
        }

        if (detailFragment == null) {
            detailFragment = DetailFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    detailFragment, R.id.contentDetail);
        }

        if (liveFragment == null && type == ZeyouUtil.WATCH_LIVE) {
            liveFragment = WatchLiveFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    liveFragment, R.id.contentVideo);
            new WatchLivePresenter(liveFragment, docFragment, chatFragment, questionFragment, this, param);
        }

        if (playbackFragment == null && type == ZeyouUtil.WATCH_PLAYBACK) {
            playbackFragment = WatchPlaybackFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    playbackFragment, R.id.contentVideo);
            new WatchPlaybackPresenter(playbackFragment, docFragment, chatFragment, this, param);
        }

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN && inputView.getContentView().getVisibility() == View.VISIBLE) {
            boolean isDismiss = isShouldHideInput(inputView.getContentView(), ev);
            if (isDismiss) {
                inputView.dismiss();
                return false;
            } else {
                return super.dispatchTouchEvent(ev);
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    public boolean isShouldHideInput(View view, MotionEvent event) {
        if (view.getVisibility() == View.GONE)
            return false;
        int[] leftTop = {0, 0};
        //获取输入框当前的location位置
        inputView.getContentView().getLocationInWindow(leftTop);
        int left = leftTop[0];
        int top = leftTop[1];
        int bottom = top + inputView.getContentView().getHeight();
        int right = left + inputView.getContentView().getWidth();
        return !(event.getX() > left && event.getX() < right
                && event.getY() > top && event.getY() < bottom);
    }

    private void initView() {
        inputView = new InputView(this, KeyBoardManager.getKeyboardHeight(this), KeyBoardManager.getKeyboardHeightLandspace(this));
        inputView.add2Window(this);
        inputView.setClickCallback(new InputView.ClickCallback() {
            @Override
            public void onEmojiClick() {

            }
        });
        inputView.setOnSendClickListener(new InputView.SendMsgClickListener() {

            @Override
            public void onSendClick(String msg, InputUser user) {
                if (chatFragment != null && chatEvent == ChatFragment.CHAT_EVENT_CHAT) {
                    chatFragment.performSend(msg, chatEvent);
                } else if (questionFragment != null && chatEvent == ChatFragment.CHAT_EVENT_QUESTION) {
                    questionFragment.performSend(msg, chatEvent);
                }
            }
        });
        inputView.setOnHeightReceivedListener(new InputView.KeyboardHeightListener() {
            @Override
            public void onHeightReceived(int screenOri, int height) {
                if (screenOri == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                    KeyBoardManager.setKeyboardHeight(WatchActivity.this, height);
                } else {
                    KeyBoardManager.setKeyboardHeightLandspace(WatchActivity.this, height);
                }
            }
        });
        ll_detail = (LinearLayout) this.findViewById(R.id.ll_detail);
        contentDoc = (FrameLayout) findViewById(R.id.contentDoc);
        contentDetail = (FrameLayout) findViewById(R.id.contentDetail);
        contentChat = (FrameLayout) findViewById(R.id.contentChat);
        contentQuestion = (FrameLayout) findViewById(R.id.contentQuestion);

        questionBtn = (RadioButton) this.findViewById(R.id.rb_question);
        chatBtn = (RadioButton) this.findViewById(R.id.rb_chat);

        tv_notice = (ExtendTextView) this.findViewById(R.id.tv_notice);
        tv_notice.setDrawableClickListener(new ExtendTextView.DrawableClickListener() {
            @Override
            public void onDrawableClick(int position) {
                switch (position) {
                    case ExtendTextView.DRAWABLE_RIGHT:
                        dismissNotice();
                        break;
                }
            }
        });
        if (type == ZeyouUtil.WATCH_LIVE) {
            questionBtn.setVisibility(View.VISIBLE);
            contentChat.setVisibility(View.VISIBLE);
            chatBtn.setText("聊天");
        }
        if (type == ZeyouUtil.WATCH_PLAYBACK) {
            chatBtn.setText("评论");
            contentChat.setVisibility(View.VISIBLE);
        }
        radio_tabs = (RadioGroup) findViewById(R.id.radio_tabs);
        radio_tabs.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_chat) {
                    chatEvent = ChatFragment.CHAT_EVENT_CHAT;
                    contentChat.setVisibility(View.VISIBLE);
                    contentDoc.setVisibility(View.GONE);
                    contentDetail.setVisibility(View.GONE);
                    contentQuestion.setVisibility(View.GONE);
                } else if (checkedId == R.id.rb_doc) {
                    contentDoc.setVisibility(View.VISIBLE);
                    contentChat.setVisibility(View.GONE);
                    contentDetail.setVisibility(View.GONE);
                    contentQuestion.setVisibility(View.GONE);
                } else if (checkedId == R.id.rb_question) {
                    chatEvent = ChatFragment.CHAT_EVENT_QUESTION;
                    contentDoc.setVisibility(View.GONE);
                    contentDetail.setVisibility(View.GONE);
                    contentQuestion.setVisibility(View.VISIBLE);
                    contentChat.setVisibility(View.GONE);
                } else if (checkedId == R.id.rb_detail) {
                    contentDoc.setVisibility(View.GONE);
                    contentChat.setVisibility(View.GONE);
                    contentQuestion.setVisibility(View.GONE);
                    contentDetail.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public void showChatView(boolean isShowEmoji, InputUser user, int contentLengthLimit) {
        if (contentLengthLimit > 0)
            inputView.setLimitNo(contentLengthLimit);
        inputView.show(isShowEmoji, user);
    }

    @Override
    public void showSignIn(String signId, int startTime) {
        if (signInDialog == null) {
            signInDialog = new SignInDialog(this);
        }
        signInDialog.setSignInId(signId);
        signInDialog.setCountDownTime(startTime);
        signInDialog.setOnSignInClickListener(new SignInDialog.OnSignInClickListener() {
            @Override
            public void signIn(String signId) {
                mPresenter.signIn(signId);
            }
        });
        signInDialog.show();
    }

    @Override
    public void dismissSignIn() {
        if (signInDialog != null)
            signInDialog.dismiss();
    }

//    @Override
//    public void showSurvey(Survey survey) {
//        if (popu == null) {
//            popu = new SurveyPopu(this);
//            popu.setOnSubmitClickListener(new SurveyPopu.OnSubmitClickListener() {
//                @Override
//                public void onSubmitClick(Survey survey1, String result) {
//                    mPresenter.submitSurvey(survey1, result);
//                }
//            });
//        }
//        popu.setSurvey(survey);
//        popu.showAtLocation(getWindow().getDecorView().findViewById(android.R.id.content), Gravity.NO_GRAVITY, 0, 0);
//    }

    @Override
    public void dismissSurvey() {
        if (popu != null)
            popu.dismiss();
    }

    @Override
    public void setShowDetail(boolean isShow) {
        if (isShow) {
            ll_detail.setVisibility(View.VISIBLE);
        } else {
            ll_detail.setVisibility(View.GONE);
        }
    }

    @Override
    public void showNotice(String content) {
        tv_notice.setText(content);
        tv_notice.setVisibility(View.VISIBLE);
    }

    @Override
    public void dismissNotice() {
        tv_notice.setVisibility(View.GONE);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        inputView.dismiss();
    }

    @Override
    public void onBackPressed() {
        inputView.dismiss();
        if (!BackHandlerHelper.handleBackPress(this)) {
            super.onBackPressed();
        }
    }

    @Override
    protected void onUserLeaveHint() {
        if (null != inputView) {
            inputView.dismiss();
        }
        super.onUserLeaveHint();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void setPresenter(WatchContract.WatchPresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


}
