package com.susu.demoapplication.javabase.singleton;

/**
 * Author : sudan
 * Time : 2022/1/17
 * Description:恶汉模式-静态常量，简洁直观
 * 不能实现懒加载，造成空间浪费。如果一个类比较大，我们在初始化的时就加载了这个类，但是我们长时间没有使用这个类，这就导致了内存空间的浪费。
 */
public class SingletonHungry {

    private SingletonHungry() {
    }

    private int num;

    public static final SingletonHungry sINSTANCE = new SingletonHungry();
}
