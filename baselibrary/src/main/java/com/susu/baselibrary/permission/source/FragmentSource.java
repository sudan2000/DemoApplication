package com.susu.baselibrary.permission.source;

import android.content.Context;

import android.app.Fragment;
import android.os.Build;

import com.susu.baselibrary.permission.ui.PermissionActivity;

/**
 * Author : sudan
 * Time : 2021/7/23
 * Description:
 */
public class FragmentSource extends Source {

    private Fragment mFragment;

    public FragmentSource(Fragment fragment) {
        mFragment = fragment;
    }

    @Override
    public Object getContext() {
        return mFragment;
    }

    @Override
    public Context getActivity() {
        return mFragment != null ? mFragment.getActivity() : null;
    }

    @Override
    public boolean isShowRationalePermission(String permission) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return false;
        }
        return mFragment.shouldShowRequestPermissionRationale(permission);
    }

    @Override
    public void requestPermissions(String[] permissions, PermissionActivity.RequestListener listener) {
        getPermissionFragment(mFragment.getActivity()).requestPermissions(permissions, listener);
    }
}
