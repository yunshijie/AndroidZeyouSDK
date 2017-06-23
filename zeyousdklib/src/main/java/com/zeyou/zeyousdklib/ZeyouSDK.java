package com.zeyou.zeyousdklib;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;

import com.zeyou.zeyousdklib.data.WebinarInfo;
import com.zeyou.zeyousdklib.data.source.SurveyDataSource;
import com.zeyou.zeyousdklib.data.source.SurveyRepository;
import com.zeyou.zeyousdklib.data.source.UserInfoDataSource;
import com.zeyou.zeyousdklib.data.source.UserInfoRepository;
import com.zeyou.zeyousdklib.data.source.WebinarInfoDataSource;
import com.zeyou.zeyousdklib.data.source.WebinarInfoRepository;
import com.zeyou.zeyousdklib.data.source.remote.SurveyRemoteDataSource;
import com.zeyou.zeyousdklib.data.source.remote.UserInfoRemoteDataSource;
import com.zeyou.zeyousdklib.data.source.remote.WebinarInfoRemoteDataSource;
import com.zeyou.zeyousdklib.utils.CostUtil;
import com.zeyou.zeyousdklib.utils.ErrorCode;
import com.zeyou.zeyousdklib.utils.SignatureUtil;

public final class ZeyouSDK {
    private static final String TAG = "ZeyouSDK";
    protected static String APP_KEY = "";
    protected static String APP_SECRET_KEY = "";
    protected static String packageName = "com.zeyou.zeyousdklib";
    protected static String signature = "com.zeyou.zeyousdklib";
    private static ZeyouSDK instance = null;
    private static Context mContext;

    private Handler handler;

    public ZeyouSDK() {
        handler = new Handler();
    }

    public static ZeyouSDK getInstance() {
        if (instance == null) {
            instance = new ZeyouSDK();
        }
        return instance;
    }

    public static void init(Context context, String app_key, String app_secret_key) {
        APP_KEY = app_key;
        APP_SECRET_KEY = app_secret_key;
        mContext = context;
        packageName = SignatureUtil.getPackageName(mContext);
        signature = SignatureUtil.getSignatureSHA1(mContext);
    }

    public Context getContext() {
        return mContext;
    }

    public static boolean isInit() {
        return (!TextUtils.isEmpty(APP_KEY)) && (!TextUtils.isEmpty(APP_SECRET_KEY));
    }

    public static void setLogEnable(boolean enable) {
        com.zeyou.zeyousdklib.utils.LogManager.isDebug = enable;
    }

    public void login(String userName, String passWord, UserInfoDataSource.UserInfoCallback callback) {
        if ((TextUtils.isEmpty(userName)) || (TextUtils.isEmpty(passWord))) {
            ErrorCallback(callback, 20001, "请输入账号或者密码");
            return;
        }
        UserInfoRepository userRepository = UserInfoRepository.getInstance(UserInfoRemoteDataSource.getInstance());
        userRepository.getUserInfo(userName, passWord, callback);
    }

    public void visit(String userId, UserInfoDataSource.UserInfoCallback callback) {

        UserInfoRepository userRepository = UserInfoRepository.getInstance(UserInfoRemoteDataSource.getInstance());
        userRepository.getVisitInfo(userId, callback);
    }
//
//   public void initBroadcast(String id, String accessToken, String zeyouId, final Broadcast broadcast, final RequestCallback callback)
//   {
//     if ((TextUtils.isEmpty(id)) || (TextUtils.isEmpty(accessToken)) || (broadcast == null))
//     {
//       ErrorCallback(callback, 261, ErrorCode.getErrorStr(261));
//       return;
//     }
//     WebinarInfoRepository repository = WebinarInfoRepository.getInstance(WebinarInfoRemoteDataSource.getInstance());
//     repository.getBroadcastWebinarInfo(id, accessToken, zeyouId, new WebinarInfoDataSource.LoadWebinarInfoCallback()
//     {
//       public void onWebinarInfoLoaded(String jsonStr, WebinarInfo webinarInfo)
//       {
//         if ((webinarInfo.status == 1) || (webinarInfo.status == 5))
//         {
//           ZeyouSDK.ErrorCallback(callback, 20101, "当前视频处在" + webinarInfo.getStatusStr() + "状态");
//         }
//         else
//         {
//           broadcast.setWebinarInfo(webinarInfo);
//           if (callback != null) {
//             callback.onSuccess();
//           }
//         }
//       }
//
//       public void onError(int errorCode, String reason)
//       {
//         ZeyouSDK.ErrorCallback(callback, errorCode, reason);
//       }
//     });
//   }
//
//   public void finishBroadcast(String id, String accessToken, Broadcast broadcast, RequestCallback callback)
//   {
//     if ((TextUtils.isEmpty(id)) || (TextUtils.isEmpty(accessToken)) || (broadcast == null))
//     {
//       ErrorCallback(callback, 261, ErrorCode.getErrorStr(261));
//       return;
//     }
//     broadcast.stop();
//     WebinarInfoRepository repository = WebinarInfoRepository.getInstance(WebinarInfoRemoteDataSource.getInstance());
//     repository.stopBroadcast(id, accessToken, callback);
//   }

