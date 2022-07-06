package com.susu.demoapplication.kotlin.method

import java.sql.DriverManager.println

/**
 * Author : sudan
 * Time : 2021/12/21
 * Description:
 */
inline fun inlined(getString: () -> String) {
    println(getString())
}

fun notInlined(getString: () -> String){
    println(getString())
}

