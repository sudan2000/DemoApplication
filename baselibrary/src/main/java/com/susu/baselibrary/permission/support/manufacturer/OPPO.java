package com.susu.baselibrary.permission.support.manufacturer;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;

/**
 * support:
 * 1. oppo a57 android 6.0.1/coloros3.0
 * <p>
 * manager home page, permissions manage page does not work!!!, or
 * {@link Protogenesis#settingIntent()}
 * <p>
 * Created by joker on 2017/8/4.
 */

public class OPPO implements PermissionsPage {
    private final Activity context;
    private final String PKG = "com.coloros.safecenter";
    private final String MANAGER_OUT_CLS = "com.coloros.safecenter.permission.singlepage" +
            ".PermissionSinglePageActivity";

    public OPPO(Activity context) {
        this.context = context;
    }

    @Override
    public Intent settingIntent() throws Exception {
        Intent intent = new Intent();
        intent.putExtra(PACK_TAG, context.getPackageName());
        ComponentName comp = new ComponentName(PKG, MANAGER_OUT_CLS);
        // do not work!!
//        comp = new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.permission" + ".PermissionAppAllPermissionActivity");
        intent.setComponent(comp);
        return intent;
    }

    @Override
    public Intent overlayIntent() throws Exception {
        Intent intent = new Intent();
        intent.putExtra("packageName", context.getPackageName());
        intent.setClassName("com.color.safecenter", "com.color.safecenter.permission.floatwindow.FloatWindowListActivity");
        if (hasActivity(context, intent)) {
            return intent;
        }
        intent.setClassName("com.coloros.safecenter", "com.coloros.safecenter.sysfloatwindow.FloatWindowListActivity");
        if (hasActivity(context, intent)) {
            return intent;
        }
        intent.setClassName("com.oppo.safe", "com.oppo.safe.permission.PermissionAppListActivity");
        return intent;
    }

    private static boolean hasActivity(Activity context, Intent intent) {
        PackageManager packageManager = context.getPackageManager();
        return packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY).size() > 0;
    }
}
