package com.zeyou.zeyousdklib.data.source;

import com.zeyou.zeyousdklib.data.PlaybackDocument;
import java.util.List;

public abstract interface PlaybackDocumentDataSource
{
  public abstract void getDocumentList(String paramString, LoadDocumentCallback paramLoadDocumentCallback);
  
  public static abstract interface LoadDocumentCallback
  {
    public abstract void onLoaded(String paramString, List<PlaybackDocument> paramList);
    
    public abstract void onDataNotAvailable(String paramString);
  }
}



