package com.susu.demoapplication.kotlin

import com.susu.demoapplication.R
import com.susu.demoapplication.mvvm.basic.BaseVMActivity
import com.susu.demoapplication.mvvm.HomeViewModel

/**
 * Author : sudan
 * Time : 2020/12/11
 * Description:
 */
class HomeActivity : BaseVMActivity() {
    private val homeVm = injectLazyVM(HomeViewModel::class.java)


    override fun injectLayoutID(): Int {
        return R.layout.activity_home
    }

    override fun initView() {
//        app_activity_home_find_doctor_tv.setOnClickListener { gotoFindDoctor() }
//        app_activity_home_message_tv.setOnClickListener { gotoMessage() }
//        app_activity_home_lecture_tv.setOnClickListener { gotoFamousDoctor() }
//        app_activity_home_account_tv.setOnClickListener {
//
//        app_activity_home_health_tv.setOnClickListener { gotoHealth() }
//        app_activity_home_vip_center_tv.setOnClickListener { gotoVipCenter() }

//        observeLoginLogout()
//        observeData()
    }

    private fun gotoFindDoctor() {
        takeIf { checkLogin() }?.let {
            showToast("去找医生页面")
        }
    }

    private fun gotoMessage() {
        takeIf { checkLogin() }?.let {
            showToast("去消息页面")
        }
    }

    private fun gotoFamousDoctor() {
        takeIf { checkLogin() }?.let {
            showToast("去名师讲堂页面")
        }
    }

    private fun gotoMyAccount() {
        takeIf { checkLogin() }?.let {
            showToast("去我的账户页面")
        }
    }

    private fun gotoHealth() {
        takeIf { checkLogin() }?.let {
            showToast("去健康档案页面")
        }
    }

    private fun gotoVipCenter() {
        takeIf { checkLogin() }?.let {
            showToast("去会员中心")
        }
    }


    private fun checkLogin(): Boolean {
        if (!isLogin) {
            return false
        }
        return true
    }

}