package com.susu.demoapplication.javabase;

import android.net.Uri;

import com.susu.demoapplication.javabase.singleton.SingletonLazyUnsafe;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Author : sudan
 * Time : 2020/12/30
 * Description:
 */
public class Test {
    public static void main(String[] args) {
//        getUriParam();
//        testClass();

        testSingletonUnsafe();

    }

    private static void testSingletonUnsafe() {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        Callable<SingletonLazyUnsafe> c1 = new Callable<SingletonLazyUnsafe>() {
            @Override
            public SingletonLazyUnsafe call() throws Exception {
                return SingletonLazyUnsafe.getInstance();
            }
        };
        Callable<SingletonLazyUnsafe> c2 = new Callable<SingletonLazyUnsafe>() {
            @Override
            public SingletonLazyUnsafe call() throws Exception {
                return SingletonLazyUnsafe.getInstance();
            }
        };
        Future<SingletonLazyUnsafe> submit1 = executorService.submit(c1);
        Future<SingletonLazyUnsafe> submit2 = executorService.submit(c2);
        try {
            SingletonLazyUnsafe singleton1 = submit1.get();
            SingletonLazyUnsafe singleton2 = submit2.get();
            System.out.println(singleton1);
            System.out.println(singleton2);
            System.out.println(singleton1 == singleton2);

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void testClass() {
        // 初始化Bean1
        Test test = new Test();
        Test.Bean1 bean1 = test.new Bean1();
        bean1.I++;
        // 初始化Bean2
        Bean2 bean2 = new Bean2();
        bean2.J++;
        //初始化Bean3
        Bean bean = new Bean();
        Bean.Bean3 bean3 = bean.new Bean3();
        bean3.k++;
    }

    class Bean1 {
        public int I = 0;
    }

    static class Bean2 {
        public int J = 0;
    }

    private static void getUriParam() {
        String url = "wynative://wechat/miniProgram?type=0&platform=weixin&originId=gh_xxcagadasgs&path=ghshkshk/jksjkj";
        Uri uri = Uri.parse(url);
        String type = uri.getQueryParameter("type");
        System.out.println("type: " + type);

        String platform = uri.getQueryParameter("platform");
        System.out.println("platform: " + platform);

        String originId = uri.getQueryParameter("originId");
        System.out.println("originId: " + originId);

        String path = uri.getQueryParameter("path");
        System.out.println("path: " + path);
    }
}

class Bean {
    class Bean3 {
        public int k = 0;
    }
}

