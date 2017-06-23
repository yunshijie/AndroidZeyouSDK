 package com.zeyou.zeyousdklib.data.source;

 import com.zeyou.zeyousdklib.data.PlaybackDocument;
 import java.util.List;

 public class PlaybackDocumentRepository
   implements PlaybackDocumentDataSource
 {
   private static PlaybackDocumentRepository INSTANCE;
   private final PlaybackDocumentDataSource messageInfoLocalDataSource;
   private final PlaybackDocumentDataSource messageInfoRemoteDataSource;

   private PlaybackDocumentRepository(PlaybackDocumentDataSource messageInfoLocalDataSource, PlaybackDocumentDataSource messageInfoRemoteDataSource)
   {
     this.messageInfoLocalDataSource = messageInfoLocalDataSource;
     this.messageInfoRemoteDataSource = messageInfoRemoteDataSource;
   }

   public static PlaybackDocumentRepository getInstance(PlaybackDocumentDataSource messageInfoLocalDataSource, PlaybackDocumentDataSource messageInfoRemoteDataSource)
   {
     if (INSTANCE == null) {
       INSTANCE = new PlaybackDocumentRepository(messageInfoLocalDataSource, messageInfoRemoteDataSource);
     }
     return INSTANCE;
   }

   public void getDocumentList(String url, final LoadDocumentCallback callback)
   {
     this.messageInfoRemoteDataSource.getDocumentList(url, new LoadDocumentCallback()
     {
       public void onLoaded(String fileUrl, List<PlaybackDocument> msgInfo)
       {
         PlaybackDocumentRepository.this.messageInfoLocalDataSource.getDocumentList(fileUrl, callback);
       }

       public void onDataNotAvailable(String reason)
       {
         callback.onDataNotAvailable(reason);
       }
     });
   }
 }



