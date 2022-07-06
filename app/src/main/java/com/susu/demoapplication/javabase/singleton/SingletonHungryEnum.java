package com.susu.demoapplication.javabase.singleton;

/**
 * Author : sudan
 * Time : 2022/1/17
 * Description:这种方式是最简洁的，不需要考虑构造方法私有化。
 * 值得注意的是枚举类不允许被继承，因为枚举类编译后默认为final class，可防止被子类修改。
 * 常量类可被继承修改、增加字段等，容易导致父类的不兼容。
 * 枚举类型是线程安全的，并且只会装载一次，设计者充分的利用了枚举的这个特性来实现单例模式，枚举的写法非常简单，
 * 而且枚举类型是所有单例实现中唯一一种不会被破坏的单例实现模式
 */
public enum SingletonHungryEnum {
    INSTANCE;

    public void print() {
        System.out.println("这是通过枚举获得的实例" + "---" + "SingletonHungryEnum.print()");
    }

    public static void main(String[] args) {
        SingletonHungryEnum singleton = SingletonHungryEnum.INSTANCE;
        System.out.println(singleton);
        singleton.print();
    }
}
