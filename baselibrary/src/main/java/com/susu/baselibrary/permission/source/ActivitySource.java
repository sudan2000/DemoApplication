package com.susu.baselibrary.permission.source;

import android.app.Activity;
import android.content.Context;
import android.os.Build;

import com.susu.baselibrary.permission.ui.PermissionActivity;

/**
 * Author : sudan
 * Time : 2021/7/22
 * Description:
 */
public class ActivitySource extends Source {

    private Activity mActivity;

    public ActivitySource(Activity activity) {
        this.mActivity = activity;
    }

    @Override
    public Object getContext() {
        return mActivity;
    }

    @Override
    public Context getActivity() {
        return mActivity;
    }

    @Override
    public boolean isShowRationalePermission(String permission) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return false;
        }
        return mActivity.shouldShowRequestPermissionRationale(permission);
    }

    @Override
    public void requestPermissions(String[] permissions, PermissionActivity.RequestListener listener) {
        getPermissionFragment(mActivity).requestPermissions(permissions, listener);
    }
}
