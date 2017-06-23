package com.zeyou.zeyousdklib.utils;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.zeyou.zeyousdklib.ZeyouSDK;
import com.zeyou.zeyousdklib.data.WebinarInfo;
import com.zeyou.zeyousdklib.widget.PlayView;

import java.util.Set;

/**
 * 本地存储观看时间，开启定时器。10s的观看时间统计计时器；心跳计时器；最短观看时间
 * Created by Administrator on 2017/6/10.
 */

public class CostUtil {


    private Handler handler;
    private WebinarInfo info;

    private int heart;
    private int minTime;
    private int maxTime;
    private int needCost;
    private Param param;
    private PlayView player;
    private boolean isNoNeedCost = false;
    private Context context;
    private int intervalRecord = 10;
    private boolean isFirstCost = true;


    public static class Param {
        public String roomId;
        public String userId;
        public String userName;
        public String userType;
        public int permission;

        public Param() {
        }

        public Param(String roomId, String userId, String userName, String userType) {
            this.roomId = roomId;
            this.userId = userId;
            this.userName = userName;
            this.userType = userType;
        }
    }

    public CostUtil(Handler handler, WebinarInfo info, Param param, PlayView player) {
        this.handler = handler;
        this.info = info;
        this.param = param;
        this.player = player;

        needCost = info.need_cost;
        minTime = info.costtime1;
        maxTime = info.costtime2;
        heart = info.heart > 0 ? info.heart : 1;
        param.permission = info.permission;

        context = ZeyouSDK.getInstance().getContext();
        init();
    }

    public void setParam(Param param) {
        this.param = param;
    }

    private Runnable runHeart = new Runnable() {
        @Override
        public void run() {
            Log.i("扣费", "心跳");
            ZeyouSDK.getInstance().heart(param.userId, param.roomId, param.userType, param.userName, param.permission + "", null);
        }
    };

    private class LocalRecord {
        public String userId;
        public String roomId;
        //上次记录时间
        public long lastTime;
        public int period;
    }

    private LocalRecord getFromLocal(String userId, String roomId) {
        LocalRecord record = null;
        SharedPreferences sp = context.getSharedPreferences("LocalRecord", Context.MODE_APPEND);
        SharedPreferences.Editor editor = sp.edit();

        String key = roomId + ":" + userId;
        String data = sp.getString(key, null);
        if (!TextUtils.isEmpty(data)) {
            String[] array = data.split(":");
            if (array.length == 2) {

                long lastTime = Long.parseLong(array[0]);
                if (System.currentTimeMillis() - lastTime >= 60 * 60 * 1000) {
                    editor.remove(key);
                } else {

                    record = new LocalRecord();
                    record.lastTime = lastTime;
                    record.period = Integer.parseInt(array[1]);
                }
            } else {
                editor.remove(key);
            }

            editor.commit();
        }

        return record;
    }

    private void saveLocal(String userId, String roomId, int period) {
        SharedPreferences sp = context.getSharedPreferences("LocalRecord", Context.MODE_APPEND);
        SharedPreferences.Editor editor = sp.edit();

        editor.putString(roomId + ":" + userId, System.currentTimeMillis() + ":" + period);
        editor.commit();
    }

    private Runnable runRecordTime = new Runnable() {
        @Override
        public void run() {
            //本地存储格式：key(roomid:userid):(lasttime:period)
//lasttime>1小时删除，period大于扣费间隔则扣费
//    获取房间信息成功后，发送心跳信息；记录播放时长；最短观看时间后扣费
            if (player.isPlaying()) {
                LocalRecord record = getFromLocal(param.userId, param.roomId);
                int period = intervalRecord;
                if (record != null) {
                    period += record.period;
                }

                if (period >= maxTime) {
                    runCost.run();
                    period = 0;
                }
                Log.i("扣费", "计时" + period);
                saveLocal(param.userId, param.roomId, period);
            }

            if (!isNoNeedCost) {
                handler.removeCallbacks(runRecordTime);
                handler.postDelayed(runRecordTime, intervalRecord * 1000);
            }
        }
    };

    private Runnable runCost = new Runnable() {
        @Override
        public void run() {

            if (!player.isPlaying()) {
                return;
            }

            Log.i("扣费", "开始扣费");
            ZeyouSDK.getInstance().cost(param.userId, param.roomId, info.epi_id + "", param.userName, new ZeyouSDK.RequestCallback() {
                @Override
                public void onSuccess() {
                    Log.i("扣费", "扣费成功");
                }

                @Override
                public void onError(int paramInt, String paramString) {
                    if (paramInt == ErrorCode.COST_NO_NEED) {
                        isNoNeedCost = true;
                    }
                }
            });
        }
    };

    public void init() {
        handler.removeCallbacks(runHeart);
        handler.postDelayed(runHeart, heart * 60 * 1000);
    }

    public void firstCost() {
        if (isFirstCost && maxTime > 0 && needCost == 1) {
            isFirstCost = false;
            handler.removeCallbacks(runCost);
            handler.removeCallbacks(runRecordTime);

            handler.postDelayed(runCost, minTime * 1000);
            handler.postDelayed(runRecordTime, intervalRecord * 1000);
        }
    }

    public void removeCallbacks() {

        Log.i("扣费", "清理扣费相关线程");
        handler.removeCallbacks(runRecordTime);
        handler.removeCallbacks(runCost);
        handler.removeCallbacks(runHeart);
    }

}
