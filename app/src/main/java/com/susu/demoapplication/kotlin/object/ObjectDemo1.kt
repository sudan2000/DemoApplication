package com.susu.demoapplication.kotlin.`object`

import java.io.File

/**
 * Author : sudan
 * Time : 2021/11/26
 * Description: object使用1
 *              对象声明
 *              java中使用单例模式需要三步: 私有化构造方法,创建一个该类的实例,提供一个获取该实例的方法.
 *              而在kotlin中只需要object一个关键字即可
 */
object ObjectDemo1 : Comparator<File> {

    private var a: Int = 1

    override fun toString(): String {
        return "我是一个对象"
    }

    fun getA(): Int {
        return a++
    }

    override fun compare(file1: File, file2: File): Int {
        return file1.path!!.compareTo(file2.path)
    }

}

fun main(args: Array<String>) {
    println(ObjectDemo1.toString())
    println(ObjectDemo1.getA())
    println(ObjectDemo1.getA())
    println(ObjectDemo1.compare(File("/User/"), File("/user/")))
}