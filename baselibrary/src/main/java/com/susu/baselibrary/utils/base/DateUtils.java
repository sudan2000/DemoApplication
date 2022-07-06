package com.susu.baselibrary.utils.base;

import java.util.Calendar;
import java.util.Date;

/**
 * Author : sudan
 * Time : 2021/4/29
 * Description:
 */
public class DateUtils {

    public static void main(String args[]) {
        getMonthDotDay();
    }

    public static void getMonthDotDay() {
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int month = c.get(Calendar.MONTH) + 1;
        int day = c.get(Calendar.DATE);
        System.out.println(month + "." + day + "------");
    }
}
