 package com.zeyou.zeyousdklib.data.source;


 import com.zeyou.zeyousdklib.ZeyouSDK;

 public class SurveyRepository
   implements SurveyDataSource
 {
   private static SurveyRepository INSTANCE;
   private final SurveyDataSource mSurveyRemoteDataSource;

   private SurveyRepository(SurveyDataSource mSurveyRemoteDataSource)
   {
     this.mSurveyRemoteDataSource = mSurveyRemoteDataSource;
   }

   public static SurveyRepository getInstance(SurveyDataSource mSurveyRemoteDataSource)
   {
     if (INSTANCE == null) {
       INSTANCE = new SurveyRepository(mSurveyRemoteDataSource);
     }
     return INSTANCE;
   }

   public void getSurveyInfo(String survey_id, SurveyInfoCallback callback)
   {
     this.mSurveyRemoteDataSource.getSurveyInfo(survey_id, callback);
   }

   public void submitSurveyInfo(String userid, String webinarid, String surveyid, String result, ZeyouSDK.RequestCallback callback)
   {
     this.mSurveyRemoteDataSource.submitSurveyInfo(userid, webinarid, surveyid, result, callback);
   }
 }



