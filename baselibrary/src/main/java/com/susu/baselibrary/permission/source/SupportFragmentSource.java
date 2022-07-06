package com.susu.baselibrary.permission.source;

import android.content.Context;
import android.os.Build;

import androidx.fragment.app.Fragment;

import com.susu.baselibrary.permission.ui.PermissionActivity;

/**
 * Author : sudan
 * Time : 2021/7/23
 * Description:
 */
public class SupportFragmentSource extends Source {

    private Fragment mFragment;

    public SupportFragmentSource(Fragment fragment) {
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
