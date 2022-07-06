package com.susu.demoapplication.kotlin.method

/**
 * Author : sudan
 * Time : 2021/12/21
 * Description:
 */
class InlineTest {

    fun test() {

        val testString = "testString"

        notInlined{test1()}
    }

    fun test1():String {
       return "a"
    }
}