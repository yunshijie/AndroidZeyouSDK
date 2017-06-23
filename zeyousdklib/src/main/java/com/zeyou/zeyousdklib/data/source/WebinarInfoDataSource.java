package com.zeyou.zeyousdklib.data.source;

import com.zeyou.zeyousdklib.Watch;
import com.zeyou.zeyousdklib.ZeyouSDK;
import com.zeyou.zeyousdklib.data.WebinarInfo;

public abstract interface WebinarInfoDataSource {
    public abstract void knockDoor(String userId, String roomId, String epiId, String userName, String msg, ZeyouSDK.RequestCallback callback);
    public abstract void heart(String userId, String roomId, String userType, String userName, String permission, ZeyouSDK.RequestCallback callback);
    public abstract void cost(String userId, String roomId, String epiId, String userName,  ZeyouSDK.RequestCallback callback);

    public abstract void doorState(String userId, String roomId, String epiId, ZeyouSDK.RequestCallback callback);

    public abstract void getBroadcastWebinarInfo(String paramString1, String paramString2, String paramString3, LoadWebinarInfoCallback paramLoadWebinarInfoCallback);

    public abstract void getWatchWebinarInfo(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6,
                                             String roomPwd, String userType, String ip, String system,String addr,String src, LoadWebinarInfoCallback paramLoadWebinarInfoCallback);

    public abstract void stopBroadcast(String paramString1, String paramString2, ZeyouSDK.RequestCallback paramRequestCallback);

    public abstract void submitLotteryInfo(String paramString1, String paramString2, String paramString3, String paramString4, ZeyouSDK.RequestCallback paramRequestCallback);

    public abstract void performSignIn(String paramString1, String paramString2, String paramString3, String paramString4, ZeyouSDK.RequestCallback paramRequestCallback);

    public static abstract interface LoadWebinarInfoCallback
            extends ZeyouSDK.ZeyouCallback {
        public abstract void onWebinarInfoLoaded(String paramString, WebinarInfo paramWebinarInfo);
    }
}



