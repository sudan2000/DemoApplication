package com.susu.demoapplication.javabase.singleton;

import java.io.IOException;
import java.util.Properties;

/**
 * Author : sudan
 * Time : 2022/1/17
 * Description:这种方式和静态常量/变量类似，只不过把new放到了静态代码块里，从简洁程度上比不过第一种。
 * 但是把new放在static代码块有别的好处，那就是可以做一些别的操作，如初始化一些变量，从配置文件读一些数据等。
 */
public class SingletonStaticBlock {

    private String info;

    private SingletonStaticBlock() {
    }

    private SingletonStaticBlock(String info) {
        this.info = info;
    }

    public static SingletonStaticBlock sINSTANCE;

    static {
        Properties properties = new Properties();
        try {
            properties.load(SingletonStaticBlock.class.getClassLoader().getResourceAsStream("info.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        sINSTANCE = new SingletonStaticBlock(properties.getProperty("info"));
    }

}









