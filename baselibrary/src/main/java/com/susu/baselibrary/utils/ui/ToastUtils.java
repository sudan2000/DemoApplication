package com.susu.baselibrary.utils.ui;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.susu.baselibrary.utils.system.CoreUtils;

/**
 * Author : sudan
 * Time : 2020/12/9
 * Description:
 */
public class ToastUtils {

    public static void showToast(String text) {
        Toast.makeText(CoreUtils.getApp(), text, Toast.LENGTH_LONG).show();
    }

    public static void show(String text) {
        Toast.makeText(CoreUtils.getApp(), text, Toast.LENGTH_LONG).show();
    }

    public static void show(final Context context, final String message) {
        if (context == null){
            return;
        }
        if (TextUtils.isEmpty(message)){
            return;
        }
        Toast.makeText(CoreUtils.getApp(), message, Toast.LENGTH_LONG).show();
//        sendHandler(context, message, ToastUtils.WHAT_NORMAL);
    }

    private static void sendHandler(final Context context, final String message, int whatType) {
//        if( handler == null ){
//            handler = new MyHandler(context);
//        }
//
//        Message msg = handler.obtainMessage(whatType);
//        msg.obj = message;
//        handler.sendMessage(msg);
    }
}
