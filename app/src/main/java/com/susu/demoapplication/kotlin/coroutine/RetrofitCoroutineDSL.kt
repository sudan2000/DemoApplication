package com.susu.demoapplication.kotlin.coroutine

import retrofit2.Call

/**
 * Author : sudan
 * Time : 2021/1/7
 * Description:
 */
class RetrofitCoroutineDSL<T> {
    var api: (Call<Result<T>>)? = null

    internal var onSuccess: ((T) -> Unit)? = null
        private set
    internal var onFail: ((errorCode: Int, errorMsg: String) -> Unit)? = null
        private set

    fun onSuccess(block: (T) -> Unit) {
        this.onSuccess = block
    }

    fun onFail(block: (errorCode: Int, errorMsg: String) -> Unit) {
        this.onFail = block
    }

    internal fun clean() {
        onSuccess = null
        onFail = null
    }
}

