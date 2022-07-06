package com.susu.demoapplication.mvvm.basic

import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.view.LayoutInflaterCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.susu.baselibrary.activitybase.base.BaseActivity
import com.susu.demoapplication.BuildConfig
import com.susu.demoapplication.R
import com.susu.demoapplication.kotlin.IActionBarConfig
import kotlinx.android.synthetic.main.base_activity_root_container.*
import kotlinx.android.synthetic.main.base_view_action_bar.*

/**
 * Author : sudan
 * Time : 2020/12/10
 * Description:
 */

abstract class BaseVMActivity : BaseActivity(), IActionBarConfig {

    private val mCoreVM by lazy {
        ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(BaseVMCore::class.java)
    }

    fun <T : BaseViewModel> injectLazyVM(vmClz: Class<T>, factory: ViewModelProvider.Factory? = null) {
        lazy {
            initializeVM(vmClz, factory)
        }
    }

    private fun <T : BaseViewModel> initializeVM(vmClz: Class<T>, factory: ViewModelProvider.Factory? = null) =
            ViewModelProvider(this, factory
                    ?: ViewModelProvider.AndroidViewModelFactory.getInstance(application))
                    .get(vmClz).apply { attach(mCoreVM) }


    override fun onCreate(savedInstanceState: Bundle?) {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
        takeIf { BuildConfig.DEBUG }?.let {
            LayoutInflaterCompat.setFactory2(layoutInflater, object : LayoutInflater.Factory2 {
                override fun onCreateView(parent: View?, name: String, context: Context, attrs: AttributeSet): View? {
                    val startTime = System.currentTimeMillis();
                    val view = delegate.createView(parent, name, context, attrs)
                    Log.i("${componentName.className}页面布局创建耗时", "$name 消耗 ${System.currentTimeMillis() - startTime} ms")
                    return view;
                }

                override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? = null
            })
        }
        super.onCreate(savedInstanceState)

        setContentView(R.layout.base_activity_root_container);
        layoutInflater.inflate(injectLayoutID(), base_activity_root_container_fl, true)

        mCoreVM.apply {
            loadingLiveData.observe(this@BaseVMActivity, Observer { showLoading ->
                if (showLoading) {
                    this@BaseVMActivity.showLoading();
                } else {
                    this@BaseVMActivity.hideLoading();
                }
            })
            toastContent.observe(this@BaseVMActivity, Observer {
                this@BaseVMActivity.showToast(it)
            })

        }
        configActionBar()
        initView()
    }


    private var actionTitle: String? = null
    private var actionLeft: String? = null
    private var actionLeftClick: () -> Unit = { finish() }

    private fun configActionBar() {
        base_activity_action_bar_v.visibility = if (showActionBar()) View.VISIBLE else View.GONE
        base_view_action_bar_title_tv.text = actionTitle
        base_view_action_bar_back_tv.text = actionLeft ?: "返回"
        base_view_action_bar_back_tv.setOnClickListener { actionLeftClick() }
    }

    @LayoutRes
    abstract fun injectLayoutID(): Int

    abstract fun initView()

    override fun actionBarTitle(title: String) {
        actionTitle = title
    }

    override fun actionLeft(leftVal: String, click: () -> Unit) {
        actionLeft = leftVal
        actionLeftClick = click
    }

    override fun showActionBar(): Boolean = false


}