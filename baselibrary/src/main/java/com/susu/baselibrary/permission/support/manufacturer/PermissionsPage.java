package com.susu.baselibrary.permission.support.manufacturer;

import android.content.Intent;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.susu.baselibrary.permission.support.manufacturer.PermissionsPage.PageType.ANDROID_SETTING_PAGE;
import static com.susu.baselibrary.permission.support.manufacturer.PermissionsPage.PageType.MANAGER_PAGE;

/**
 * Created by joker on 2017/8/4.
 * 应用权限管理页面
 */

public interface PermissionsPage {
    String PACK_TAG = "package";

    // normally, ActivityNotFoundException
    Intent settingIntent() throws Exception;

    Intent overlayIntent() throws Exception;


    @IntDef({MANAGER_PAGE, ANDROID_SETTING_PAGE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface PageType {
        int MANAGER_PAGE = 1;
        int ANDROID_SETTING_PAGE = 0;
    }
}
