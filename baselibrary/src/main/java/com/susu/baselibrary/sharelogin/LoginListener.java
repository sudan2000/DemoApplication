package com.susu.baselibrary.sharelogin;

/**
 * Author : sudan
 * Time : 2022/1/13
 * Description:
 */
public interface LoginListener {

    public void onSuccess(LoginResponse response);

    public void onException(Throwable e);

    public void onCancel();
}
