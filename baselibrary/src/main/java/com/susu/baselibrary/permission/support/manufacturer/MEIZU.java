package com.susu.baselibrary.permission.support.manufacturer;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;


/**
 * Created by joker on 2017/8/24.
 */

public class MEIZU implements PermissionsPage {
    private final Activity activity;
    private final String N_MANAGER_OUT_CLS = "com.meizu.safe.permission.PermissionMainActivity";
    private final String L_MANAGER_OUT_CLS = "com.meizu.safe.SecurityMainActivity";
    private final String PKG = "com.meizu.safe";

    public MEIZU(Activity activity) {
        this.activity = activity;
    }

    @Override
    public Intent settingIntent() throws Exception {
        Intent intent = new Intent();
        intent.putExtra(PACK_TAG, activity.getPackageName());
        ComponentName comp = new ComponentName(PKG, getCls());
        intent.setComponent(comp);
        return intent;
    }

    @Override
    public Intent overlayIntent() throws Exception {
        Intent intent = new Intent("com.meizu.safe.security.SHOW_APPSEC");
        intent.putExtra("packageName", activity.getPackageName());
        // remove this line code for fix flyme6.3
//        intent.setComponent(new ComponentName("com.meizu.safe", "com.meizu.safe.security.AppSecActivity"));
        return intent;
    }

    private String getCls() {
//        if (ManufacturerSupportUtil.isAndroidL()) {
//            return L_MANAGER_OUT_CLS;
//        } else {
            return N_MANAGER_OUT_CLS;
//        }
    }
}
