 package com.zeyou.zeyousdklib.utils;

 public class ErrorCode
 {
   public static final String CALLBACK_ERROR_STR = "call should not be null!";
   public static final int INIT_FAILED = 20001;
   public static final int IO_EXCEPTION = 257;
   public static final int JSON_EXCEPTION = 258;
   public static final int EXCEPTION = 259;
   public static final int NO_DATA = 260;
   public static final int INPUT_PARAMETER_ERROR = 261;
   public static final int VIDEO_STATUS_BROADCAST = 262;
   public static final int INPUT_PARAMETER_CONTENT = 263;
   public static final int USER_LOGIN_OUT = 264;

   public static final int NEED_KNOCK_DOOR = 265;
   public static final int NEED_PASSWORD = 266;
   public static final int NOT_PERMIT = 267;
   public static final int LACK_MONEY = 268;
   public static final int PROCESSING_KNOCK_DOOR = 269;
   public static final int REJECT_KNOCK_DOOR = 270;

   public static final int COST_NO_NEED = 271;
   public static final int COST_INVALID = 272;

   public static final int SUCCESS = 200;

   public static String getErrorStr(int code)
   {
     String errorMsg = "";
     switch (code)
     {
     case 261:
       errorMsg = "参数错误";
       break;
     case 260:
       errorMsg = "未获取数据";
       break;
     case 262:
       errorMsg = "直播中,获取失败";
       break;
     case 263:
       errorMsg = "请输入内容";
       break;
     case 264:
       errorMsg = "当前用户未登录";
       break;
     default:
       errorMsg = "未知错误";
     }
     return errorMsg;
   }
 }



