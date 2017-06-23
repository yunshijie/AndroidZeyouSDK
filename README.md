# android-player-sdk

## SDK 概述

android-player-sdk 是一个适用于 Android 平台的影音播发器 SDK ，基于 [ijkplayer](https://github.com/Bilibili/ijkplayer) ( based on [ffplay](http://ffmpeg.org/) )，可高速定制化和二次开发，为 Android 开发者提供简单，快捷的接口。

## 播放器功能特性

	
* 支持在线视频协议：`HLS`, `RTMP`, `HTTP-FLV` 等，支持 `HLS` 多种分辨率切换

* 支持本地视频播放 （MP4、M4A、flv 等）

* 支持设置窗口大小和全屏设置

* 支持缓冲大小设置

* 提供 UpVideoView 控件

* 支持 ARM, ARMv7a, ARM64v8a, X86 主流芯片体系架构

## sdk 使用

* 导入 [so lib 库](https://github.com/upyun/android-player-sdk/tree/master/upyun-player-demo/src/main/jniLibs)

* 导入 java lib 库 [ijkplayer-java](https://github.com/upyun/android-player-sdk/tree/master/ijkplayer-java) 和 [upplayer](https://github.com/upyun/android-player-sdk/tree/master/upplayer)

## SDK 使用示例

直接使用控件 UpVideoView 

```java
        //设置默认缓存区大小 (需在setVideoPath 或者 resume 前执行生效)
        upVideoView.setBufferSize(1 * 1024 * 1024);

        //设置背景图片
        upVideoView.setImage(R.drawable.dog);

        //设置播放地址
        upVideoView.setVideoPath(path);

        //开始播放
        upVideoView.start();
        
        //暂停播放
        upVideoView.pause();
        
        // 重新开始播放器
        upVideoView.resume();
```

详见 [DEMO](https://github.com/upyun/android-player-sdk/blob/master/upyun-player-demo/src/main/java/com/upyun/playdemo/MainActivity.java)

## SDK 最低要求

Android 2.3 (API 9) 及其以上

## 版本历史

0.1.0 基本的直播播放器

* 支持rtmp, hls, http-flv

0.2.0 统计信息，增加播放控制

* 增加统计信息的获取和上传

* 支持调整宽高比

* 增加缓冲区控制

* 增加全屏播放和断线重连

0.3.0 完善统计功能，单音频播放

* 增加pid,cid等统计参数

* 支持单音频播放，speex格式解码

* 播放器秒开优化

0.4.0 累积延迟，性能优化

* 播放器累积延时优化

* 播放器内存消耗优化

* 播放器apk大小精简至6M（不含speex）

0.5.0 添加动态追帧 (9.18)

* 播放器添加动态追帧

0.6.0 h265 & 音频数据接口（1.6）

* 支持h265播放

* 增加获取音频数据接口

## 说明
SDK 目前基于 [ijkplayer](https://github.com/Bilibili/ijkplayer) , 感谢 [ijkplayer](https://github.com/Bilibili/ijkplayer)。


