package com.susu.demoapplication.javabase.finaltest;

/**
 * Author : sudan
 * Time : 2020/12/30
 * 1.修饰类：当用final修饰一个类时，表明这个类不能被继承。
 * final类中的成员变量可以根据需要设为final，
 * 但是要注意final类中的所有成员方法都会被隐式地指定为final方法
 * 2.修饰方法：如果只有在想明确禁止 该方法在子类中被覆盖的情况下才将方法设置为final的
 * 3.修饰变量：如果是基本数据类型的变量，则其数值一旦在初始化之后便不能更改；如果是引用类型的变量，则在对其初始化之后便不能再让其指向另一个对象。
 */
public class FinalKeyword {

    public static void main(String[] args) {
        MyClass myClass = new MyClass();
        StringBuffer buffer = new StringBuffer("hello");
        myClass.changeValue(buffer);
        System.out.println("main方法中buffer：" + buffer.toString());
        myClass.changeValue2(buffer);
        System.out.println("main方法中buffer：" + buffer.toString());

        MyClass2 myClass2 = new MyClass2();
        StringBuffer buffer2 = new StringBuffer("hello");
        myClass2.changeValue(buffer2);
        System.out.println("main方法中buffer2：" + buffer2.toString());

        MyClass3 myClass3 = new MyClass3();
        int i = 9;
        myClass3.changeValue(i);
        System.out.println("main方法中i：" + i);
    }
}

/**
 * 方法changeValue和main方法中的变量i根本就不是一个变量，
 * 因为java参数传递采用的是值传递，
 * 对于基本类型的变量，相当于直接将变量进行了拷贝。
 * 所以即使没有final修饰的情况下，在方法内部改变了变量i的值也不会影响方法外的i。
 */
class MyClass3 {
//    void changeValue(final int i) {
//        i++;
//    }

    void changeValue(int i) {
        i++;
        System.out.println("changeValue内部的i：" + i);
    }
}

class MyClass {
    void changeValue(final StringBuffer buffer) {
        buffer.append("world");
        System.out.println("changeValue内部的buffer：" + buffer);
    }

    void changeValue2(StringBuffer buffer) {
        buffer.append("world");
        System.out.println("changeValue2内部的buffer：" + buffer);
    }
}

/**
 * 如果把final去掉了，然后在changeValue中让buffer指向了其他对象，也不会影响到main方法中的buffer，
 * 原因在于java采用的是值传递，对于引用变量，传递的是引用的值，
 * 也就是说让实参和形参同时指向了同一个对象，因此让形参重新指向另一个对象对实参并没有任何影响。
 */
class MyClass2 {
    void changeValue(StringBuffer buffer) {
        buffer = new StringBuffer("abc");
        System.out.println("changeValue内部的buffer：" + buffer);
    }
}
