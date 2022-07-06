package com.susu.baselibrary.utils;

import android.app.Activity;
import android.content.Intent;

/**
 * Author : sudan
 * Time : 2020/12/14
 * Description:
 */
public class JumpUtils {

    public static void jumpToActivity(Activity activity, Class clz) {
        Intent intent = new Intent(activity, clz);
        activity.startActivity(intent);

    }
}
