 package com.zeyou.zeyousdklib.data.source;



import com.zeyou.zeyousdklib.ZeyouSDK;


 public class UserInfoRepository
   implements UserInfoDataSource
 {
   private static UserInfoRepository INSTANCE;
   private final UserInfoDataSource userInfoRemoteDataSource;

   private UserInfoRepository(UserInfoDataSource userInfoRemoteDataSource)
   {
     this.userInfoRemoteDataSource = userInfoRemoteDataSource;
   }

   public static UserInfoRepository getInstance(UserInfoDataSource userInfoRemoteDataSource)
   {
     if (INSTANCE == null) {
       INSTANCE = new UserInfoRepository(userInfoRemoteDataSource);
     }
     return INSTANCE;
   }

   public void getUserInfo(String username, String userpass, UserInfoCallback callback)
   {
     this.userInfoRemoteDataSource.getUserInfo(username, userpass, callback);
   }

   public void getVisitInfo(String userId, UserInfoCallback callback)
   {
     this.userInfoRemoteDataSource.getVisitInfo(userId, callback);
   }


   public void sendChat(String url, String token, String content, ZeyouSDK.RequestCallback callback)
   {
     this.userInfoRemoteDataSource.sendChat(url, token, content, callback);
   }

   public void sendComment(String webinar_id, String content, String user_id, ZeyouSDK.RequestCallback callback)
   {
     this.userInfoRemoteDataSource.sendComment(webinar_id, content, user_id, callback);
   }

   public void sendQuestion(String user_id, String webinar_id, String content, ZeyouSDK.RequestCallback callback)
   {
     this.userInfoRemoteDataSource.sendQuestion(user_id, webinar_id, content, callback);
   }
 }



