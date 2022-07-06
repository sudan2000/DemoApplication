package com.susu.baselibrary.permission;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.susu.baselibrary.permission.source.ActivitySource;
import com.susu.baselibrary.permission.source.ContextSource;
import com.susu.baselibrary.permission.source.FragmentSource;
import com.susu.baselibrary.permission.source.SupportFragmentSource;

/**
 * Author : sudan
 * Time : 2021/7/22
 * Description:
 */
public class PermissionManager {

    public static Options with(@NonNull Activity activity) {
        return new Options(new ActivitySource(activity));
    }

    public static Options with(@NonNull android.app.Fragment fragment) {
        return new Options(new FragmentSource(fragment));
    }

    public static Options with(@NonNull Fragment fragment) {
        return new Options(new SupportFragmentSource(fragment));
    }

    public static Options with(@NonNull Context context) {
        if (context instanceof Activity) {
            return new Options(new ActivitySource((Activity) context));
        }
        return new Options(new ContextSource(context));
    }
}
