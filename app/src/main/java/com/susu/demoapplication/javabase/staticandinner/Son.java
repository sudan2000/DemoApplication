package com.susu.demoapplication.javabase.staticandinner;

import java.io.Serializable;

/**
 * Author : sudan
 * Time : 2021/12/9
 * Description:
 */
public class Son extends Father implements Serializable {


    public static void staticMethod() {
        System.out.println("子类static方法");
    }


    public void nonStaticMethod() {
        System.out.println("子类非static方法");
    }

    public static void main(String[] args) {
        Father father = new Son();
        father.nonStaticMethod();
        Father.staticMethod();
        Son.staticMethod();
    }
}
