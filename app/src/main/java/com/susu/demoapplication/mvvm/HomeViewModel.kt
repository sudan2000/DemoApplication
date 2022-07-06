package com.susu.demoapplication.mvvm

import android.app.Application
import androidx.lifecycle.liveData
import com.susu.baselibrary.utils.base.DeviceUtils
import com.susu.demoapplication.mvvm.basic.BaseViewModel

/**
 * Author : sudan
 * Time : 2020/12/11
 * Description:
 */
class HomeViewModel(application: Application) : BaseViewModel(application) {
    companion object {
        const val TAG = "HomeViewModel"
    }

    fun fetchCustomer() = liveData<Int> {
        val params = DeviceUtils.getDeviceId(getApplication())
//        val response = safeCall(call = {FetchCustomerRequest(params).hideToast().execute()}, showLoading = false,showErrorToast = false,errorMessage = "")
//        when(response){
//            is ApiSusscessResponse ->{
//                val hasNoCustomer = response.body.data?.isNullOrEmpty() ?: true
//                emit(response.body.data?.size ?: 0)
//            }
//            is ApiErrorResponse -> {
//
//            }
//        }
    }
}