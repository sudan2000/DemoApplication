package com.susu.demoapplication.kotlin

/**
 * Author : sudan
 * Time : 2020/12/10
 * Description:
 */
interface IActionBarConfig {
    fun showActionBar(): Boolean = false;
    fun actionBarTitle(title: String)
    fun actionLeft(leftVal: String, click: () -> Unit)

}