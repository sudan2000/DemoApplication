package com.susu.baselibrary.updateversion;

/**
 * Author : sudan
 * Time : 2021/7/27
 * Description:
 */
public interface DownLoadServiceListener {
    /**
     * Service 开始下载
     */
    void onHandleIntentService();

    /**
     * Service销毁
     */
    void onDestroyService();
}
