package com.susu.demoapplication.kotlin.`object`

import java.io.File

/**
 * Author : sudan
 * Time : 2021/11/26
 * Description: 可以在类中使用声明对象,
 *              这样的对象在类中也是单一实例存在的,
 *              kotlin中可以理解成一个类中的单一实例,
 *              不随宿主类对象的不同而变化.
 *              这种类中嵌套一个类,用object关键字声明时一样可以用类名.对象的方法
 */
class ObjectDemo2 {

    private val a: Int = 1

    object FilePathComparator : Comparator<File> {
        override fun compare(file1: File, file2: File): Int {
            return file1.path.compareTo(file2.path)
        }



    }
}

fun main(args: Array<String>) {

}