package com.zeyou.zeyousdklib.data.source.remote;

import android.os.Handler;
import android.os.Looper;


import com.zeyou.zeyousdklib.HttpDataSource;
import com.zeyou.zeyousdklib.ZeyouSDK;

import com.zeyou.zeyousdklib.data.WebinarInfo;


import com.zeyou.zeyousdklib.data.source.WebinarInfoDataSource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WebinarInfoRemoteDataSource
        implements WebinarInfoDataSource {
    private static final String TAG = WebinarInfoRemoteDataSource.class.getName();
    private static Handler mDelivery;
    private static WebinarInfoRemoteDataSource INSTANCE;

    public static WebinarInfoRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new WebinarInfoRemoteDataSource();
            mDelivery = new Handler(Looper.getMainLooper());
        }
        return INSTANCE;
    }

    public void getBroadcastWebinarInfo(String id, String access_token, String user_id, final WebinarInfoDataSource.LoadWebinarInfoCallback callback) {
        HashMap<String, String> map = new HashMap();
        map.put("id", id);
        map.put("access_token", access_token);
        map.put("user_id", user_id == null ? "" : user_id);
        HttpDataSource.post("webinar/start", "start", map, new Callback() {
            public void onFailure(Call call, final IOException e) {
                WebinarInfoRemoteDataSource.mDelivery.post(new Runnable() {
                    public void run() {
                        ZeyouSDK.ErrorCallback(callback, 257, e.getMessage());
                    }
                });
            }

            public void onResponse(Call call, final Response response)
                    throws IOException {
                final String resultStr = response.body().string();
                WebinarInfoRemoteDataSource.mDelivery.post(new Runnable() {
                    public void run() {
                        if (response.code() == 200) {
                            WebinarInfoRemoteDataSource.this.dealResponse(resultStr, callback);
                        } else {
                            ZeyouSDK.ErrorCallback(callback, response.code(), response.message());
                        }
                    }
                });
            }
        });
    }

    @Override
    public void heart(String userId, String roomId, String userType, String userName, String permission, final ZeyouSDK.RequestCallback callback) {

        HashMap<String, String> map = new HashMap();
        map.put("user_id", userId);
        map.put("room_id", roomId);
        map.put("user_type", userType);
        map.put("permission", permission);
        map.put("user_name", userName);

        HttpDataSource.post("heart/heart", "heart", map, new Callback() {
            public void onFailure(Call call, final IOException e) {
                WebinarInfoRemoteDataSource.mDelivery.post(new Runnable() {
                    public void run() {
                        ZeyouSDK.ErrorCallback(callback, 257, e.getMessage());
                    }
                });
            }

            public void onResponse(Call call, Response response)
                    throws IOException {
                if (callback == null) {
                    return;
                }
                final String resultStr = response.body().string();
                WebinarInfoRemoteDataSource.mDelivery.post(new Runnable() {
                    public void run() {
                        try {
                            JSONObject result = new JSONObject(resultStr);
                            String msg = result.optString("msg");
                            int code = result.optInt("code");
                            if (code == 200) {
                                callback.onSuccess();
                            } else {
                                ZeyouSDK.ErrorCallback(callback, code, msg);
                            }
                        } catch (JSONException e) {
                            ZeyouSDK.ErrorCallback(callback, 258, e.getMessage());
                            e.printStackTrace();
                        } catch (Exception e) {
                            ZeyouSDK.ErrorCallback(callback, 259, e.getMessage());
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    @Override
    public void cost(String userId, String roomId, String epiId, String userName, final ZeyouSDK.RequestCallback callback) {
        HashMap<String, String> map = new HashMap();
        map.put("user_id", userId);
        map.put("room_id", roomId);
        map.put("epi_id", epiId);
        map.put("user_name", userName);

        HttpDataSource.post("heart/cost", "cost", map, new Callback() {
            public void onFailure(Call call, final IOException e) {
                WebinarInfoRemoteDataSource.mDelivery.post(new Runnable() {
                    public void run() {
                        ZeyouSDK.ErrorCallback(callback, 257, e.getMessage());
                    }
                });
            }

            public void onResponse(Call call, Response response)
                    throws IOException {
                if (callback == null) {
                    return;
                }
                final String resultStr = response.body().string();
                WebinarInfoRemoteDataSource.mDelivery.post(new Runnable() {
                    public void run() {
                        try {
                            JSONObject result = new JSONObject(resultStr);
                            String msg = result.optString("msg");
                            int code = result.optInt("code");
                            if (code == 200) {
                                callback.onSuccess();
                            } else {
                                ZeyouSDK.ErrorCallback(callback, code, msg);
                            }
                        } catch (JSONException e) {
                            ZeyouSDK.ErrorCallback(callback, 258, e.getMessage());
                            e.printStackTrace();
                        } catch (Exception e) {
                            ZeyouSDK.ErrorCallback(callback, 259, e.getMessage());
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    @Override
    public void knockDoor(String userId, String roomId, String epiId, String userName, String msg, final ZeyouSDK.RequestCallback callback) {

        HashMap<String, String> map = new HashMap();
        map.put("user_id", userId);
        map.put("room_id", roomId);
        map.put("epi_id", epiId);
        map.put("msg", msg);
        map.put("user_name", userName);

        HttpDataSource.post("webinar/knockDoor", "knockDoor", map, new Callback() {
            public void onFailure(Call call, final IOException e) {
                WebinarInfoRemoteDataSource.mDelivery.post(new Runnable() {
                    public void run() {
                        ZeyouSDK.ErrorCallback(callback, 257, e.getMessage());
                    }
                });
            }

            public void onResponse(Call call, Response response)
                    throws IOException {
                if (callback == null) {
                    return;
                }
                final String resultStr = response.body().string();
                WebinarInfoRemoteDataSource.mDelivery.post(new Runnable() {
                    public void run() {
                        try {
                            JSONObject result = new JSONObject(resultStr);
                            String msg = result.optString("msg");
                            int code = result.optInt("code");
                            if (code == 200) {
                                callback.onSuccess();
                            } else {
                                ZeyouSDK.ErrorCallback(callback, code, msg);
                            }
                        } catch (JSONException e) {
                            ZeyouSDK.ErrorCallback(callback, 258, e.getMessage());
                            e.printStackTrace();
                        } catch (Exception e) {
                            ZeyouSDK.ErrorCallback(callback, 259, e.getMessage());
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    @Override
    public void doorState(String userId, String roomId, String epiId, final ZeyouSDK.RequestCallback callback) {

        HashMap<String, String> map = new HashMap();
        map.put("user_id", userId);
        map.put("room_id", roomId);
        map.put("epi_id", epiId);

        HttpDataSource.post("webinar/doorState", "doorState", map, new Callback() {
            public void onFailure(Call call, final IOException e) {
                WebinarInfoRemoteDataSource.mDelivery.post(new Runnable() {
                    public void run() {
                        ZeyouSDK.ErrorCallback(callback, 257, e.getMessage());
                    }
                });
            }

            public void onResponse(Call call, Response response)
                    throws IOException {
                if (callback == null) {
                    return;
                }
                final String resultStr = response.body().string();
                WebinarInfoRemoteDataSource.mDelivery.post(new Runnable() {
                    public void run() {
                        try {
                            JSONObject result = new JSONObject(resultStr);
                            String msg = result.optString("msg");
                            int code = result.optInt("code");
                            if (code == 200) {
                                callback.onSuccess();
                            } else {
                                ZeyouSDK.ErrorCallback(callback, code, msg);
                            }
                        } catch (JSONException e) {
                            ZeyouSDK.ErrorCallback(callback, 258, e.getMessage());
                            e.printStackTrace();
                        } catch (Exception e) {
                            ZeyouSDK.ErrorCallback(callback, 259, e.getMessage());
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    public void stopBroadcast(String id, String access_token, final ZeyouSDK.RequestCallback callback) {
        HashMap<String, String> map = new HashMap();
        map.put("access_token", access_token);
        map.put("id", id);
        HttpDataSource.post("webinar/stop", "stop", map, new Callback() {
            public void onFailure(Call call, final IOException e) {
                WebinarInfoRemoteDataSource.mDelivery.post(new Runnable() {
                    public void run() {
                        ZeyouSDK.ErrorCallback(callback, 257, e.getMessage());
                    }
                });
            }

            public void onResponse(Call call, Response response)
                    throws IOException {
                if (callback == null) {
                    return;
                }
                final String resultStr = response.body().string();
                WebinarInfoRemoteDataSource.mDelivery.post(new Runnable() {
                    public void run() {
                        try {
                            JSONObject result = new JSONObject(resultStr);
                            String msg = result.optString("msg");
                            int code = result.optInt("code");
                            if (code == 200) {
                                callback.onSuccess();
                            } else {
                                ZeyouSDK.ErrorCallback(callback, code, msg);
                            }
                        } catch (JSONException e) {
                            ZeyouSDK.ErrorCallback(callback, 258, e.getMessage());
                            e.printStackTrace();
                        } catch (Exception e) {
                            ZeyouSDK.ErrorCallback(callback, 259, e.getMessage());
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    public void submitLotteryInfo(String join_id, String lottery_id, String name, String phone, final ZeyouSDK.RequestCallback callback) {
        HashMap<String, String> map = new HashMap();
        map.put("join_id", join_id);
        map.put("lottery_id", lottery_id);
        map.put("name", name);
        map.put("phone", phone);
        HttpDataSource.post("webinar/setlotteryinfo", "setlotteryinfo", map, new Callback() {
            public void onFailure(Call call, final IOException e) {
                WebinarInfoRemoteDataSource.mDelivery.post(new Runnable() {
                    public void run() {
                        ZeyouSDK.ErrorCallback(callback, 257, e.getMessage());
                    }
                });
            }

            public void onResponse(Call call, Response response)
                    throws IOException {
                if (callback == null) {
                    return;
                }
                final String resultStr = response.body().string();
                WebinarInfoRemoteDataSource.mDelivery.post(new Runnable() {
                    public void run() {
                        try {
                            JSONObject result = new JSONObject(resultStr);
                            String msg = result.optString("msg");
                            int code = result.optInt("code");
                            if (code == 200) {
                                callback.onSuccess();
                            } else {
                                ZeyouSDK.ErrorCallback(callback, code, msg);
                            }
                        } catch (JSONException e) {
                            ZeyouSDK.ErrorCallback(callback, 258, e.getMessage());
                            e.printStackTrace();
                        } catch (Exception e) {
                            ZeyouSDK.ErrorCallback(callback, 259, e.getMessage());
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }


    public void performSignIn(String webinarId, String userId, String name, String signId, final ZeyouSDK.RequestCallback callback) {
        HashMap<String, String> map = new HashMap();
        map.put("webinar_id", webinarId);
        map.put("user_id", userId);
        map.put("name", name);
        map.put("sign_id", signId);
        HttpDataSource.post("webinar/sign-record", "sign-record", map, new Callback() {
            public void onFailure(Call call, final IOException e) {
                WebinarInfoRemoteDataSource.mDelivery.post(new Runnable() {
                    public void run() {
                        ZeyouSDK.ErrorCallback(callback, 257, e.getMessage());
                    }
                });
            }

            public void onResponse(Call call, final Response response)
                    throws IOException {
                if (callback == null) {
                    return;
                }
                final String resultStr = response.body().string();
                WebinarInfoRemoteDataSource.mDelivery.post(new Runnable() {
                    public void run() {
                        if (response.code() == 200) {
                            WebinarInfoRemoteDataSource.this.dealSignInData(resultStr, callback);
                        } else {
                            ZeyouSDK.ErrorCallback(callback, response.code(), response.message());
                        }
                    }
                });
            }
        });
    }

    public void getWatchWebinarInfo(String id, String name, String email, String password, final String zeyouId, String record_id,
                                    String roomPwd, String userType, String ip, String system,String addr,String src,final WebinarInfoDataSource.LoadWebinarInfoCallback callback) {
        HashMap<String, String> map = new HashMap();
//     map.put("id", id);
//     map.put("name", name);
//     map.put("email", email);
//     map.put("record_id", record_id);
        map.put("pass", password == null ? "" : password);
        map.put("user_id", zeyouId == null ? "" : zeyouId);
        map.put("room_id", id);

        map.put("room_pass", roomPwd);
        map.put("user_name", name);
        map.put("user_type", userType);
        map.put("ip", ip);
        map.put("source", src);
        map.put("system", system);
        map.put("addr", addr);

        HttpDataSource.post("webinar/watch", "watch", map, new Callback() {
            public void onFailure(Call call, final IOException e) {
                WebinarInfoRemoteDataSource.mDelivery.post(new Runnable() {
                    public void run() {
                        ZeyouSDK.ErrorCallback(callback, 257, e.getMessage());
                    }
                });
            }

            public void onResponse(Call call, final Response response)
                    throws IOException {
                final String resultStr = response.body().string();
                WebinarInfoRemoteDataSource.mDelivery.post(new Runnable() {
                    public void run() {
                        if (response.code() == 200) {
                            WebinarInfoRemoteDataSource.this.dealWatchRespone(zeyouId, resultStr, callback);
                        } else {
                            ZeyouSDK.ErrorCallback(callback, response.code(), response.message());
                        }
                    }
                });
            }
        });
    }

    private void dealResponse(String responseStr, WebinarInfoDataSource.LoadWebinarInfoCallback callback) {
        try {
            JSONObject result = new JSONObject(responseStr);
            String msg = result.optString("msg");
            int code = result.optInt("code");
            if (code == 200) {
                JSONObject dataobj = result.getJSONObject("data");
                WebinarInfo webinarInfo = new WebinarInfo();
                webinarInfo.webinar_id = dataobj.optString("webinar_id");
                webinarInfo.media_srv = dataobj.optString("media_srv");
                webinarInfo.streamtoken = dataobj.optString("streamtoken");
                webinarInfo.msg_token = dataobj.optString("msg_token");
                webinarInfo.msg_server = dataobj.optString("msg_server");
                webinarInfo.chat_server = dataobj.optString("chat_server");
                webinarInfo.publish_server = dataobj.optString("publish_server");
                webinarInfo.join_id = dataobj.optString("join_id");
                callback.onWebinarInfoLoaded(responseStr, webinarInfo);
            } else {
                ZeyouSDK.ErrorCallback(callback, code, msg);
            }
        } catch (JSONException e) {
            ZeyouSDK.ErrorCallback(callback, 258, e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            ZeyouSDK.ErrorCallback(callback, 259, e.getMessage());
            e.printStackTrace();
        }
    }

    private void dealWatchRespone(String user_id, String responseStr, WebinarInfoDataSource.LoadWebinarInfoCallback callback) {
        try {
            JSONObject obj = new JSONObject(responseStr);
            String msg = obj.optString("msg");
            int code = obj.optInt("code");
            if (code == 200) {
                JSONObject dataResult = obj.optJSONObject("data");
                WebinarInfo webinarInfo = new WebinarInfo();

                webinarInfo.rtmp_video = dataResult.optString("rtmp_video");
                webinarInfo.user_id = user_id;

                webinarInfo.webinar_id = dataResult.optString("webinar_id");
                webinarInfo.msg_token = dataResult.optString("msg_token");
                webinarInfo.host = dataResult.optString("host");
                webinarInfo.layout = dataResult.optInt("layout");
                webinarInfo.buffer = dataResult.optInt("buffer");
                webinarInfo.video = dataResult.optString("video");
                webinarInfo.docurl = dataResult.optString("docurl");
                webinarInfo.status = dataResult.optInt("status");
                webinarInfo.page = dataResult.optInt("page");
                webinarInfo.doc = dataResult.optString("doc");
                webinarInfo.join_id = dataResult.optString("join_id");
                webinarInfo.msg_server = dataResult.optString("msg_server");
                webinarInfo.chat_server = dataResult.optString("chat_server");
                webinarInfo.chat_token = dataResult.optString("chat_token");
                webinarInfo.publish_server = dataResult.optString("publish_server");
                webinarInfo.session_id = dataResult.optString("session_id");
                WebinarInfo.Notice notice = new WebinarInfo.Notice();
                JSONObject noticeObj = dataResult.optJSONObject("notice");
                if (noticeObj != null) {
                    notice.content = noticeObj.optString("content");
                    notice.publish_release_time = noticeObj.optString("publish_release_time");
                }
                webinarInfo.notice = notice;


                webinarInfo.room_id = dataResult.optInt("room_id");
                webinarInfo.epi_id = dataResult.optInt("epi_id");
                webinarInfo.permission = dataResult.optInt("permission");
                webinarInfo.costtime1 = dataResult.optInt("costtime1");
                webinarInfo.costtime2 = dataResult.optInt("costtime2");
                webinarInfo.heart = dataResult.optInt("heart");
                webinarInfo.default_video = dataResult.optString("default_video");
                webinarInfo.definition = dataResult.optInt("definition");

                JSONObject StyleSDResult = dataResult.optJSONObject("SD");
                webinarInfo.SD.value = StyleSDResult.optString("value");
                webinarInfo.SD.valid = StyleSDResult.optInt("valid");

                JSONObject StyleHDResult = dataResult.optJSONObject("HD");
                webinarInfo.HD.value = StyleHDResult.optString("value");
                webinarInfo.HD.valid = StyleHDResult.optInt("valid");

                webinarInfo.need_cost = dataResult.optInt("need_cost");

                callback.onWebinarInfoLoaded(responseStr, webinarInfo);
            } else {
                ZeyouSDK.ErrorCallback(callback, code, msg);
            }
        } catch (JSONException e) {
            ZeyouSDK.ErrorCallback(callback, 258, e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            ZeyouSDK.ErrorCallback(callback, 259, e.getMessage());
            e.printStackTrace();
        }
    }

    private void dealSignInData(String jsonStr, ZeyouSDK.RequestCallback callback) {
        JSONObject data = null;
        try {
            data = new JSONObject(jsonStr);
            String msg = data.optString("msg");
            int code = data.optInt("code");
            if (code == 200) {
                callback.onSuccess();
            } else {
                ZeyouSDK.ErrorCallback(callback, code, msg);
            }
        } catch (Exception e) {
            ZeyouSDK.ErrorCallback(callback, 259, e.getMessage());
        }
    }

}



