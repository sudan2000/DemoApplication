package com.susu.demoapplication.network;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OkHttpClientImpl {

    private static OkHttpClient mClient;
    private static OkHttpClientImpl sInstance;
    private Request request;
    private Response response;
    private String responseStr;

    public static void init(){
        mClient = new OkHttpClient.Builder()
                .build();
    }



    public static synchronized OkHttpClientImpl getInstance() {
        if (sInstance == null) {
            sInstance = new OkHttpClientImpl();
        }
        return sInstance;

    }

    public OkHttpClient getmClient() {
        return mClient;
    }

    public JSONObject postReq(Request request) throws IOException, JSONException {
        return post(request);
    }

    private JSONObject post(Request request) throws IOException, JSONException {
        this.request = request;
        Response response = getmClient().newCall(request).execute();
        String result = response.body().string();
        this.response = response;
        this.responseStr = result;
        if (response.code() == 200) {
            return new JSONObject(result);
        } else {
            String message = "Server service is down, status code is:" + response.code() + " Message is: " + result;
            throw new IOException(message);
        }
    }
}
