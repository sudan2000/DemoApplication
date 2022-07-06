package com.susu.demoapplication.basetest

import android.os.Bundle
import com.susu.baselibrary.activitybase.base.BaseActivity
import com.susu.demoapplication.R
import kotlinx.android.synthetic.main.activity_base_test.*

/**
 * Author : sudan
 * Time : 2021/11/24
 * Description:
 */
class BaseTestActivity: BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base_test)
        urlTest()
    }

    private fun urlTest(){
        tvUrlTest.setOnClickListener{
            UrlTest.test()
        }
    }
}