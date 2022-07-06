package com.susu.baselibrary.permission.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.susu.baselibrary.permission.support.PermissionsPageManager;

/**
 * Author : sudan
 * Time : 2021/7/22
 * Description:
 */
public class PermissionActivity extends Activity {


    private static final String TAG = "Permission---PermissionActivity";
    private static RequestListener mListener;
    private static final int DEFAULT_PERMISSIONS_CODE = 365;
    private static final String INTENT_KEY_PERMISSIONS = "INTENT_KEY_PERMISSIONS";
    private static final String INTENT_KEY_OPT = "INTENT_KEY_OPT";
    private static final int OPT_PERMISSION = 1;
    private static final int OPT_CUSTOM_DIALOG = 2;
    private static final int OPT_INSTALL = 3;

    private static final int DEFAULT_INSTALL_REQUEST_CODE = 368;


    public static void requestPermissions(Context context, String[] permissions, RequestListener listener) {
        mListener = listener;
        Intent intent = new Intent(context, PermissionActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(INTENT_KEY_PERMISSIONS, permissions);
        intent.putExtra(INTENT_KEY_OPT, OPT_PERMISSION);
        context.startActivity(intent);
    }

    public static void install(Context context, RequestListener requestListener) {
        PermissionActivity.mListener = requestListener;
        Intent intent = new Intent(context, PermissionActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(INTENT_KEY_OPT, OPT_INSTALL);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() == null) {
            finish();
        }
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            finish();
        }
        int opt = bundle.getInt(INTENT_KEY_OPT);
        if (opt == OPT_PERMISSION) {
            String[] permissions = bundle.getStringArray(INTENT_KEY_PERMISSIONS);
            requestPermissions(permissions);

        } else if (opt == OPT_CUSTOM_DIALOG) {

        } else if (opt == OPT_INSTALL) {
            PermissionsPageManager.startInstallPage(this, DEFAULT_INSTALL_REQUEST_CODE);
        }
    }

    private void requestPermissions(String[] permissions) {
        if (permissions != null && permissions.length != 0) {
            ActivityCompat.requestPermissions(this, permissions, DEFAULT_PERMISSIONS_CODE);
        } else {
            finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (mListener != null) {
            mListener.onRequestCallback(true);
        }
        finish();
    }

    public interface RequestListener {
        void onRequestCallback(boolean sure);
    }
}
