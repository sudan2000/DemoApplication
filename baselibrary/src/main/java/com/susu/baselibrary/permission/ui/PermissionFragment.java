package com.susu.baselibrary.permission.ui;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

/**
 * Author : sudan
 * Time : 2021/7/23
 * Description:
 */
public class PermissionFragment extends Fragment {

    private static final int DEFAULT_PERMISSION_CODE = 300;

    private PermissionActivity.RequestListener mListener;


    public static PermissionFragment newInstance() {
        PermissionFragment fragment = new PermissionFragment();
        return fragment;
    }


    @TargetApi(Build.VERSION_CODES.M)
    public void requestPermissions(String[] permissions, PermissionActivity.RequestListener listener) {
        mListener = listener;
        requestPermissions(permissions, DEFAULT_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (mListener != null) {
            mListener.onRequestCallback(true);
        }
    }
}
