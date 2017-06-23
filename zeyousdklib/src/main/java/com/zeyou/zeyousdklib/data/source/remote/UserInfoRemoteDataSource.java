package com.zeyou.zeyousdklib.data.source.remote;

import android.os.Handler;
import android.os.Looper;

import com.zeyou.zeyousdklib.HttpDataSource;
import com.zeyou.zeyousdklib.ZeyouSDK;

import com.zeyou.zeyousdklib.data.UserInfo;
import com.zeyou.zeyousdklib.data.source.UserInfoDataSource;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;

import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

import org.json.JSONException;
import org.json.JSONObject;

public class UserInfoRemoteDataSource
        implements UserInfoDataSource {
    private static UserInfoRemoteDataSource INSTANCE;
    private static Handler mDelivery;

    public static UserInfoRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UserInfoRemoteDataSource();
            mDelivery = new Handler(Looper.getMainLooper());
        }
        return INSTANCE;
    }

    public void getVisitInfo(String userId, final UserInfoDataSource.UserInfoCallback callback) {
        HashMap<String, String> map = new HashMap();
        map.put("account", userId);
        HttpDataSource.post("user/visit", "visit", map, new Callback() {
            public void onFailure(Call call, final IOException e) {
                UserInfoRemoteDataSource.mDelivery.post(new Runnable() {
                    public void run() {
                        ZeyouSDK.ErrorCallback(callback, 257, e.getMessage());
                    }
                });
            }

            public void onResponse(Call call, final Response response)
                    throws IOException {
                final String resultStr = response.body().string();
                UserInfoRemoteDataSource.mDelivery.post(new Runnable() {
                    public void run() {
                        if (response.code() == 200) {
                            UserInfoRemoteDataSource.this.dealLoginResponse(resultStr, callback);
                        } else {
                            ZeyouSDK.ErrorCallback(callback, response.code(), response.message());
                        }
                    }
                });
            }
        });
    }

    public void getUserInfo(String username, String userpass, final UserInfoDataSource.UserInfoCallback callback) {
        HashMap<String, String> map = new HashMap();
        map.put("account", username);
        map.put("password", userpass);
        HttpDataSource.post("user/login", "login", map, new Callback() {
            public void onFailure(Call call, final IOException e) {
                UserInfoRemoteDataSource.mDelivery.post(new Runnable() {
                    public void run() {
                        ZeyouSDK.ErrorCallback(callback, 257, e.getMessage());
                    }
                });
            }

            public void onResponse(Call call, final Response response)
                    throws IOException {
                final String resultStr = response.body().string();
                UserInfoRemoteDataSource.mDelivery.post(new Runnable() {
                    public void run() {
                        if (response.code() == 200) {
                            UserInfoRemoteDataSource.this.dealLoginResponse(resultStr, callback);
                        } else {
                            ZeyouSDK.ErrorCallback(callback, response.code(), response.message());
                        }
                    }
                });
            }
        });
    }

    public void sendChat(String url, String token, String content, final ZeyouSDK.RequestCallback callback) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("text", content);
            jsonObject.put("avatar", "");
            jsonObject.put("nick_name", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = new FormBody.Builder().add("token", token).add("event", "msg").add("data", jsonObject.toString()).build();
        HttpDataSource.post(url, body, new Callback() {
            public void onFailure(Call call, final IOException e) {
                UserInfoRemoteDataSource.mDelivery.post(new Runnable() {
                    public void run() {
                        ZeyouSDK.ErrorCallback(callback, 257, e.getMessage());
                    }
                });
            }

            public void onResponse(Call call, Response response)
                    throws IOException {
                UserInfoRemoteDataSource.mDelivery.post(new Runnable() {
                    public void run() {
                        callback.onSuccess();
                    }
                });
            }
        });
    }

    public void sendComment(String webinar_id, String content, String user_id, final ZeyouSDK.RequestCallback callback) {
        HashMap<String, String> map = new HashMap();
        map.put("webinar_id", webinar_id);
        map.put("content", content);
        map.put("user_id", user_id);
        HttpDataSource.post("user/send-comment", "send-comment", map, new Callback() {
            public void onFailure(Call call, final IOException e) {
                UserInfoRemoteDataSource.mDelivery.post(new Runnable() {
                    public void run() {
                        ZeyouSDK.ErrorCallback(callback, 257, e.getMessage());
                    }
                });
            }

            public void onResponse(Call call, final Response response)
                    throws IOException {
                final String resultStr = response.body().string();
                UserInfoRemoteDataSource.mDelivery.post(new Runnable() {
                    public void run() {
                        if (response.code() == 200) {
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
                        } else {
                            ZeyouSDK.ErrorCallback(callback, response.code(), response.message());
                        }
                    }
                });
            }
        });
    }

    public void sendQuestion(String user_id, String webinar_id, String content, final ZeyouSDK.RequestCallback callback) {
        HashMap<String, String> map = new HashMap();
        map.put("user_id", user_id);
        map.put("webinar_id", webinar_id);
        map.put("content", content);
        HttpDataSource.post("question/addques", "addques", map, new Callback() {
            public void onFailure(Call call, final IOException e) {
                UserInfoRemoteDataSource.mDelivery.post(new Runnable() {
                    public void run() {
                        ZeyouSDK.ErrorCallback(callback, 257, e.getMessage());
                    }
                });
            }

            public void onResponse(Call call, final Response response)
                    throws IOException {
                final String resultStr = response.body().string();
                if (callback != null) {
                    UserInfoRemoteDataSource.mDelivery.post(new Runnable() {
                        public void run() {
                            if (response.code() == 200) {
                                UserInfoRemoteDataSource.this.dealQuestionResult(resultStr, callback);
                            } else {
                                ZeyouSDK.ErrorCallback(callback, response.code(), response.message());
                            }
                        }
                    });
                }
            }
        });
    }

    private void dealLoginResponse(String responseStr, UserInfoDataSource.UserInfoCallback callback) {
        if (callback == null) {
            return;
        }
        try {
            JSONObject result = new JSONObject(responseStr);
            String msg = result.optString("msg");
            int code = result.optInt("code");
            if (code == 200) {
                JSONObject data = result.getJSONObject("data");
                UserInfo userInfo = new UserInfo();
                userInfo.user_id = data.optString("user_id");
                userInfo.account = data.optString("account");
                userInfo.avatar = data.optString("avatar");
                userInfo.nick_name = data.optString("nick_name");
                userInfo.user_type = data.optInt("user_type");
                userInfo.ip = data.optString("ip");
                userInfo.sys = "Android SDK";
                userInfo.addr = data.optString("addr");
                callback.onSuccess(userInfo);
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

    private void dealQuestionResult(String responseStr, ZeyouSDK.RequestCallback callback) {
        try {
            JSONObject result = new JSONObject(responseStr);
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
}



