package com.susu.demoapplication.javabase.singleton;

/**
 * Author : sudan
 * Time : 2022/1/17
 * Description:
 */
public class SingletonLazyUnsafe {

    private static SingletonLazyUnsafe sINSTANCE;

    /**
     * 除枚举方式外, 其他方法都会通过反射的方式破坏单例,反射是通过调用构造方法生成新的对象，
     * 想要阻止单例破坏，可以在构造方法中进行判断，若已有实例, 则阻止生成新的实例
     */
    private SingletonLazyUnsafe() {
        if (sINSTANCE != null) {
            throw new RuntimeException("实例已经存在，请通过 getInstance()方法获取");
        }
    }

    public static SingletonLazyUnsafe getInstance() {
        if (sINSTANCE == null) {
            sINSTANCE = new SingletonLazyUnsafe();
        }
        return sINSTANCE;
    }

}
