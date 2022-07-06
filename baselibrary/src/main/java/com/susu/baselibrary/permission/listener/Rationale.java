package com.susu.baselibrary.permission.listener;

import android.content.Context;

/**
 * Author : sudan
 * Time : 2021/7/27
 * Description:
 */
public interface Rationale<T> {

    void showRationale(Context context, T data, RequestExecutor executor);


}
