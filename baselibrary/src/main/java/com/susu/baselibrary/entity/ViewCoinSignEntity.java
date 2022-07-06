package com.susu.baselibrary.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Author : sudan
 * Time : 2021/4/30
 * Description:
 */
public class ViewCoinSignEntity {

    public List<CoinSignInfoEntity> mSignList = new ArrayList<>();
    public boolean todaySign; //今天是否签到
    public int signedDay;


}
