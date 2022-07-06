package com.susu.demoapplication.basetest

import android.net.Uri
import com.susu.baselibrary.utils.base.LogUtils

/**
 * Author : sudan
 * Time : 2021/11/24
 * Description:
 */
object UrlTest {

    fun test() {
        val url = "wynative://wechat/miniProgram?type=0&platform=weixin&originId=gh_xxcagadasgs&path=   ghshkshk/jksjkj"
        val uri = Uri.parse(url)
        val type = uri.getQueryParameter("type")
        LogUtils.d("test---", "type: $type") //0

        val platform = uri.getQueryParameter("platform")
        LogUtils.d("test---", "platform: $platform") //weixin

        val originId = uri.getQueryParameter("originId")
        LogUtils.d("test---", "originId: $originId") //gh_xxcagadasgs

        val path = uri.getQueryParameter("path")
        LogUtils.d("test---", "path: $path") //   ghshkshk/jksjkj
    }
}