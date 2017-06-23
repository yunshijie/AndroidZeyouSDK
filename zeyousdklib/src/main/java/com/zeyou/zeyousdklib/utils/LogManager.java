 package com.zeyou.zeyousdklib.utils;

 import android.util.Log;
 import com.zeyou.zeyousdklib.HttpDataSource;

 public class LogManager
 {
   private static final boolean innerDebug = HttpDataSource.debugMode;
   public static boolean isDebug = true;
   private static final String TAG = "ZeyouSDK";

   public static void i(String msg)
   {
     if (isDebug) {
       Log.i("ZeyouSDK", msg);
     }
   }

   public static void d(String msg)
   {
     if (isDebug) {
       Log.d("ZeyouSDK", msg);
     }
   }

   public static void e(String msg)
   {
     if (isDebug) {
       Log.e("ZeyouSDK", msg);
     }
   }

   public static void v(String msg)
   {
     if (isDebug) {
       Log.v("ZeyouSDK", msg);
     }
   }

   public static void i(String tag, String msg)
   {
     if (isDebug) {
       Log.i(tag, msg);
     }
   }

   public static void d(String tag, String msg)
   {
     if (isDebug) {
       Log.d(tag, msg);
     }
   }

   public static void e(String tag, String msg)
   {
     if (isDebug) {
       Log.e(tag, msg);
     }
   }

   public static void v(String tag, String msg)
   {
     if (isDebug) {
       Log.v(tag, msg);
     }
   }

   public static void innerLog(String tag, String msg)
   {
     if (innerDebug) {
       Log.e(tag, msg);
     }
   }
 }



