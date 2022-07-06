package com.susu.baselibrary.speech;

import android.os.Environment;

/**
 * Author : sudan
 * Time : 2021/10/18
 * Description:
 */
public class SpeechConstants {

    public static final String APP_ID = "e7165c2c";
    public static final String API_SECRET = "ZjMxOWY5MjY2M2VmNzc2NmFiM2M5YTEz";
    public static final String API_KEY = "0637a9eb60d8f2ef4d3d7448e19a260d";
    public static final String API_KEY_WEB = "4c0028ed65e4fee00719045d705002f3";


    public static final String HOST_URL = "https://iat-api.xfyun.cn/v2/iat"; //中英文，http url 不支持解析 ws/wss schema

    public static final String FOLDER = Environment.getExternalStorageDirectory().getPath();

    // 请求地址
    private static final String HOST = "rtasr.xfyun.cn/v1/ws";
    public static final String BASE_URL = "ws://" + HOST;
    public static final String ORIGIN = "http://" + HOST;
}
