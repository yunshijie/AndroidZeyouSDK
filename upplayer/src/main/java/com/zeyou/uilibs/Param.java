package com.zeyou.uilibs;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.telephony.TelephonyManager;

import com.zeyou.upplayer.R;

import java.io.Serializable;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.TELEPHONY_SERVICE;

/**
 * 直播参数类
 */
public class Param implements Serializable {

    //发直播相关
    public String broId = "";
    public String broToken = "";
    //    public int pixel_type = CameraFilterView.TYPE_HDPI;
    public int videoBitrate = 500;
    public int videoFrameRate = 20;
    //看直播相关
    public String watchId = "";
    public String key = "";
    public int bufferSecond = 2;

    //用户相关
    public String userZeyouId = "";
    public String userCustomId = "";
    public String userName = "";
    public String userAvatar = "";
    public int user_type = 0;
    public String ip = "";
    public String addr = "";
    public String roomPwd = "";
    public String sys = "Android";
    public String src = "Android " + Build.VERSION.SDK_INT;

    public static Param getParam(Context context) {

        Param param = new Param();
        SharedPreferences sp = context.getSharedPreferences("set", MODE_PRIVATE);
        TelephonyManager telephonyMgr = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
        param.broId = sp.getString("broid", "526390495");
        param.broToken = sp.getString("brotoken", "afdf316cc729d070e0e4371976881b32");
//            param.pixel_type = sp.getInt("pixeltype", CameraFilterView.TYPE_HDPI);
        param.videoBitrate = sp.getInt("videobitrate", 500);
        param.videoFrameRate = sp.getInt("videoframerate", 20);

        param.watchId = sp.getString("watchid", "100");
        param.key = sp.getString("key", "");
        param.bufferSecond = sp.getInt("buffersecond", 2);

        param.userZeyouId = sp.getString("userzeyouid", "");
        param.userCustomId = sp.getString("usercustomid", telephonyMgr.getDeviceId());
        param.userName = sp.getString("username", Build.BRAND + context.getString(R.string.phone_user));
        param.userAvatar = sp.getString("useravatar", "");

        param.user_type = sp.getInt("user_type", param.user_type);
        param.ip = sp.getString("ip", param.ip);
        param.addr = sp.getString("addr", param.addr);
        param.sys = sp.getString("sys", param.sys);
        param.roomPwd = sp.getString("roomPwd", param.roomPwd);

        return param;
    }

    public static void setParam(Context context, Param param) {
        if (param == null)
            return;
        SharedPreferences sp = context.getSharedPreferences("set", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("broid", param.broId);
        editor.putString("brotoken", param.broToken);
//        editor.putInt("pixeltype", param.pixel_type);
        editor.putInt("videobitrate", param.videoBitrate);
        editor.putInt("videoframerate", param.videoFrameRate);

        editor.putString("watchid", param.watchId);
        editor.putString("key", param.key);
        editor.putInt("buffersecond", param.bufferSecond);


        editor.putString("userzeyouid", param.userZeyouId);
        editor.putString("usercustomid", param.userCustomId);
        editor.putString("username", param.userName);
        editor.putString("useravatar", param.userAvatar);

        editor.putInt("user_type", param.user_type);
        editor.putString("ip", param.ip);
        editor.putString("addr", param.addr);
        editor.putString("sys", param.sys);
        editor.putString("roomPwd", param.roomPwd);

        editor.commit();

    }
}
