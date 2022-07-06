package com.susu.baselibrary.permission.source;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import com.susu.baselibrary.permission.ui.PermissionActivity;

import java.lang.reflect.Method;

/**
 * Author : sudan
 * Time : 2021/7/23
 * Description:
 */
public class ContextSource extends Source {

    private Context mContext;

    public ContextSource(Context context) {
        this.mContext = context;
    }

    @Override
    public Object getContext() {
        return mContext;
    }

    @Override
    public Context getActivity() {
        return mContext;
    }

    @Override
    public boolean isShowRationalePermission(String permission) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return false;
        }
        if (mContext instanceof Activity) {
            ((Activity) mContext).shouldShowRequestPermissionRationale(permission);
        }
        try {
            PackageManager packageManager = mContext.getPackageManager();
            Class<?> pkManagerClass = packageManager.getClass();
            Method method = pkManagerClass.getMethod("shouldShowRequestPermissionRationale", String.class);
            if (!method.isAccessible()) method.setAccessible(true);
            return (boolean) method.invoke(packageManager, permission);
        } catch (Exception ignored) {
            return false;
        }
    }

    @Override
    public void requestPermissions(String[] permissions, PermissionActivity.RequestListener listener) {
        PermissionActivity.requestPermissions(mContext, permissions, listener);
    }
}
