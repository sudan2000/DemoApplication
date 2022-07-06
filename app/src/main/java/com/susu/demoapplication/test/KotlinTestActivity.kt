package com.susu.demoapplication.test

import android.util.Log
import com.susu.baselibrary.activitybase.BaseJavaActivity
import com.susu.demoapplication.R
import kotlinx.coroutines.*

/**
 * Author : sudan
 * Time : 2020/12/14
 * Description:
 */
class KotlinTestActivity : BaseJavaActivity(){

    private lateinit var job: Job

    companion object {
        const val TAG = "KotlinTestActivity"
    }

    override fun injectLayoutID(): Int {
        return R.layout.activity_kotlin_test
    }

    override fun initView() {
        job = Job()
//        tv1.setOnClickListener { click() }

    }

    override fun initData() {

    }


    private fun click() {

    }

    fun goHome(count: Int = 2, play: (a: String) -> Unit) {
        Log.d(TAG, "goHome方法内部")
        play("play")

    }

//    override val coroutineContext: CoroutineContext
//        get() = Dispatchers.Main + job


}