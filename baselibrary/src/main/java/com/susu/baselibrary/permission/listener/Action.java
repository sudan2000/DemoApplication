package com.susu.baselibrary.permission.listener;

/**
 * Author : sudan
 * Time : 2021/7/27
 * Description:
 */
public interface Action<T> {

    void onGranted(T data);

    void onDenied(T data);
}
