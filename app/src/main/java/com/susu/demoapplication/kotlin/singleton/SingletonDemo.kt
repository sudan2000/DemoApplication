package com.susu.demoapplication.kotlin.singleton

/**
 * Author : sudan
 * Time : 2021/1/14
 * Description: https://www.jianshu.com/p/5797b3d0ebd0
 */


object SingletonHungryDemo {}


class SingletonLazyDemo private constructor() {
    companion object {
        private var instance: SingletonLazyDemo? = null
            get() {
                if (field == null) {
                    field = SingletonLazyDemo()
                }
                return field
            }

        @Synchronized //线程安全
        fun get(): SingletonLazyDemo {
            return instance!!
        }
    }
}


class SingletonDoubleCheckDemo private constructor() {
    companion object {
        val instance: SingletonDoubleCheckDemo by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            SingletonDoubleCheckDemo()
        }
    }
}

class SingletonStaticInnerClassDemo private constructor() {

    companion object {
        val instance = SingletonHolder.holder
    }

    private object SingletonHolder {
        val holder = SingletonStaticInnerClassDemo()
    }
}

class SingletonDoubleCheckWithParamDemo private constructor(private val property: Int) {

    companion object {
        @Volatile
        private var instance: SingletonDoubleCheckWithParamDemo? = null
        fun getInstance(property: Int) {
            instance ?: synchronized(this) {
                instance ?: SingletonDoubleCheckWithParamDemo(property).also {
                    instance = it
                }
            }
        }
    }
}



class Demo {

    val ins = SingletonStaticInnerClassDemo.instance
}
















