package com.susu.demoapplication.javabase.transienttest;

import com.susu.baselibrary.utils.stream.StreamUtils;
import com.susu.demoapplication.javabase.staticandinner.Father;
import com.susu.demoapplication.javabase.staticandinner.Son;

/**
 * Author : sudan
 * Time : 2021/12/9
 * Description: 关键字transient
 *      在类的实例对象的序列化处理过程中会被忽略。
 *      因此，transient变量不会贯穿对象的序列化和反序列化，生命周期仅存于调用者的内存中而不会写到磁盘里进行持久化。
 *  (1) 一旦变量被transient修饰，变量将不再是对象持久化的一部分，该变量内容在序列化后无法被访问。
 * （2）transient关键字只能修饰变量，而不能修饰方法和类。
 *      注意，本地变量是不能被transient关键字修饰的。
 *      变量如果是用户自定义类变量，则该类需要实现Serializable接口。
 * （3）一个静态变量不管是否被transient修饰，均不能被序列化
 *      (如果反序列化后类中static变量还有值，则值为当前JVM中对应static变量的值)。
 *      序列化保存的是对象状态，静态变量保存的是类状态，因此序列化并不保存静态变量。
 *   使用场景：
 * （1）类中的字段值可以根据其它字段推导出来，如一个长方形类有三个属性长度、宽度、面积，面积不需要序列化。
 * （2）一些安全性的信息，一般情况下是不能离开JVM的。
 * （3）如果类中使用了Logger实例，那么Logger实例也是不需要序列化的
 *
 */
public class TransientTest {

    public static void main(String[] args) {
        User user = new User();
        user.setUserName("Alax");
        user.setPassword("123456");
        user.setUserSex("male");
        Father father1 = new Son();
        father1.a = 1;
        father1.b = 2;
        user.setFather(father1);
        Son son = new Son();
        son.a = 3;
        son.b = 4;
        user.setSon(son);

        System.out.println("read before Serializable");
        System.out.println("userName: " + user.getUserName());
        System.out.println("password: " + user.getPassword());
        System.out.println("father a: " + user.getFather().a);
        System.out.println("father b: " + user.getFather().b);
        System.out.println("son a: " + user.getSon().a);
        System.out.println("son b: " + user.getSon().b);
        System.out.println("userSex: " + user.getUserSex());

        StreamUtils.writeObject("./user.txt", user);
        User user1 = (User) StreamUtils.readObject("./user.txt");

        System.out.println("read after Serializable");
        System.out.println("userName: " + user1.getUserName());
        System.out.println("password: " + user1.getPassword());
        System.out.println("father a: " + user1.getFather().a);
        System.out.println("father b: " + user1.getFather().b);
        System.out.println("son : " + user1.getSon());
        System.out.println("userSex: " + user.getUserSex());

    }
}
