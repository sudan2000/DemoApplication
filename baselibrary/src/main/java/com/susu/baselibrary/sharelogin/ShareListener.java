package com.susu.baselibrary.sharelogin;

/**
 * Author : sudan
 * Time : 2022/1/13
 * Description:
 */
public interface ShareListener {
    /**
     * @param o 只在QQ分享才有值，为QQ分享返回值
     */
    public void onSuccess(Object o);

    public void onException(Throwable e);

    public void onCancel();
}
