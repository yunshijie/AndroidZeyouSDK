 package com.zeyou.zeyousdklib;

 import com.zeyou.zeyousdklib.utils.LogManager;
 import com.zeyou.zeyousdklib.utils.Md5Encode;
 import java.io.IOException;
 import java.util.Arrays;
 import java.util.HashMap;
 import java.util.concurrent.TimeUnit;

 import okhttp3.Call;
 import okhttp3.Callback;

 import okhttp3.FormBody;
 import okhttp3.OkHttpClient;
 import okhttp3.Request;

 import okhttp3.RequestBody;

 public class HttpDataSource
 {
   private static final String TAG = "HttpDataSource";
   public static boolean debugMode = true;
   public static final String BASE_URL = debugMode ? "http://192.168.11.230:8081/api/v1/" : "https://api.gaoqinghuiyi.com/api/v1/";

   public static void post(String area, String api_name, HashMap<String, String> hashMap, Callback callback)
   {
     if (!ZeyouSDK.isInit())
     {
       callback.onFailure(null, new IOException("init SDK first..."));
       return;
     }
     String url = BASE_URL + area;
     hashMap.put("api_name", api_name);
     hashMap.put("app_key", ZeyouSDK.APP_KEY);
     hashMap.put("app_secret_key", ZeyouSDK.APP_SECRET_KEY);
     hashMap.put("client", "android");
     hashMap.put("package_name", ZeyouSDK.packageName);
     hashMap.put("signature", ZeyouSDK.signature);

     String param = getParam(hashMap);
     hashMap.put("sign", param);

     RequestBody body = getRequestBody(hashMap);
     post(url, body, callback);
   }

   private static String getParam(HashMap<String, String> params)
   {
     String param = "";
     Object[] key = params.keySet().toArray();
     Arrays.sort(key);
     for (int i = 0; i < key.length; i++) {
       param = param + key[i] + (String)params.get(key[i]);
     }
     LogManager.innerLog("HttpDataSource", "param:" + param);
     return Md5Encode.getMD5(param);
   }

   private static RequestBody getRequestBody(HashMap<String, String> hashMap)
   {
     String param = "";
     FormBody.Builder builder = new FormBody.Builder();
     for (String key : hashMap.keySet())
     {
       param = param + key + ":" + (String)hashMap.get(key) + "\n";
       builder.add(key, (String)hashMap.get(key));
     }
     LogManager.innerLog("HttpDataSource", param);
     return builder.build();
   }

   public static void post(String url, RequestBody body, Callback callback)
   {
     OkHttpClient mOkHttpClient = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).build();
     Request request = new Request.Builder().url(url).post(body).build();
     Call call = mOkHttpClient.newCall(request);
     call.enqueue(callback);
   }
 }



