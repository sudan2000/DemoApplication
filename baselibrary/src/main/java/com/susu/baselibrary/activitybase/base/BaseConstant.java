package com.susu.baselibrary.activitybase.base;


import com.susu.baselibrary.BuildConfig;

/**
 * Author : sudan
 * Time : 2022/1/14
 * Description:
 */
public class BaseConstant {
    /** 退出登录广播-token失效或者被踢。
     * 通过CodeChecker被动触发，或者H5通知到移动端账号被踢。
     * 主进程和remote进程（H5独立进程）都监听进行处理
     * */
    public static final String LOGIN_TIMEOUT_BROADCAST = BuildConfig.APPLICATION_ID + ".broadcast_login_timeout";

}
