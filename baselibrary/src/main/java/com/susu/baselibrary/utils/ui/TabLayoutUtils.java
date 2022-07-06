package com.susu.baselibrary.utils.ui;

import android.content.Context;
import android.view.View;

import com.google.android.material.tabs.TabLayout;

/**
 * Author : sudan
 * Time : 2020/12/28
 * Description:
 */
public class TabLayoutUtils {

    public static void dynamicSetTabLayoutMode(Context mContext, TabLayout tabLayout) {
        int tabWidth = calculateTabWidth(tabLayout);
        int screenWidth = mContext.getResources().getDisplayMetrics().widthPixels;

        if (tabWidth <= screenWidth) {
            tabLayout.setTabMode(TabLayout.MODE_FIXED);
        } else {
            tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        }
    }


    private static int calculateTabWidth(TabLayout tabLayout) {
        int tabWidth = 0;
        for (int i = 0; i < tabLayout.getChildCount(); i++) {
            final View view = tabLayout.getChildAt(i);
            view.measure(0, 0); // 通知父view测量，以便于能够保证获取到宽高
            tabWidth += view.getMeasuredWidth();
        }
        return tabWidth;
    }
}