    public void heart(String userId, String roomId, String userType, String userName, String permission, final ZeyouSDK.RequestCallback callback) {

        if ((TextUtils.isEmpty(userId)) || (TextUtils.isEmpty(roomId)) || (TextUtils.isEmpty(userType)) || (TextUtils.isEmpty(userName)) || (TextUtils.isEmpty(permission))) {
            ErrorCallback(callback, 261, ErrorCode.getErrorStr(261));
            return;
        }

        WebinarInfoRepository repository = WebinarInfoRepository.getInstance(WebinarInfoRemoteDataSource.getInstance());
        repository.heart(userId, roomId, userType, userName, permission, callback);
    }

    public void cost(String userId, String roomId, String epiId, String userName, final ZeyouSDK.RequestCallback callback) {
        if ((TextUtils.isEmpty(userId)) || (TextUtils.isEmpty(roomId)) || (TextUtils.isEmpty(epiId)) || (TextUtils.isEmpty(userName))) {
            ErrorCallback(callback, 261, ErrorCode.getErrorStr(261));
            return;
        }

        WebinarInfoRepository repository = WebinarInfoRepository.getInstance(WebinarInfoRemoteDataSource.getInstance());
        repository.cost(userId, roomId, epiId, userName, callback);
    }

    public void knockDoor(String userId, String roomId, String epiId, String userName, String msg, RequestCallback callback) {

        if ((TextUtils.isEmpty(userId)) || (TextUtils.isEmpty(roomId)) || (TextUtils.isEmpty(userName)) || (TextUtils.isEmpty(msg))) {
            ErrorCallback(callback, 261, ErrorCode.getErrorStr(261));
            return;
        }
        WebinarInfoRepository repository = WebinarInfoRepository.getInstance(WebinarInfoRemoteDataSource.getInstance());
        repository.knockDoor(userId, roomId, epiId, userName, msg, callback);
    }

    public void doorState(String userId, String roomId, String epiId, RequestCallback callback) {
        if ((TextUtils.isEmpty(userId)) || (TextUtils.isEmpty(roomId)) ) {
            ErrorCallback(callback, 261, ErrorCode.getErrorStr(261));
            return;
        }
        WebinarInfoRepository repository = WebinarInfoRepository.getInstance(WebinarInfoRemoteDataSource.getInstance());
        repository.doorState(userId, roomId, epiId, callback);
    }

    public void initWatch(String id, String nickname, String email, String userId, String password, String roomPwd, String userType, String ip, String system, String addr, String src, Watch watch, RequestCallback callback) {
        initWatch(id, nickname, email, userId, password, "", roomPwd, userType, ip, system, addr, src, watch, callback);
    }

