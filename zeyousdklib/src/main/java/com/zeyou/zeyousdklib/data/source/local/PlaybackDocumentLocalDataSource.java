 package com.zeyou.zeyousdklib.data.source.local;

 import android.text.TextUtils;
 import com.zeyou.zeyousdklib.data.PlaybackDocument;

 import com.zeyou.zeyousdklib.data.source.PlaybackDocumentDataSource;

 import com.zeyou.zeyousdklib.utils.FileUtil;
 import java.util.ArrayList;
 import java.util.List;
 import org.json.JSONArray;
 import org.json.JSONException;
 import org.json.JSONObject;

 public class PlaybackDocumentLocalDataSource
   implements PlaybackDocumentDataSource
 {
   private static PlaybackDocumentLocalDataSource INSTANCE;

   public static PlaybackDocumentLocalDataSource getInstance()
   {
     if (INSTANCE == null) {
       INSTANCE = new PlaybackDocumentLocalDataSource();
     }
     return INSTANCE;
   }

   public void getDocumentList(String url, PlaybackDocumentDataSource.LoadDocumentCallback callback)
   {
     String text = FileUtil.readFile2String(url);
     if (TextUtils.isEmpty(text))
     {
       callback.onDataNotAvailable("无内容！");
       return;
     }
     if (text.startsWith("﻿")) {
       text = text.substring(1);
     }
     List<PlaybackDocument> list = new ArrayList();
     try
     {
       JSONObject jsonObject = new JSONObject(text);
       JSONArray jsonArray = jsonObject.getJSONArray("cuepoint");
       if (jsonArray != null)
       {
         for (int i = 0; i < jsonArray.length(); i++)
         {
           JSONObject obj = (JSONObject)jsonArray.get(i);
           String event = obj.optString("event");
           if (event.equals("flashMsg"))
           {
             PlaybackDocument msgInfo = new PlaybackDocument();
             msgInfo.created_at = obj.optDouble("created_at");
             msgInfo.data = obj.optString("data");
             msgInfo.event = event;
             msgInfo.id = obj.optString("id");
             msgInfo.content = obj.optString("content");

             JSONObject pptjson = new JSONObject(msgInfo.content);
             PlaybackDocument.PPT p = new PlaybackDocument.PPT();
             p.doc = pptjson.optString("doc");
             p.page = pptjson.optInt("page");
             p.totalPage = pptjson.optInt("totalPage");
             msgInfo.ppt = p;
             list.add(msgInfo);
           }
         }
         text = null;
         callback.onLoaded(null, list);
       }
       else
       {
         callback.onDataNotAvailable("无内容！");
       }
     }
     catch (JSONException e)
     {
       e.printStackTrace();
     }
     catch (Exception e)
     {
       e.printStackTrace();
     }
   }
 }



