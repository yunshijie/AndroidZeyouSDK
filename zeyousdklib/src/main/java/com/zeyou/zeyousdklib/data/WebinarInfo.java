 package com.zeyou.zeyousdklib.data;

 import java.util.ArrayList;
 import java.util.List;

 public class WebinarInfo
 {
   public static final int LIVE = 1;
   public static final int BESPEAK = 2;
   public static final int END = 3;
   public static final int VIDEO = 4;
   public static final int MEDIA = 5;
   public String webinar_id;
   public String media_srv;
   public String streamtoken;
   public String msg_token;
   public String msg_server;
   public String chat_server;
   public String chat_token;
   public String publish_server;
   public String host;
   public int layout;
   public int buffer;
   public String video;
   public String join_id;
   public String docurl;
   public int status;
   public int page;
   public String doc;
   public String session_id;
   public String user_id;

   public List<Stream> streams;
   public Notice notice;
   public Result A;
   public Result UHD;

   //默认视频
   public String default_video;
   public String rtmp_video;
   public Result SD;
   public Result HD;
   public int room_id;
   public int epi_id;
   public int permission;
   // min time
   public int costtime1;
   // max time
   public int costtime2;
   public int heart;
   public int definition;
   public int need_cost;

   public WebinarInfo()
   {
     this.page = -1;







     this.streams = new ArrayList();



     this.A = new Result();
     this.SD = new Result();
     this.HD = new Result();
     this.UHD = new Result();
   }

   public String getStatusStr()
   {
     String statusStr = "";
     switch (this.status)
     {
     case 1:
       statusStr = "直播";
       break;
     case 2:
       statusStr = "预告";
       break;
     case 4:
       statusStr = "回放";
       break;
     case 3:
       statusStr = "结束";
       break;
     case 5:
       statusStr = "录播";
     }
     return statusStr;
   }

   public static class Notice
   {
     public String content;
     public String publish_release_time;
   }

   public class Result
   {
     public String value;
     public int valid;

     public Result() {}
   }

   public class Stream
   {
     public String name;
     public String src;

     public Stream() {}
   }
 }



