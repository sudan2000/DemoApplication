package com.susu.demoapplication.algorithm;

import com.susu.baselibrary.utils.base.LogUtils;

/**
 * Author : sudan
 * Time : 2021/2/22
 * Description:
 */
public class Fibonacci {

    public static void main(String[] args) {
        int i = getResult2(8);
        LogUtils.print(i + "");
    }

    //递归
    private static int getResult(int n) {
        if (n == 0) {
            return 0;
        }
        if (n == 1) {
            return 1;
        }
        return getResult(n - 1) + getResult(n - 2);
    }

    //遍历
    private static int getResult2(int n) {
        if (n == 0) {
            return 0;
        }
        if (n == 1) {
            return 1;
        }
        int first = 0;
        int second = 1;
        int fibn = 0;
        for(int i = 2; i <= n; i++) {
            fibn = first + second;
            first = second;
            second = fibn;
        }
        return fibn;

    }


}
