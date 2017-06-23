 package com.zeyou.zeyousdklib.data.source.remote;

 import android.os.Handler;
 import android.os.Looper;
 import com.zeyou.zeyousdklib.HttpDataSource;
 import com.zeyou.zeyousdklib.ZeyouSDK;

 import com.zeyou.zeyousdklib.data.Survey;

 import com.zeyou.zeyousdklib.data.source.SurveyDataSource;

 import com.zeyou.zeyousdklib.utils.LogManager;
 import java.io.IOException;
 import java.util.ArrayList;
 import java.util.HashMap;
 import okhttp3.Call;
 import okhttp3.Callback;
 import okhttp3.Response;

 import org.json.JSONArray;
 import org.json.JSONException;
 import org.json.JSONObject;

 public class SurveyRemoteDataSource
   implements SurveyDataSource
 {
   private static final String TAG = SurveyRemoteDataSource.class.getName();
   private static Handler mDelivery;
   private static SurveyRemoteDataSource INSTANCE;

   public static SurveyRemoteDataSource getInstance()
   {
     if (INSTANCE == null)
     {
       INSTANCE = new SurveyRemoteDataSource();
       mDelivery = new Handler(Looper.getMainLooper());
     }
     return INSTANCE;
   }

   public void getSurveyInfo(String survey_id, final SurveyDataSource.SurveyInfoCallback callback)
   {
     HashMap<String, String> map = new HashMap();
     map.put("survey_id", survey_id);
     HttpDataSource.post("survey/info", "survey/info", map, new Callback()
     {
       public void onFailure(Call call, final IOException e)
       {
         SurveyRemoteDataSource.mDelivery.post(new Runnable()
         {
           public void run()
           {
             ZeyouSDK.ErrorCallback(callback, 257, e.getMessage());
             ZeyouSDK.ErrorCallback(callback, 257, e.getMessage());
           }
         });
       }

       public void onResponse(Call call, final Response response)
         throws IOException
       {
         if (callback == null) {
           return;
         }
         final String resultStr = response.body().string();
         LogManager.innerLog(SurveyRemoteDataSource.TAG, resultStr);
         SurveyRemoteDataSource.mDelivery.post(new Runnable()
         {
           public void run()
           {
             if (response.code() == 200) {
               SurveyRemoteDataSource.this.dealResponse(resultStr, callback);
             } else {
               ZeyouSDK.ErrorCallback(callback, response.code(), response.message());
             }
           }
         });
       }
     });
   }

   public void submitSurveyInfo(String userid, String webinarid, String surveyid, String result, final ZeyouSDK.RequestCallback callback)
   {
     HashMap<String, String> map = new HashMap();
     map.put("webinar_id", webinarid);
     map.put("user_id", userid);
     map.put("survey_id", surveyid);
     map.put("question_answer", result);

     HttpDataSource.post("survey/answer", "survey/answer", map, new Callback()
     {
       public void onFailure(Call call, final IOException e)
       {
         SurveyRemoteDataSource.mDelivery.post(new Runnable()
         {
           public void run()
           {
             if (callback != null) {
               ZeyouSDK.ErrorCallback(callback, 257, e.getMessage());
             }
           }
         });
       }

       public void onResponse(Call call, final Response response)
         throws IOException
       {
         if (callback == null) {
           return;
         }
         final String resultStr = response.body().string();
         LogManager.innerLog(SurveyRemoteDataSource.TAG, resultStr);
         SurveyRemoteDataSource.mDelivery.post(new Runnable()
         {
           public void run()
           {
             if (response.code() == 200) {
               try
               {
                 JSONObject result = new JSONObject(resultStr);
                 String msg = result.optString("msg");
                 int code = result.optInt("code");
                 if (code == 200) {
                   callback.onSuccess();
                 } else {
                   ZeyouSDK.ErrorCallback(callback, code, msg);
                 }
               }
               catch (JSONException e)
               {
                 e.printStackTrace();
               }
             } else {
               ZeyouSDK.ErrorCallback(callback, response.code(), response.message());
             }
           }
         });
       }
     });
   }

   private void dealResponse(String resultStr, SurveyDataSource.SurveyInfoCallback callback)
   {
     try
     {
       JSONObject result = new JSONObject(resultStr);
       String msg = result.optString("msg");
       int code = result.optInt("code");
       if (code == 200)
       {
         JSONObject data = result.getJSONObject("data");
         Survey survey = new Survey();
         survey.surveyid = data.optString("survey_id");
         survey.subject = data.optString("subject");
         survey.webinarid = data.optString("webinar_id");
         survey.questions = new ArrayList();
         JSONArray array = data.optJSONArray("list");
         if ((array != null) && (array.length() > 0)) {
           for (int i = 0; i < array.length(); i++)
           {
             JSONObject obj = array.optJSONObject(i);
             Survey.Question question = new Survey.Question();
             question.ques_id = obj.optString("ques_id");
             question.subject = obj.optString("subject");
             question.ordernum = obj.optInt("ordernum");
             question.must = obj.optInt("must");
             question.type = obj.optInt("type");
             if (question.type != 0)
             {
               question.options = new ArrayList();
               JSONArray optionArry = obj.optJSONArray("list");
               if ((optionArry != null) && (optionArry.length() > 0)) {
                 for (int j = 0; j < optionArry.length(); j++)
                 {
                   JSONObject optObj = optionArry.optJSONObject(j);
                   if (optObj != null) {
                     question.options.add(optObj.optString("subject"));
                   }
                 }
               }
             }
             survey.questions.add(question);
           }
         }
         callback.onSuccess(survey);
       }
       else
       {
         ZeyouSDK.ErrorCallback(callback, code, msg);
       }
     }
     catch (JSONException e)
     {
       ZeyouSDK.ErrorCallback(callback, 258, e.getMessage());
       e.printStackTrace();
     }
     catch (Exception e)
     {
       ZeyouSDK.ErrorCallback(callback, 259, e.getMessage());
       e.printStackTrace();
     }
   }
 }



