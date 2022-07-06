package com.susu.demoapplication.javabase.inner;

/**
 * 内部类对外部类的引用
 * 静态内部类，非静态内部类，匿名内部类
 */
public class Outer {
    private int count;
    /**
     * 匿名内部类1 编译后
     * class Outer$1 extends Outer.StaticInner {
     *      Outer$1(Outer paramOuter) {
     *          super(null);
     *      }
     *      public void doAction() {
     *          Outer.access$108(this.this$0);
     *      }
     * }
     * 这个类代表我们声明成员变量si1的匿名内部类,
     * 可以看到它继承自静态内部类StaticInner,它还定义了一个构造函数,
     * 并传入了内部类的实例作为参数,虽然不知道super(null)具体实现,
     * 但是我们知道它要访问paramOuter实例的成员变量,
     * 必须得使用它的引用来访问,所以它肯定是持有了外部类实例的引用.
     */
    private StaticInner si1 = new StaticInner() {
        @Override
        public void doAction() {
            count++;

        }
    };

    private StaticInner si2;

    private Inner i3;


    public void setInner(StaticInner inner) {
        this.si2 = inner;
    }


    public Outer() {
        i3 = new Inner();
        /**
         * 匿名内部类2
         * class Outer$2 extends Outer.StaticInner {
         *   Outer$2(Outer paramOuter) {
         *     super(null);
         *   }
         *   public void doAction() {
         *     super.doAction();
         *     Outer.access$108(this.this$0);
         *   }
         * }
         * 这个和上面提到的Outer$1.class类似,
         * 所以这两种匿名内部类是没有任何本质区别的,
         * 不管是定义成员变量还是方法传参.
         */
        setInner(new StaticInner() {
            @Override
            public void doAction() {
                super.doAction();
                count++;
            }
        });
    }

    private void setParam(final int count) {
        setInner(new StaticInner() {
            @Override
            public void doAction() {
                super.doAction();
                Outer.this.count = count;
            }
        });
    }

    public void doAction() {
        si1.doAction();
        si2.doAction();
        i3.doSomething();
    }


    /**
     * 内部类
     * class Outer$Inner {
     *      private Outer$Inner(Outer paramOuter) {}
     *      public void doSomething() {
     *          Outer.access$108(this.this$0);
     *      }
     * }
     * 这是定义的内部类,可以看到编译器也为它单独生成了一个.class文件,
     * 构造函数被定义为私有的,且同样传入了外部类的实例,
     * 所以它也是持有了外部类实例的引用.
     */
    private class Inner {

        public void doSomething() {
            count++;
        }
    }


    /**
     * 静态内部类
     * class Outer$StaticInner
     * {
     *   public void doAction() {}
     * }
     * 这是静态内部类编译后的字节码文件,编译器并没有为它添加额外的构造函数,
     * 所以它其实和我们的外部类没有任何关系,这是写在同一个.java源文件中而已.
     */
    private static class StaticInner {

        public void doAction() {


        }
    }
}
