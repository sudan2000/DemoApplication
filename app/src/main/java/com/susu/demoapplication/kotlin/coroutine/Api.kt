package com.susu.demoapplication.kotlin.coroutine

import com.susu.demoapplication.mvvm.basic.User
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Author : sudan
 * Time : 2021/1/7
 * Description:
 */
open interface UserApi {
    @GET("id")
    fun getUser(@Path("id") id: String): Call<User>
}

internal object Factory {
    fun getUserApi(): UserApi {
        val retrofit = Retrofit.Builder()
                .baseUrl("http://www.baidu.com/")
                .build()
        return retrofit.create(UserApi::class.java)
    }
}