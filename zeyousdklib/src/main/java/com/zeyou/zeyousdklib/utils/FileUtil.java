 package com.zeyou.zeyousdklib.utils;

 import android.os.Environment;
 import android.os.Handler;
 import android.os.Message;
 import java.io.ByteArrayOutputStream;
 import java.io.File;
 import java.io.FileInputStream;
 import java.io.FileOutputStream;
 import java.io.IOException;
 import java.io.InputStream;

 import okhttp3.Call;
 import okhttp3.Callback;
 import okhttp3.OkHttpClient;
 import okhttp3.Request;
 import okhttp3.Request.Builder;
 import okhttp3.Response;



 public class FileUtil
 {
   private static final String app_name = "zeyou";
   private static final String cache_dir = "zeyou" + File.separator + "sdkcache";

   public static String getFileName(String filePath)
   {
     String name = "";
     try
     {
       name = filePath.substring(filePath.lastIndexOf("/"));
     }
     catch (Exception e)
     {
       e.printStackTrace();
     }
     return name;
   }

   public static boolean isSDCardVisable()
   {
     return Environment.getExternalStorageState().equals("mounted");
   }

   public static String getCacheDir()
   {
     if (!isSDCardVisable()) {
       return null;
     }
     String footDir = Environment.getExternalStorageDirectory().getAbsolutePath();
     String cachPath = footDir + File.separator + cache_dir;
     File file = new File(cachPath);
     file.mkdirs();
     return file.getAbsolutePath();
   }

   public static void downloadFile(final String localPath, String remotePath,final Handler handler)
   {
     Request request = new Builder().url(remotePath).build();
     new OkHttpClient().newCall(request).enqueue(new Callback()
     {
       public void onFailure(Call call, IOException e)
       {
         handler.sendEmptyMessage(0);
       }

       public void onResponse(Call call, Response response)
         throws IOException
       {
         InputStream is = null;
         byte[] buf = new byte[2048];
         int len = 0;
         FileOutputStream fos = null;
         try
         {
           is = response.body().byteStream();
           long total = response.body().contentLength();
           File file = new File(localPath);
           fos = new FileOutputStream(file);
           long sum = 0L;
           while ((len = is.read(buf)) != -1)
           {
             fos.write(buf, 0, len);
             sum += len;
             int progress = (int)((float)sum * 1.0F / (float)total * 100.0F);
             Message msg = Message.obtain();
             msg.what = 2;
             msg.arg1 = progress;
             handler.sendMessage(msg);
           }
           fos.flush();
           handler.sendEmptyMessage(1); return;
         }
         catch (Exception e)
         {
           handler.sendEmptyMessage(0);
         }
         finally
         {
           try
           {
             if (is != null) {
               is.close();
             }
           }
           catch (IOException localIOException4) {}
           try
           {
             if (fos != null) {
               fos.close();
             }
           }
           catch (IOException localIOException5) {}
         }
       }
     });
   }

   public static String readFile2String(String localFile)
   {
     String text = "";
     try
     {
       String encoding = "utf-8";
       File file = new File(localFile);
       if ((file.isFile()) && (file.exists()))
       {
         ByteArrayOutputStream outStream = new ByteArrayOutputStream();
         FileInputStream fis = new FileInputStream(file);
         byte[] buffer = new byte[1024];
         int length = 0;
         while ((length = fis.read(buffer)) != -1) {
           outStream.write(buffer, 0, length);
         }
         byte[] b = outStream.toByteArray();

         fis.close();
         outStream.flush();
         outStream.close();
         text = new String(b);
         b = null;
       }
       else
       {
         System.out.println("找不到指定的文件");
       }
     }
     catch (Exception e)
     {
       System.out.println("读取文件内容出错");
       e.printStackTrace();
     }
     return text;
   }
 }



