package com.susu.demoapplication.kotlin.method

import android.widget.TextView
import com.susu.baselibrary.utils.system.CoreUtils
import java.sql.DriverManager.println
import java.util.*

/**
 * Author : sudan
 * Time : 2021/12/21
 * Description: https://blog.csdn.net/qq_23626713/article/details/90698341
 * 如果一个函数不返回Unit或者Nothing，那么尽可能让它成为一个纯函数（即没有副作用的函数）。 这是函数式编程的约定。
 */
//无返回值函数
fun function() {
    println("This is a function")
}

fun functionWithParams(obj: Any?) {
    println("You have passed an object to this function: $obj")
}

fun functionWithReturnValue(): Int {
    return Random().nextInt()
}

//如果一个函数不返回东西，你可以不写返回值。也可以让它显示返回Unit
fun functionReturnsUnit(): Unit {
    //    return
    //    return Unit
}

//如果一个函数不会返回, 也就是说只要调用这个函数，那么在它返回之前程序肯定GG了（比如一定会抛出异常的函数）
//因此你也不知道返回值该写啥，那么你可以让它返回Nothing：
fun functionReturnsNothing(): Nothing {
    throw RuntimeException("")
}

//一个函数也可以拥有Java风格的泛型参数：
fun <T> functionWithGenericsParam(t: T): T {
    return t
}

fun functionWithDefaultParams(limit: Int = 10): Int {
    for (i in 0..limit) {
        println(i.toString())
    }
    return limit
}

//如果一个函数的函数体只需要一个表达式就可以计算出来,可以用下面表达式
//fun functionReturningIncreasedInteger(num: Int): Int {
//    return num + 1
//}
fun functionReturningIncreasedInteger(num: Int) = num + 1

//内部函数 :函数里面也可以定义函数
//内部函数可以直接访问外部函数的局部变量、常量，Java是做不到。 递归也没有任何问题
fun functionWithAnotherFunctionInside() {
    val valuesInTheOuterScope = "Kotlin is awesome"
    fun theFunctionInside(int: Int = 10) {
        println(valuesInTheOuterScope)
        if (int >= 5) {
            theFunctionInside(int - 1)
        }
    }
    theFunctionInside()
}

//------------------------------------------------方法----------------------------------------------
//方法是一种特殊的函数，它必须通过类的实例调用，也就是说每个方法可以在方法内部拿到这个方法的实例。这是方法和函数的不同之处。
fun aFunction() = Unit

class AClass {
    fun aMethod() = Unit
}
//--------------------------------------------------------------------------------------------------
/**
 * 中缀表达式：infix修饰符
 * 一个方法如果在声明时有一个infix修饰符，那么它可以使用中缀语法调用。
 * 所谓中缀语法就是不需要点和括号的方法调用：
 */
class InfixClass {
    infix fun step(step: Int) = Unit
}
//--------------------------------------------------------------------------------------------------
/**
 * 操作符重载
 * 中缀表达式可以在一定程度上简化函数调用的代码。如果说中缀表达式还不够简洁，那么你一定需要操作符重载了。
 * Kotlin的操作符重载的规则是：
 *      该方法使用operator修饰符
 *      该方法的方法名必须被声明为特定的名称，以将对应的操作符映射为这个函数的调用
 *      参数必须符合该操作符的规定，比如+的重载就不能有多于一个（不含）的参数，也不能为空参数。
 */
//举个例子，我们要重载A类的+运算符。注意三个规定（函数名、参数得符合规矩，加operator修饰）：
class OperatorClass {
    operator fun plus(operatorClass: OperatorClass) {
        println("invoking plus")
    }

    operator fun minus(operatorClass: OperatorClass) = Unit
    operator fun times(operatorClass: OperatorClass) = Unit
    operator fun div(operatorClass: OperatorClass) = Unit
    operator fun rem(operatorClass: OperatorClass) = Unit

    //    operator fun mod(operatorClass: OperatorClass) = Unit
    operator fun rangeTo(operatorClass: OperatorClass) = Unit
    operator fun get(index: Int) = Unit
    operator fun get(index1: Int, index2: Int) = Unit
    operator fun set(index: Int, value: Any?) = Unit
    operator fun set(index1: Int, index2: Int, value: Any?) = Unit

    //        operator fun invoke(obj: Any?)
    operator fun inc() = OperatorClass()
    operator fun dec() = OperatorClass()
    operator fun unaryPlus() = OperatorClass()
    operator fun unaryMinus() = OperatorClass()
    operator fun compareTo(other: Any?) = 0
    override operator fun equals(other: Any?) = false
    operator fun contains(element: Any?) = false
    operator fun iterator() = object : Iterator<Any> {
        override operator fun hasNext() = false
        override operator fun next() = Unit
    }
}

/**
 * Lambda表达式
 * 把Lambda抽象为了一种类型，而Java只是单方法匿名接口实现的语法糖
 */
fun plus(param: Int) = { b: Int -> param + b }

fun main(args: Array<String>) {
    for (i in 1..10 step 2) {
        //...
    }
    val infixClass = InfixClass()
    infixClass step 2

    //----------------------------------------------
    val plusTest = OperatorClass() + OperatorClass()
    val plusTest2 = OperatorClass().plus(OperatorClass())//两种写法都可以

    //----------------------lambda------------------------
    //Lambda表达式最常用的地方之一是为GUI控件设置监听器：
    val textView = TextView(CoreUtils.getApp());
    textView.setOnClickListener {
        (it as TextView).let{ tv->
            tv.text = "我是TextView"
        }
    }

    //还有调用集合框架抽象出来的高阶函数
    (1..10).forEach {
        println(it)
    }
    //首先，我们有无参数的Lambda表达式：
//    { }
    //你可以直接把它写在外面，并且把它当成对象使用。Lambda对象有缺省的invoke函数的实现，也就是调用这个Lambda：
//    { println("aaa") }.invoke()
//    { println("aaa") }()

    //Lambda变量的签名不需要写参数名，只需要写类型。Lambda的返回值就是最后一个表达式的返回值
    val value: (String, Int) -> Int = { str: String, int: Int ->
        println("num = $int, str = $str")
        int
    }
    value("nihao", 239) + 1

    plus(222)(333)



}