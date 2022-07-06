package com.susu.demoapplication.kotlin.`object`

/**
 * Author : sudan
 * Time : 2021/1/14
 * Description: object 这个关键字定义一个类并同时创建个实例(也就是一个类对象)
 *              1.对象声明
 *
 */
/**
 *
 * 匿名对象可以用作只在本地和私有作用域中声明的类型。
 * 如果你使用匿名对象作为公有函数的返回类型或者用作公有属性的类型，
 * 那么该函数或属性的实际类型会是匿名对象声明的超类型，
 * 如果你没有声明任何超类型，就会是Any。在匿名对象中添加的成员将无法访问。
 */
class ObjectDemo {
    // 私有函数，所以其返回类型是匿名对象类型
    private fun foo() = object {
        val x: String = "x"
    }

    // 公有函数，所以其返回类型是 Any
    fun publicFoo() = object {
        val x: String = "x"
    }

    fun bar() {
        val x1 = foo().x        // 没问题
//        val x2 = publicFoo().x  // 错误：未能解析的引用“x”
    }
}








