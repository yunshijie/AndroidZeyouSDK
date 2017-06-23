 package com.zeyou.zeyousdklib.data.source.remote;

 import android.os.Handler;
 import android.os.Message;
 import android.text.TextUtils;
 import com.zeyou.zeyousdklib.data.source.PlaybackDocumentDataSource;

 import com.zeyou.zeyousdklib.utils.FileUtil;
 import java.io.File;

 public class PlaybackDocumentRemoteDataSource
   implements PlaybackDocumentDataSource
 {
   private static PlaybackDocumentRemoteDataSource INSTANCE;

   public static PlaybackDocumentRemoteDataSource getInstance()
   {
     if (INSTANCE == null) {
       INSTANCE = new PlaybackDocumentRemoteDataSource();
     }
     return INSTANCE;
   }

   public void getDocumentList(String url, final PlaybackDocumentDataSource.LoadDocumentCallback callback)
   {
     String fileName = FileUtil.getFileName(url);
     if (TextUtils.isEmpty(fileName))
     {
       callback.onDataNotAvailable("无文档！");
       return;
     }
     final File localFile = new File(FileUtil.getCacheDir(), fileName);
     if (localFile != null)
     {
       if (localFile.exists()) {
         localFile.delete();
       }
       FileUtil.downloadFile(localFile.getAbsolutePath(), url, new Handler()
       {
         public void handleMessage(Message msg)
         {
           super.handleMessage(msg);
           switch (msg.what)
           {
           case 0:
             callback.onDataNotAvailable("下载失败！");
             break;
           case 1:
             callback.onLoaded(localFile.getAbsolutePath(), null);
             break;
           case 2:
             break;
           }
         }
       });
     }
   }
 }



