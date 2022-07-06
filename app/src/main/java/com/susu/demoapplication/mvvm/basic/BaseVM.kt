package com.susu.demoapplication.mvvm.basic

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * Author : sudan
 * Time : 2020/12/10
 * Description:
 */

interface IViewModel {
    fun showLoading();
    fun hideLoading();
    fun showToast(text: String);
}

/**
 * internal 关键字为模块内部可见。
 */
internal class BaseVMCore : IViewModel, ViewModel() {
    val loadingLiveData by lazy {
        MutableLiveData<Boolean>()
    }

    val toastContent by lazy {
        MutableLiveData<String>()
    }

    override fun showLoading() {
        loadingLiveData.value = true
    }

    override fun hideLoading() {
        loadingLiveData.value = false
    }

    override fun showToast(text: String) {
        toastContent.value = text
    }

}

/**
 * open关键字：
 * 在java中允许创建任意的子类并重写方法任意的方法，除非显示的使用了final关键字进行标注。
 * 而在kotlin中它所有的类默认都是final的，不能被继承，而且类中所有的方法也是默认是final的
 * 为类增加open，class就可以被继承了
 * 为方法增加open，那么方法就可以被重写了
 */
open class BaseViewModel(application: Application) : AndroidViewModel(application), IViewModel {

    private var wrapper: IViewModel? = null

    internal fun attach(wrapper: IViewModel) {
        this.wrapper = wrapper;
    }

    override fun showLoading() {
        wrapper?.showLoading();
    }

    override fun hideLoading() {
        wrapper?.hideLoading()
    }

    override fun showToast(text: String) {
        wrapper?.showToast(text)
    }

}


