package com.zeyou.playdemo;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.telephony.TelephonyManager;

import com.zeyou.uilibs.Param;
import com.zeyou.zeyousdklib.ZeyouSDK;

/**
 * 主Application类
 */
public class ZeyouApplication extends Application {


    public static Param param;
    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        getParam();
        ZeyouSDK.init(this, getResources().getString(R.string.zeyou_app_key), getResources().getString(R.string.zeyou_app_secret_key));
        ZeyouSDK.setLogEnable(true);
    }

    public static Param getParam() {

        param = Param.getParam(context);
        return param;
    }

    public static void setParam(Param mParam) {
        if (param == null)
            return;
        param = mParam;
        Param.setParam(context, param);
    }
}
