package com.susu.demoapplication.javabase.staticandinner;

/**
 * Author : sudan
 * Time : 2021/11/22
 * Description:
 *
 *  静态内部类使用场景一般是当外部类需要使用内部类，而内部类无需外部类资源，
 *  并且内部类可以单独创建的时候会考虑采用静态内部类的设计。
 *  (例如builder)
 *
 */
public class Outer {
    private String name;
    private int age;

    public static class Builder {
        private String name;
        private int age;

        public Builder(int age) {
            this.age = age;
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withAge(int age) {
            this.age = age;
            return this;
        }

        public Outer build() {
            return new Outer(this);
        }
    }

    private Outer(Builder b) {
        this.age = b.age;
        this.name = b.name;
    }
}
