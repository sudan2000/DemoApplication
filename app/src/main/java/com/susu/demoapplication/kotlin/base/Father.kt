package com.susu.demoapplication.kotlin.base

/**
 * Author : sudan
 * Time : 2021/11/25
 * Description:
 *      open:
 *          在java中允许创建任意的子类并重写方法任意的方法，除非显示的使用了final关键字进行标注。
 *          而在kotlin中它所有的类默认都是final的，而且在类中所有的方法也是默认是final的,除非使用open
 *      private等:
 *          在 Kotlin 中一切都是默认 public 的
 *
 */
open class Father  {
    //指定主构造函数不可访问，私有化

    private val a = "该属性不能被子类访问"
    protected var b = "该属性能被子类访问"
    internal var c = "同一模块下被访问"

    //在 Kotlin 中一切都是默认 public 的
    var d = "到处都可以被访问"


    open fun eat(food: String) {
        print("父亲吃食物：$food")
    }
}