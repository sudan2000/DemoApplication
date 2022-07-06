package com.susu.baselibrary.permission;

import com.susu.baselibrary.permission.install.InstallRequest;
import com.susu.baselibrary.permission.perms.LRequest;
import com.susu.baselibrary.permission.perms.MRequest;
import com.susu.baselibrary.permission.perms.PermissionRequest;
import com.susu.baselibrary.permission.source.Source;
import com.susu.baselibrary.utils.system.OsUtils;

/**
 * Author : sudan
 * Time : 2021/7/22
 * Description:
 */
public class Options {

    private Source mSource;


    public Options(Source source) {
        mSource = source;
    }

    public PermissionRequest requestPermissions(String... permissions) {
        if (OsUtils.isOverMarshmallow()) {
            return new MRequest().requestPermissions(permissions);
        } else {
            return new LRequest().requestPermissions(permissions);
        }
    }

    /**
     * Android 8.0 安装请求
     *
     * @return
     */
    public InstallRequest install() {
        return new InstallRequest(mSource);
    }
}
