package com.susu.demoapplication.kotlin.`object`

/**
 * Author : sudan
 * Time : 2021/11/25
 * Description: https://www.jianshu.com/p/812467365678
 *              在kotlin没有static关键字的概念,
 *              要使用静态方法可以使用伴生对象,或者使用顶层函数,
 *              不过顶层函数不能访问类中的私有成员,但伴生对象可以
 */
class CompanionObjectTest {


    private val str: String = "伴生对象可以访问我"

    companion object {

        /**
         * str 作为一个私有变量,伴生对象是有访问权限的.
         * 但是这里要注意,直接访问string这个类成员是不可以的,
         * 因为伴生对象的方法相当于一个static静态方法,
         * 而string是非静态的,所以要先创建对象才能访问
         */
        fun printStr() {
            print(CompanionObjectTest().str)

        }
    }
}

fun main(args: Array<String>) {
    CompanionObjectTest.printStr()
}