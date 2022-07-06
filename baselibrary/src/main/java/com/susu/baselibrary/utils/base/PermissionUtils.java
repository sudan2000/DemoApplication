package com.susu.baselibrary.utils.base;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;

import androidx.annotation.StringRes;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.susu.baselibrary.utils.ui.DialogUtils;
import com.susu.baselibrary.utils.ui.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Author : sudan
 * Time : 2020/12/9
 * Description:
 */
public class PermissionUtils {

    private static final String TAG = "PermissionUtils";
    private static PermissionCallbacks mPermissionCallbacks;


    public interface PermissionCallbacks extends ActivityCompat.OnRequestPermissionsResultCallback {

        void onPermissionsGranted(List<String> perms);

        void onPermissionsDenied(List<String> perms);

    }

    public static void requestPermission(Context context, PermissionCallbacks callbacks, String... permissions) {
        mPermissionCallbacks = callbacks;
        if (PermissionUtils.hasPermissions(context, permissions)) {
            ToastUtils.showToast("有权限");
        } else {
            PermissionUtils.requestPermissions(context, "需要权限",
                    1, permissions);
        }
    }


    public static boolean hasPermissions(Context context, String... perms) {
        for (String perm : perms) {
            boolean hasPerm = ContextCompat.checkSelfPermission(context, perm) == PackageManager.PERMISSION_GRANTED;
            if (!hasPerm) {
                return false;
            }
        }

        return true;
    }

    public static void requestPermissions(final Object object, String rationale,
                                          final int requestCode, final String... perms) {
        requestPermissions(object,
                rationale,
                android.R.string.ok,
                android.R.string.cancel,
                requestCode,
                perms);
    }

    /**
     * @param object Activity or Fragment
     */
    public static void requestPermissions(final Object object, String rationale,
                                          @StringRes int positiveButton,
                                          @StringRes int negativeButton,
                                          final int requestCode, final String... perms) {

        checkCallingObjectSuitability(object);

        boolean shouldShowRationale = false;
        for (String perm : perms) {
            shouldShowRationale = shouldShowRationale || shouldShowRequestPermissionRationale(object, perm);
        }
        if (shouldShowRationale) { //弹框询问
            DialogUtils.showCommonDialog(getActivity(object), rationale, positiveButton, negativeButton,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            executePermissionsRequest(object, perms, requestCode);
                        }
                    }, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (mPermissionCallbacks != null) {
                                mPermissionCallbacks.onPermissionsDenied(new ArrayList<String>());
                            } else {
                                PermissionCallbacks callbacks = (PermissionCallbacks) object;
                                if (callbacks != null) {
                                    callbacks.onPermissionsDenied(new ArrayList<String>());
                                }
                            }
                        }
                    });
        } else {
            executePermissionsRequest(object, perms, requestCode);
        }
    }

    public static void onRequestPermissionsResult(int requestCode, String[] permissions,
                                                  int[] grantResults, Object object) {

        checkCallingObjectSuitability(object);
        PermissionCallbacks callbacks = (PermissionCallbacks) object;

        // Make a collection of granted and denied permissions from the request.
        ArrayList<String> granted = new ArrayList<>();
        ArrayList<String> denied = new ArrayList<>();
        for (int i = 0; i < permissions.length; i++) {
            String perm = permissions[i];
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                granted.add(perm);
            } else {
                denied.add(perm);
            }
        }

        // Report granted permissions, if any.
        if (!granted.isEmpty()) {
            // Notify callbacks
            callbacks.onPermissionsGranted(granted);
        }

        // Report denied permissions, if any.
        if (!denied.isEmpty()) {
            callbacks.onPermissionsDenied(denied);
        }

        // If 100% successful, call annotated methods
        if (!granted.isEmpty() && denied.isEmpty()) {
            runAnnotatedMethods(object, requestCode);
        }
    }

    private static boolean shouldShowRequestPermissionRationale(Object object, String perm) {
        if (object instanceof Activity) {
            return ActivityCompat.shouldShowRequestPermissionRationale((Activity) object, perm);
        } else if (object instanceof Fragment) {
            return ((Fragment) object).shouldShowRequestPermissionRationale(perm);
        } else {
            return false;
        }
    }

    private static void executePermissionsRequest(Object object, String[] perms, int requestCode) {
        checkCallingObjectSuitability(object);
        if (object instanceof Activity) {
            ActivityCompat.requestPermissions((Activity) object, perms, requestCode);
        } else if (object instanceof Fragment) {
            ((Fragment) object).requestPermissions(perms, requestCode);
        }
    }

    private static Activity getActivity(Object object) {
        if (object instanceof Activity) {
            return ((Activity) object);
        } else if (object instanceof Fragment) {
            return ((Fragment) object).getActivity();
        } else {
            return null;
        }
    }

    private static void runAnnotatedMethods(Object object, int requestCode) {
        Class clazz = object.getClass();
//        for (Method method : clazz.getDeclaredMethods()) {
//            if (method.isAnnotationPresent(AfterPermissionGranted.class)) {
//                // Check for annotated methods with matching request code.
//                AfterPermissionGranted ann = method.getAnnotation(AfterPermissionGranted.class);
//                if (ann.value() == requestCode) {
//                    // Method must be void so that we can invoke it
//                    if (method.getParameterTypes().length > 0) {
//                        throw new RuntimeException("Cannot execute non-void method " + method.getName());
//                    }
//
//                    try {
//                        // Make method accessible if private
//                        if (!method.isAccessible()) {
//                            method.setAccessible(true);
//                        }
//                        method.invoke(object);
//                    } catch (IllegalAccessException e) {
////                        ILogger.e(TAG, "runDefaultMethod:IllegalAccessException", e);
//                    } catch (InvocationTargetException e) {
////                        ILogger.e(TAG, "runDefaultMethod:InvocationTargetException", e);
//                    }
//                }
//            }
//        }
    }

    private static void checkCallingObjectSuitability(Object object) {
        // Make sure Object is an Activity or Fragment
        if (!((object instanceof Fragment) || (object instanceof Activity))) {
            throw new IllegalArgumentException("Caller must be an Activity or a Fragment.");
        }

        // Make sure Object implements callbacks
        if (!(object instanceof PermissionCallbacks)) {
            throw new IllegalArgumentException("Caller must implement PermissionCallbacks.");
        }
    }

}
