package com.susu.demoapplication.kotlin.base

/**
 * Author : sudan
 * Time : 2021/11/25
 * Description:
 */
class Son : Father() {

    override fun eat(food: String) {
        print("儿子吃食物：$food")
    }

}