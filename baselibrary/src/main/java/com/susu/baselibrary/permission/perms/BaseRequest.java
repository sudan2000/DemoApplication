package com.susu.baselibrary.permission.perms;

/**
 * Author : sudan
 * Time : 2021/7/23
 * Description:
 */
public abstract class BaseRequest implements PermissionRequest {

    protected String[] mPermissions;

    @Override
    public PermissionRequest requestPermissions(String... permissions) {
        mPermissions = permissions;
        return this;
    }


}
