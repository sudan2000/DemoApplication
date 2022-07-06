package com.susu.demoapplication.javabase.singleton;

/**
 * Author : sudan
 * Time : 2022/1/17
 * Description:双重检验懒汉模式，通过同步代码块控制并发创建实例，并且采用双重检验，
 * 当两个线程同时执行第一个判空时，都满足的情况下，都会进来，然后去争锁，
 * 假设线程1拿到了锁，执行同步代码块的内容，创建了实例并返回，
 * 此时线程2又获得锁，执行同步代码块内的代码，因为此时线程1已经创建了，所以线程2虽然拿到锁了，
 * 如果内部不加判空的话，线程2会再new一次，导致两个线程获得的不是同一个实例。线程安全的控制其实是内部判空在起作用
 * 那为什么还要加外层判空的呢？
 * 内层判空已经可以满足线程安全了，加外层判空的目的是为了提高效率。
 * 因为可能存在这样的情况：线程1拿到锁后执行同步代码块，在new之后，还没有释放锁的时候，
 * 线程2过来了，它在等待锁（此时线程1已经创建了实例，只不过还没释放锁，线程2就来了），
 * 然后线程1释放锁后，线程2拿到锁，进入同步代码块汇总，判空，返回。
 * 这种情况线程2是不是不用去等待锁了？所以在外层又加了一个判空就是为了防止这种情况，线程2过来后先判空，不为空就不用去等待锁了，这样提高了效率。
 */
public class SingletonLazyDoubleCheck {

    /**
     * 双重检查锁模式是一种非常好的单例实现模式，解决了单例、性能、线程安全问题，上面的双重检测锁模式看上去完美无缺，其实是存在问题，
     * 在多线程的情况下，可能会出现空指针问题，出现问题的原因是JVM在实例化对象的时候会进行优化和指令重排序操作。
     * sINSTANCE = new SingletonLazySafe();操作并不是一个原子指令，会被分割成多个指令：
     * 1 memory = allocate(); //1：分配对象的内存空间
     * 2 ctorInstance(memory); //2：初始化对象
     * 3 instance = memory; //3：设置instance指向刚分配的内存地址
     * 经过指令重排
     * 1 memory = allocate(); //1：分配对象的内存空间
     * 2 instance = memory; //3：设置instance指向刚分配的内存地址，此时对象还没被初始化
     * 3 ctorInstance(memory); //2：初始化对象
     * volatile有内存屏障的功能
     */
    private static volatile SingletonLazyDoubleCheck sINSTANCE;//防止指令重排


    private SingletonLazyDoubleCheck() {
    }

    public static SingletonLazyDoubleCheck getInstance() {
        if (sINSTANCE == null) {
            synchronized (SingletonLazyDoubleCheck.class) {
                if (sINSTANCE == null) {
                    sINSTANCE = new SingletonLazyDoubleCheck();
                }
            }
        }
        return sINSTANCE;
    }




}
