package com.susu.demoapplication.javabase.singleton;

/**
 * Author : sudan
 * Time : 2022/1/17
 * Description:在内部类被加载和初始化时 才创建实例
 * 静态内部类不会自动随着外部类的加载和初始化而初始化，它是要单独加载和初始化的。
 * 因为是在内部类加载和初始化时创建的 因此它是线程安全的
 *
 * 静态内部类又是如何实现线程安全的呢？
 * 类加载时机：JAVA虚拟机在有且仅有的5种场景下会对类进行初始化。
 * 1.遇到new、getstatic、setstatic或者invokestatic这4个字节码指令时，对应的java代码场景为：new一个关键字或者一个实例化对象时、读取或设置一个静态字段时(final修饰、已在编译期把结果放入常量池的除外)、调用一个类的静态方法时。
 * 2.使用java.lang.reflect包的方法对类进行反射调用的时候，如果类没进行初始化，需要先调用其初始化方法进行初始化。
 * 3.当初始化一个类时，如果其父类还未进行初始化，会先触发其父类的初始化。
 * 4.当虚拟机启动时，用户需要指定一个要执行的主类(包含main()方法的类)，虚拟机会先初始化这个类。
 * 5.当使用JDK 1.7等动态语言支持时，如果一个java.lang.invoke.MethodHandle实例最后的解析结果REF_getStatic、REF_putStatic、REF_invokeStatic的方法句柄，并且这个方法句柄所对应的类没有进行过初始化，则需要先触发其初始化。
 * 这5种情况被称为是类的主动引用，注意，这里《虚拟机规范》中使用的限定词是"有且仅有"，那么，除此之外的所有引用类都不会对类进行初始化，称为被动引用。静态内部类就属于被动引用的行列。
 *
 * 当getInstance()方法被调用时，SingletonHolder才在SingletonLazyInner的运行时常量池里，把符号引用替换为直接引用，这时静态对象sINSTANCE也真正被创建，然后再被getInstance()方法返回出去，这点同饿汉模式。
 * 那么INSTANCE在创建过程中又是如何保证线程安全的呢？
 * 虚拟机会保证一个类的<clinit>()方法在多线程环境中被正确地加锁、同步，如果多个线程同时去初始化一个类，那么只会有一个线程去执行这个类的<clinit>()方法，其他线程都需要阻塞等待，直到活动线程执行<clinit>()方法完毕。如果在一个类的<clinit>()方法中有耗时很长的操作，就可能造成多个进程阻塞(需要注意的是，其他线程虽然会被阻塞，但如果执行<clinit>()方法后，其他线程唤醒之后不会再次进入<clinit>()方法。同一个加载器下，一个类型只会初始化一次。)，在实际应用中，这种阻塞往往是很隐蔽的。
 *
 * 静态内部类也有着一个致命的缺点，就是传参的问题，由于是静态内部类的形式去创建单例的，故外部无法传递参数进去，例如Context这种参数
 *
 */
public class SingletonLazyInner {
    private SingletonLazyInner() {
    }


    private static class SingletonHolder {
        private static SingletonLazyInner sINSTANCE = new SingletonLazyInner();
    }

    public static SingletonLazyInner getInstance() {
        return SingletonHolder.sINSTANCE;
    }
}
