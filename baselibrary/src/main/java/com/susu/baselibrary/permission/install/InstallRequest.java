package com.susu.baselibrary.permission.install;


import android.app.AppOpsManager;
import android.content.Context;
import android.os.Build;

import com.susu.baselibrary.permission.listener.Action;
import com.susu.baselibrary.permission.listener.Rationale;
import com.susu.baselibrary.permission.listener.RequestExecutor;
import com.susu.baselibrary.permission.source.Source;
import com.susu.baselibrary.permission.ui.PermissionActivity;
import com.susu.baselibrary.utils.system.OsUtils;

import java.lang.reflect.Method;

/**
 * Author : sudan
 * Time : 2021/7/27
 * Description:
 */
public class InstallRequest implements RequestExecutor, PermissionActivity.RequestListener {

    private static final int OP_REQUEST_INSTALL_PACKAGES = 66;

    private Action<Void> mRequestListener;

    protected Source mSource;
    private Rationale<Void> mRationale = new Rationale<Void>() {
        @Override
        public void showRationale(Context context, Void data, RequestExecutor executor) {
            executor.execute();
        }
    };

    public InstallRequest(Source source) {
        this.mSource = source;
    }

    public Context getContext() {
        return mSource.getActivity();
    }

    public InstallRequest requestPermissionListener(Action<Void> listener) {
        this.mRequestListener = listener;
        return this;
    }

    public void request() {
        if (canRequestPackageInstalls()) {
            onSuccess();
        } else {
            mRationale.showRationale(getContext(), null, this);
        }

    }


    /**
     * 是否允许安装未知来源应用
     * <p>
     * 6.0以下：不需要允许 安装未知来源应用 权限
     * 6.0-8.0(不包括8.0)：不需要允许 安装未知来源应用 权限；
     * 问题：但是我oppo 6.0的手机上需要允许安装应用权限
     * <p>
     * 8.0及以上：需要允许 安装未知来源应用 权限；否则无法安装
     * <p>
     * targetSdkVersion>=24: 有个应用间文件共享问题需要处理；
     */
    private final boolean canRequestPackageInstalls() {
        // 8.0及以上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            // targetSdkVersion是26以上才能获取正确的canRequestPackageInstalls，否则会一直返回false
            if (OsUtils.getTargetSdkVersion(getContext()) < Build.VERSION_CODES.O) {
                try {
                    Class<AppOpsManager> clazz = AppOpsManager.class;
                    AppOpsManager appOpsManager = (AppOpsManager) getContext().getSystemService(Context.APP_OPS_SERVICE);
                    Method method = clazz.getDeclaredMethod("checkOpNoThrow", int.class, int.class, String.class);
                    int result = (int) method.invoke(appOpsManager, OP_REQUEST_INSTALL_PACKAGES, android.os.Process.myUid(), getContext().getPackageName());
                    return result == AppOpsManager.MODE_ALLOWED;
                } catch (Exception ignored) {
                    // Android P does not allow reflections.
                    return true;
                }
            }
            return getContext().getPackageManager().canRequestPackageInstalls();

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 6.0-8.0(不含)
            return true;
        } else {
            // 6.0以下不需要判断安装权限
            return true;
        }
    }


    @Override
    public void execute() {
        PermissionActivity.install(getContext(), this);
    }

    @Override
    public void rationaleCancel() {
        onFail();
    }

    @Override
    public void onRequestCallback(boolean sure) {

    }

    void onSuccess() {
        if (mRequestListener != null) {
            mRequestListener.onGranted(null);
        }
    }

    void onFail() {
        if (mRequestListener != null) {
            mRequestListener.onDenied(null);
        }
    }
}
