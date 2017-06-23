package com.zeyou.zeyousdklib.data.source;

import com.zeyou.zeyousdklib.ZeyouSDK;
import com.zeyou.zeyousdklib.data.Survey;

public abstract interface SurveyDataSource
{
  public abstract void getSurveyInfo(String paramString, SurveyInfoCallback paramSurveyInfoCallback);
  
  public abstract void submitSurveyInfo(String paramString1, String paramString2, String paramString3, String paramString4, ZeyouSDK.RequestCallback paramRequestCallback);
  
  public static abstract interface SurveyInfoCallback
    extends ZeyouSDK.ZeyouCallback
  {
    public abstract void onSuccess(Survey paramSurvey);
  }
}



