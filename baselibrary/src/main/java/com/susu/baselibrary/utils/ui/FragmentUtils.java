package com.susu.baselibrary.utils.ui;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.List;

/**
 * Author : sudan
 * Time : 2021/7/23
 * Description:
 */
public class FragmentUtils {

    public static void addFragment(FragmentManager fragmentManager, int containerViewId, Fragment fragment) {
        if (fragment == null || fragment.isAdded() || fragmentManager == null || fragmentManager.isDestroyed()) {
            return;
        }
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(containerViewId, fragment);
        fragmentTransaction.commitAllowingStateLoss();
    }

    public static void addFragment(FragmentManager fragmentManager, Fragment fragment, final String tag) {
        addFragment(fragmentManager, fragment, tag, false);
    }

    public static void addFragment(FragmentManager fragmentManager, Fragment fragment, final String tag, boolean add2BackStack) {
        if (fragment == null || fragment.isAdded() || fragmentManager.isDestroyed()) {
            return;
        }
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(fragment, tag);
        if (add2BackStack) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commitAllowingStateLoss();
        fragmentManager.executePendingTransactions();
    }

    public static void addFragment(FragmentManager fragmentManager, int containerViewId, Fragment fragment, final String tag) {
        if (fragment == null || fragment.isAdded() || fragmentManager.isDestroyed()) {
            return;
        }
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Fragment lastInstance = findFragmentByTag(fragmentManager, tag);
        if (lastInstance != null) {
            fragmentTransaction.remove(lastInstance);
        }
        fragmentTransaction.add(containerViewId, fragment, tag);
        fragmentTransaction.commitAllowingStateLoss();
        fragmentManager.executePendingTransactions();
    }


    public static void showFragment(FragmentManager fragmentManager, Fragment fragment) {
        if (fragment == null || fragmentManager == null || fragmentManager.isDestroyed()) {
            return;
        }

        @SuppressLint("RestrictedApi") List<Fragment> allFragments = fragmentManager.getFragments();
        if (null == allFragments) {
            return;
        }
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        for (Fragment each : allFragments) {
            fragmentTransaction.hide(each);
            if (each == fragment) {
                fragmentTransaction.show(fragment);
            }
        }
        fragmentTransaction.commitAllowingStateLoss();
    }

    public static void removeFragment(FragmentManager fragmentManager, Fragment fragment) {
        if (fragment == null || fragmentManager == null || fragmentManager.isDestroyed()) {
            return;
        }
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.remove(fragment);
        fragmentTransaction.commitAllowingStateLoss();
    }

    public static void hideFragment(FragmentManager fragmentManager, Fragment fragment) {
        if (fragment == null || fragment.isHidden() || fragmentManager.isDestroyed()) {
            return;
        }
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.hide(fragment);
        fragmentTransaction.commitAllowingStateLoss();
    }

    public static void hideFragment(FragmentManager fragmentManager, String tag) {
        if (fragmentManager.isDestroyed() || TextUtils.isEmpty(tag)) {
            return;
        }
        Fragment fragment = fragmentManager.findFragmentByTag(tag);
        if (fragment == null || fragment.isHidden()) {
            return;
        }
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.hide(fragment);
        fragmentTransaction.commitAllowingStateLoss();
    }

    public static void replaceFragment(FragmentManager fragmentManager, int containerViewId, Fragment fragment) {
        if (fragment == null || fragment.isAdded() || fragmentManager == null || fragmentManager.isDestroyed()) {
            return;
        }
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(containerViewId, fragment);
        fragmentTransaction.commitAllowingStateLoss();
    }

    public static void replaceFragmentWithAnimation(FragmentManager fragmentManager,
                                                    int containerViewId,
                                                    Fragment fragment, String fragmentTag) {
        if (fragment == null || fragment.isAdded() || fragmentManager == null || fragmentManager.isDestroyed()) {
            return;
        }
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//		fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,
//				R.anim.enter_from_left, R.anim.exit_to_right);
        fragmentTransaction.replace(containerViewId, fragment, fragmentTag);
        fragmentTransaction.addToBackStack("backRecord");
        fragmentTransaction.commitAllowingStateLoss();
    }

    public static void replaceFragment(FragmentManager fragmentManager, int containerViewId,
                                       Fragment fragment, String fragmentTag) {
        if (fragment == null || fragment.isAdded() || fragmentManager == null || fragmentManager.isDestroyed()) {
            return;
        }
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(containerViewId, fragment, fragmentTag);
        fragmentTransaction.commitAllowingStateLoss();
    }

    public static Fragment findFragmentById(FragmentManager fragmentManager, int id) {
        return fragmentManager.findFragmentById(id);
    }

    public static Fragment findFragmentByTag(FragmentManager fragmentManager, String tag) {
        if (fragmentManager == null || fragmentManager.isDestroyed() || TextUtils.isEmpty(tag)) {
            return null;
        }
        return fragmentManager.findFragmentByTag(tag);
    }

}
