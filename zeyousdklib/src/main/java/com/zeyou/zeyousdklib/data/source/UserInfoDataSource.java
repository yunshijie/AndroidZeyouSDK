package com.zeyou.zeyousdklib.data.source;

import com.zeyou.zeyousdklib.ZeyouSDK;
import com.zeyou.zeyousdklib.data.UserInfo;

public abstract interface UserInfoDataSource
{
  public abstract void getUserInfo(String paramString1, String paramString2, UserInfoCallback paramUserInfoCallback);
  public abstract void getVisitInfo(String userId, UserInfoCallback paramUserInfoCallback);
  
  public abstract void sendChat(String paramString1, String paramString2, String paramString3, ZeyouSDK.RequestCallback paramRequestCallback);
  
  public abstract void sendComment(String paramString1, String paramString2, String paramString3, ZeyouSDK.RequestCallback paramRequestCallback);
  
  public abstract void sendQuestion(String paramString1, String paramString2, String paramString3, ZeyouSDK.RequestCallback paramRequestCallback);
  
  public static abstract interface UserInfoCallback
    extends ZeyouSDK.ZeyouCallback
  {
    public abstract void onSuccess(UserInfo paramUserInfo);
  }
}



