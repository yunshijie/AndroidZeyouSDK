 package com.zeyou.zeyousdklib;

 import android.os.Handler;
 import android.os.Looper;
 import com.zeyou.zeyousdklib.data.PlaybackDocument;
 import com.zeyou.zeyousdklib.data.PlaybackDocument.PPT;
 import com.zeyou.zeyousdklib.data.WebinarInfo;

 import com.zeyou.zeyousdklib.data.source.PlaybackDocumentDataSource;
import com.zeyou.zeyousdklib.data.source.PlaybackDocumentRepository;
 import com.zeyou.zeyousdklib.data.source.local.PlaybackDocumentLocalDataSource;
 import com.zeyou.zeyousdklib.data.source.remote.PlaybackDocumentRemoteDataSource;
 import java.util.List;

 public class ZeyouPPT
 {
   private static final String TAG = "ZeyouPPT";
   private WebinarInfo webinarInfo;
   private List<PlaybackDocument> pptList;
   private Handler mDelivery = null;

   public ZeyouPPT()
   {
     this.mDelivery = new Handler(Looper.getMainLooper());
   }

   public void setWebinarInfo(WebinarInfo webinarInfo)
   {
     this.webinarInfo = webinarInfo;
   }

   private int position = 0;
   private static PPT ppt;
   private static final String imgUrlFormat = "%s/%s/%d.jpg";

   public String getPPT(long currentPos)
   {
     if (this.webinarInfo == null) {
       return null;
     }
     if ((this.pptList == null) || (this.pptList.size() <= 0)) {
       return null;
     }
     for (int i = 0; i < this.pptList.size(); i++)
     {
       if (currentPos <= ((PlaybackDocument)this.pptList.get(i)).created_at)
       {
         this.position = i;
         break;
       }
       this.position = this.pptList.size();
     }
     if (this.position == 0)
     {
       if (((PlaybackDocument)this.pptList.get(0)).created_at > currentPos) {
         ppt = null;
       } else {
         ppt = ((PlaybackDocument)this.pptList.get(0)).ppt;
       }
     }
     else {
       ppt = ((PlaybackDocument)this.pptList.get(this.position - 1)).ppt;
     }
     if (ppt != null) {
       return url(this.webinarInfo.host, ppt.doc, ppt.page);
     }
     return null;
   }

   void initPPT()
   {
     if (this.webinarInfo == null) {
       return;
     }
     PlaybackDocumentRepository repository = PlaybackDocumentRepository.getInstance(PlaybackDocumentLocalDataSource.getInstance(), PlaybackDocumentRemoteDataSource.getInstance());
     repository.getDocumentList(this.webinarInfo.docurl, new PlaybackDocumentDataSource.LoadDocumentCallback()
     {
       public void onLoaded(String fileUrl, List<PlaybackDocument> msgInfo)
       {
         ZeyouPPT.this.pptList = msgInfo;
       }

       public void onDataNotAvailable(String reason) {}
     });
   }

   private void clearList()
   {
     if (this.pptList != null) {
       this.pptList.clear();
     }
   }

   void destory()
   {
     clearList();
     this.webinarInfo = null;
   }

   public static String url(String host, String doc, int page)
   {
     return String.format("%s/%s/%d.jpg", new Object[] { host, doc, Integer.valueOf(page) });
   }

   public static abstract interface PPTChangeCallback
   {
     public abstract void onPPTChange(String paramString);
   }
 }



