package com.zeyou.zeyousdklib.data.source;


import com.zeyou.zeyousdklib.ZeyouSDK;


public class WebinarInfoRepository
        implements WebinarInfoDataSource {
    private static WebinarInfoRepository INSTANCE;
    private final WebinarInfoDataSource mWebinarInfoRemoteDataSource;

    private WebinarInfoRepository(WebinarInfoDataSource mWebinarInfoRemoteDataSource) {
        this.mWebinarInfoRemoteDataSource = mWebinarInfoRemoteDataSource;
    }

    public static WebinarInfoRepository getInstance(WebinarInfoDataSource webinarInfoRemoteDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new WebinarInfoRepository(webinarInfoRemoteDataSource);
        }
        return INSTANCE;
    }

    public void getBroadcastWebinarInfo(String id, String access_token, String zeyouId, LoadWebinarInfoCallback callback) {
        this.mWebinarInfoRemoteDataSource.getBroadcastWebinarInfo(id, access_token, zeyouId, callback);
    }

    public void getWatchWebinarInfo(String id, String name, String email, String password, String user_id, String record_id, String roomPwd, String userType, String ip, String system, String addr, String src, LoadWebinarInfoCallback callback) {
        this.mWebinarInfoRemoteDataSource.getWatchWebinarInfo(id, name, email, password, user_id, record_id, roomPwd, userType, ip, system, addr, src, callback);
    }

    public void stopBroadcast(String id, String access_token, ZeyouSDK.RequestCallback callback) {
        this.mWebinarInfoRemoteDataSource.stopBroadcast(id, access_token, callback);
    }

    public void submitLotteryInfo(String join_id, String lottery_id, String name, String phone, ZeyouSDK.RequestCallback callback) {
        this.mWebinarInfoRemoteDataSource.submitLotteryInfo(join_id, lottery_id, name, phone, callback);
    }

    public void performSignIn(String webinarId, String userId, String name, String signId, ZeyouSDK.RequestCallback callback) {
        this.mWebinarInfoRemoteDataSource.performSignIn(webinarId, userId, name, signId, callback);
    }

    @Override
    public void knockDoor(String userId, String roomId, String epiId, String userName, String msg, ZeyouSDK.RequestCallback callback) {
        mWebinarInfoRemoteDataSource.knockDoor(userId, roomId, epiId, userName, msg, callback);
    }

    @Override
    public void doorState(String userId, String roomId, String epiId, ZeyouSDK.RequestCallback callback) {
        mWebinarInfoRemoteDataSource.doorState(userId, roomId, epiId, callback);
    }

    @Override
    public void heart(String userId, String roomId, String userType, String userName, String permission, ZeyouSDK.RequestCallback callback) {
        mWebinarInfoRemoteDataSource.heart(userId, roomId, userType, userName, permission, callback);
    }

    @Override
    public void cost(String userId, String roomId, String epiId, String userName, ZeyouSDK.RequestCallback callback) {
        mWebinarInfoRemoteDataSource.cost(userId, roomId, epiId, userName, callback);
    }
}



