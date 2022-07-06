package com.susu.demoapplication.kotlin.coroutine

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.ConnectException

/**
 * Author : sudan
 * Time : 2021/1/7
 * Description:
 */
fun <T> CoroutineScope.retrofit(dsl: RetrofitCoroutineDSL<T>.() -> Unit) {

    this.launch(Dispatchers.Main) {
        val coroutine = RetrofitCoroutineDSL<T>().apply(dsl)
        coroutine.api?.let { call ->
            val deferred = async(Dispatchers.IO) {
                try {
                    call.execute()
                } catch (e: ConnectException) {
                    coroutine.onFail?.invoke(-1, "网络连接出错")
                    null
                } catch (e: IOException) {
                    coroutine.onFail?.invoke(-1, "未知网络错误")
                    null
                }
            }
            deferred.invokeOnCompletion {
                if (deferred.isCancelled) {
                    call.cancel()
                    coroutine.clean()
                }
            }
            val response = deferred.await()
            if (response == null) {
                coroutine.onFail?.invoke(-1, "返回为空")
            } else {
                response.let {
                    if (response.isSuccessful) {


                    }
                }
            }
        }


    }
}