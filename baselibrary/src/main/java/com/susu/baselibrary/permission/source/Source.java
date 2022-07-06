package com.susu.baselibrary.permission.source;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;

import androidx.fragment.app.FragmentActivity;

import com.susu.baselibrary.permission.ui.PermissionActivity;
import com.susu.baselibrary.permission.ui.PermissionFragment;

/**
 * Author : sudan
 * Time : 2021/7/22
 * Description:
 */
public abstract class Source {

    protected static final String TAG = "Permission---Source";
    protected static final String TAG_FRAGMENT = "PermissionFragment";

    public abstract Object getContext();

    public abstract Context getActivity();

    /**
     * 是否需要解释说明
     */
    public abstract boolean isShowRationalePermission(String permission);

    public abstract void requestPermissions(String[] permissions, PermissionActivity.RequestListener listener);

    public PermissionFragment getPermissionFragment(Activity activity) {
        PermissionFragment fragment = findPermissionFragment(activity);
        FragmentManager manager = activity.getFragmentManager();
        FragmentTransaction trans = manager.beginTransaction();
        if (fragment == null) {
            fragment = PermissionFragment.newInstance();
            trans.add(fragment, TAG_FRAGMENT);
        }
        trans.show(fragment).commitAllowingStateLoss();
        manager.executePendingTransactions();
        return fragment;
    }

    public PermissionFragment findPermissionFragment(Activity activity) {
        return (PermissionFragment) activity.getFragmentManager().findFragmentByTag(TAG_FRAGMENT);
    }
}
