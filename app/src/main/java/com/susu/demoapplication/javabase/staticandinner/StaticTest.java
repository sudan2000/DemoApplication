package com.susu.demoapplication.javabase.staticandinner;

/**
 * Author : sudan
 * Time : 2021/11/22
 * Description:特点：
 * 1.静态内部类跟静态方法一样，只能访问静态的成员变量和方法，不能访问非静态的方法和属性，但是普通内部类可以访问任意外部类的成员变量和方法
 * 2.静态内部类可以声明普通成员变量和方法，而普通内部类不能声明static成员变量和方法。
 * 3.静态内部类可以单独初始化:
 * Inner i = new Outer.Inner();
 * 普通内部类初始化：
 * Outer o = new Outer();
 * Inner i = o.new Inner();
 */
public class StaticTest {

    private static String a = "外部A";

    private String b = "外部B";


    public static void main(String[] args) {
        StaticInnerClass innerClass = new StaticInnerClass();
        String aa = innerClass.getAInner();
        System.out.println("aa: " + aa);
    }

    public String getAOut() {
        a = "外部A";
        return a;
    }

    public String getBOut() {
        b = "外部B";
        return b;
    }

    public class InnerClass {

//        public static int c = 3;

        public String getAInner() {
            a = "非静态内部A";
            return a;
        }

        public String getBInner() {
            b = "非静态内部B";
            return b;
        }
    }


    /**
     * 静态内部类使用场景一般是当外部类需要使用内部类，而内部类无需外部类资源，
     * 并且内部类可以单独创建的时候会考虑采用静态内部类的设计。
     * (例如builder)
     */
    public static class StaticInnerClass {

        private int d = 4;

        public String getAInner() {
            a = "静态内部A";
            return a;
        }

        public String getBInner() {
            String b = "静态内部类获取不到外部非静态变量B";
            return b;
        }
    }

}
