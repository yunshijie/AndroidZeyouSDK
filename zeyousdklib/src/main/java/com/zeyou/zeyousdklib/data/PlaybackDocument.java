package com.zeyou.zeyousdklib.data;

public class PlaybackDocument
{
  public double created_at;
  public String id;
  public String content;
  public String event;
  public String data;
  public PPT ppt;
  
  public static class PPT
  {
    public String type;
    public int totalPage;
    public String doc;
    public String uid;
    public int page;
  }
}



