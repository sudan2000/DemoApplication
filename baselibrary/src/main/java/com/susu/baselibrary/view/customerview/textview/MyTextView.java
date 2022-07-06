package com.susu.baselibrary.view.customerview.textview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;

import com.susu.baselibrary.utils.base.LogUtils;

/**
 * Author : sudan
 * Time : 2021/5/13
 * Description:
 */
public class MyTextView extends androidx.appcompat.widget.AppCompatTextView {

    private static final String TAG = "MyTextView";

    public MyTextView(Context context) {
        super(context);
    }

    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        FontTop: -58      FontBottom: 16          FontDescent: 16     FontAscent: -58
//        TextViewTop: 0    TextViewBottom: 550
//        Baseline: 58
//        字内容的坐标系和TextView组件的坐标系是不一样的。字内容是以其父容器的baseline为原点的。
//        Top: 0        ------------------------------- FontTop: -58
//                      -------------------------------
//
//        baseline: 58  -------------------------------
//        Bottom: 74    ------------------------------- FontBottom: 16

        LogUtils.d(TAG, "FontBottom: " + getPaint().getFontMetricsInt().bottom +
                "  \nFontDescent: " + getPaint().getFontMetricsInt().descent +
                " \nFontAscent: " + getPaint().getFontMetricsInt().ascent +
                " \nFontTop: " + getPaint().getFontMetricsInt().top +
                " \nBaseline: " + getBaseline());


        LogUtils.d(TAG, "TextViewBottom: " + getBottom() +
                " \nTextViewTop: " + getTop() +
                " \nBaseline: " + getBaseline());

    }

    public int getCenterBaseLineY() {
        Paint.FontMetrics fontMetrics = getPaint().getFontMetrics();
        return (int) (getMeasuredHeight() / 2 - (fontMetrics.top + fontMetrics.bottom) / 2);
    }


}
