package com.susu.demoapplication.kotlin.base

/**
 * Author : sudan
 * Time : 2021/11/23
 * Description:
 */
object ForTest {

    /**
     * 方式1 -----------------------------------------
     */
    fun traverse1() {
        for (index in 0..10) {
            print(index)
        }
    }

    /**
     * 方式2 -----------------------------------------
     */
    fun traverse2() {
        for (index in 10 downTo 0) {
            print(index)
        }
    }

    /**
     * 方式3 -----------------------------------------
     */
    fun traverse3() {
        for (index in 0..10 step 2) {
            print(index)
        }
    }

    /**
     * 方式4 创建一个不包含末尾元素的区间-----------------------------------------
     */
    fun traverse4() {
        for (index in 0 until 10) { //0123456789 不包含最后结尾
            print(index)
        }
    }

    /**
     * 遍历一个数组/列表，想同时取出下标和元素
     */
    fun traverseArray1() {
        val arr = arrayOf("a", "b", "c")
        for ((index, e) in arr.withIndex()) {
            print("下标：$index, 元素：$e")
        }
    }

    /**
     * 遍历一个数组/列表，只取出下标
     */
    fun traverseArray2() {
        val arr2 = arrayOf("a", "b", "c")
        for (index in arr2.indices) {
            print("下标：$index")
        }
    }

    /**
     * 遍历取元素：
     */
    fun traverseArray3() {
        val arr3 = arrayOf("a", "b", "c")
        for (element in arr3) {
            print("元素：$element")
        }
    }
}

fun main(args: Array<String>) {
    ForTest.traverse1()
    print("-------------")
    ForTest.traverse2()
    print("-------------")
    ForTest.traverse3()
    print("-------------")
    ForTest.traverse4()
    print("-------------")
    ForTest.traverseArray1()
    print("-------------")
    ForTest.traverseArray2()
    print("-------------")
    ForTest.traverseArray3()
    print("-------------")
}