    //进入房间输入密码
    @Deprecated
    public void initWatch(final String id, final String nickname, String email, final String userId, String password, String recordId, String roomPwd, final String userType, String ip, String system, String addr, String src, final Watch watch, final RequestCallback callback) {
        if (TextUtils.isEmpty(id)) {
            ErrorCallback(callback, 261, ErrorCode.getErrorStr(261));
            return;
        }
        if ((TextUtils.isEmpty(userId)) && ((TextUtils.isEmpty(nickname)) || (TextUtils.isEmpty(email)))) {
            ErrorCallback(callback, 261, ErrorCode.getErrorStr(261));
            return;
        }
        WebinarInfoRepository repository = WebinarInfoRepository.getInstance(WebinarInfoRemoteDataSource.getInstance());
        repository.getWatchWebinarInfo(id, nickname, email, password, userId, recordId, roomPwd, userType, ip, system, addr, src, new WebinarInfoDataSource.LoadWebinarInfoCallback() {
            public void onWebinarInfoLoaded(String jsonStr, WebinarInfo webinarInfo) {
                if (webinarInfo != null) {
//                    if ((webinarInfo.status == 1) && ((watch instanceof WatchLive))) {


                    CostUtil.Param costParam = new CostUtil.Param(id, userId, nickname, userType);
                    final CostUtil costUtil = new CostUtil(handler, webinarInfo, costParam, watch.getPlayView());
                    watch.setCallback(new Watch.Callback() {
                        @Override
                        public void onStart() {
                            costUtil.firstCost();
                        }

                        @Override
                        public void onDestory() {
                            costUtil.removeCallbacks();
                        }
                    });

                    if ((watch instanceof WatchLive)) {
                        watch.setWebinarInfo(webinarInfo);
                        if (callback != null) {
                            callback.onSuccess();
                        }
                        return;
                    }
//                    if (((webinarInfo.status == 4) || (webinarInfo.status == 5)) && ((watch instanceof WatchPlayback))) {
                    if ((watch instanceof WatchPlayback)) {
                        watch.setWebinarInfo(webinarInfo);
                        if (callback != null) {
                            callback.onSuccess();
                        }
                        return;
                    }


//                    String status = webinarInfo.getStatusStr();
//                    ZeyouSDK.ErrorCallback(callback, 20209, "当前视频处在" + status + "状态");
                    ZeyouSDK.ErrorCallback(callback, 20209, "返回数据错误");
                }
            }

            public void onError(int errorCode, String reason) {
                ZeyouSDK.ErrorCallback(callback, errorCode, reason);
            }
        });
    }

    public void performSignIn(String webinarId, String userId, String name, String signId, RequestCallback callback) {
        if ((TextUtils.isEmpty(webinarId)) || (TextUtils.isEmpty(signId))) {
            ErrorCallback(callback, 261, ErrorCode.getErrorStr(261));
            return;
        }
        if ((TextUtils.isEmpty(userId)) || (TextUtils.isEmpty(name)) || (TextUtils.isEmpty(signId))) {
            ErrorCallback(callback, 264, ErrorCode.getErrorStr(264));
            return;
        }
        WebinarInfoRepository repository = WebinarInfoRepository.getInstance(WebinarInfoRemoteDataSource.getInstance());
        repository.performSignIn(webinarId, userId, name, signId, callback);
    }

    public void submitLotteryInfo(String join_id, String lottery_id, String name, String phone, RequestCallback callback) {
        if ((TextUtils.isEmpty(join_id)) || (TextUtils.isEmpty(lottery_id)) || (TextUtils.isEmpty(name)) || (TextUtils.isEmpty(phone))) {
            ErrorCallback(callback, 261, ErrorCode.getErrorStr(261));
            return;
        }
        WebinarInfoRepository repository = WebinarInfoRepository.getInstance(WebinarInfoRemoteDataSource.getInstance());
        repository.submitLotteryInfo(join_id, lottery_id, name, phone, callback);
    }

    public void getSurveyInfo(String survey_id, SurveyDataSource.SurveyInfoCallback callback) {
        if (TextUtils.isEmpty(survey_id)) {
            ErrorCallback(callback, 261, ErrorCode.getErrorStr(261));
            return;
        }
        SurveyRepository repository = SurveyRepository.getInstance(SurveyRemoteDataSource.getInstance());
        repository.getSurveyInfo(survey_id, callback);
    }

    public void submitSurveyInfo(String userid, Watch watch, String surveyid, String result, RequestCallback callback) {
        if ((TextUtils.isEmpty(userid)) || (TextUtils.isEmpty(surveyid)) || (TextUtils.isEmpty(result))) {
            ErrorCallback(callback, 261, ErrorCode.getErrorStr(261));
            return;
        }
        if ((watch == null) || (!watch.isAvaliable())) {
            ErrorCallback(callback, 261, ErrorCode.getErrorStr(261));
            return;
        }
        SurveyRepository repository = SurveyRepository.getInstance(SurveyRemoteDataSource.getInstance());
        repository.submitSurveyInfo(userid, watch.webinarInfo.webinar_id, surveyid, result, callback);
    }

    public static void ErrorCallback(ZeyouCallback callback, int errorCode, String errorMsg) {
        if (callback != null) {
            callback.onError(errorCode, errorMsg);
        }
    }

    public static abstract interface RequestCallback
            extends ZeyouCallback {
        public abstract void onSuccess();
    }

    public static abstract interface ZeyouCallback {
        public abstract void onError(int paramInt, String paramString);
    }
}



