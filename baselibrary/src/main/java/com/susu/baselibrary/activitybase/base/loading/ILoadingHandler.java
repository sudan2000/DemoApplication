package com.susu.baselibrary.activitybase.base.loading;

import android.app.Activity;

/**
 * Author : sudan
 * Time : 2022/1/14
 * Description:
 */
public interface ILoadingHandler {

    void create(Activity activity);

    void showLoading();

    void dismissLoading();

    void showToast(int stringId);

    void showToast(String text);

}
