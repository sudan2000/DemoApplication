package com.susu.baselibrary.view.customerview;

import android.view.View;

/**
 * Author : sudan
 * Time : 2021/5/6
 * Description:
 */
public class ViewWrapper {
    private View rView;

    public ViewWrapper(View target) {
        rView = target;
    }

    public int getWidth() {
        return rView.getLayoutParams().width;
    }

    public void setWidth(int width) {
        rView.getLayoutParams().width = width;
        rView.requestLayout();
    }

    public int getHeight() {
        return rView.getLayoutParams().height;
    }

    public void setHeight(int height) {
        rView.getLayoutParams().height = height;
        rView.requestLayout();
    }

}
