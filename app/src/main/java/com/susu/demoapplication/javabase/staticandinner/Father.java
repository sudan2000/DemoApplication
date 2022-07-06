package com.susu.demoapplication.javabase.staticandinner;

import java.io.Serializable;

/**
 * Author : sudan
 * Time : 2021/12/9
 * Description:
 */
public class Father implements Serializable {

    public int a;
    public int b;

    public static void staticMethod(){
        System.out.println("父类static方法");
    }

    public void nonStaticMethod(){
        System.out.println("父类非static方法");
    }
}
