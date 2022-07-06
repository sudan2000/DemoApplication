package com.susu.baselibrary.permission.support.manufacturer;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;

/**
 * Created by joker on 2017/8/4.
 */

public class Protogenesis implements PermissionsPage {
    private final Activity activity;

    public Protogenesis(Activity activity) {
        this.activity = activity;
    }

    // system details setting page
    @Override
    public Intent settingIntent() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
        intent.setData(uri);
        return intent;
    }

    @Override
    public Intent overlayIntent() {
        // 启动隐式Intent
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
        // 携带App的包名信息
        intent.setData(Uri.parse("package:" + activity.getPackageName()));
        return intent;
    }
}
