package com.susu.baselibrary.view.customerview.textview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.susu.baselibrary.R;
import com.susu.baselibrary.utils.base.LogUtils;

/**
 * Author : sudan
 * Time : 2021/2/25
 * Description:
 */
public class TestTextView extends AppCompatTextView {

    private static final String TAG = "TestTextView";

    private Paint mPaint = null;
    private Context mContext;

    public TestTextView(Context context) {
        this(context, null, 0);
    }

    public TestTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TestTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }


    public void init() {
        //设置画笔
        mPaint = new Paint();
        mPaint.setColor(getResources().getColor(R.color.black));
        mPaint.setTextSize(40);
        mPaint.setTypeface(Typeface.DEFAULT_BOLD);  // 设置粗体
        mPaint.setAntiAlias(true);  // 消除锯齿
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(getResources().getColor(R.color.green));

        String drawStr = "g我f";
        Paint.FontMetrics fontMetrics1 = mPaint.getFontMetrics();
        float top1 = fontMetrics1.top;
        float bottom1 = fontMetrics1.bottom;
        float ascent1 = fontMetrics1.ascent;
        float descent1 = fontMetrics1.descent;
        // top:-42.246094 bottom:10.839844 ascent:-37.109375 descent:9.765625 centerY:137
        LogUtils.e(TAG, "top1:" + top1 + " bottom1:" + bottom1 + " ascent1:" + ascent1 + " descent1:" + descent1 + " centerY:" + getHeight() / 2);

        // 画笔, 这个坐标是左下角的坐标，错误计算方法，baseLine 盒子 中心点，文字不能居中
        canvas.drawText(drawStr, getWidth() / 2, getHeight() / 2, mPaint);

//-----------------------------------------------------------------------------------------------------------------
        // 这些坐标都是相对于baseLine的，top和ascent 是负数，在baseline的上面 ,bottom 、descent 是正数
        Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
        float top = fontMetrics.top;
        float bottom = fontMetrics.bottom;
        float ascent = fontMetrics.ascent;
        float descent = fontMetrics.descent;
        // top:-42.246094 bottom:10.839844 ascent:-37.109375 descent:9.765625 centerY:137
        LogUtils.e(TAG, "top:" + top + " bottom:" + bottom + " ascent:" + ascent + " descent:" + descent + " centerY:" + getHeight() / 2);
        float newBaseLineY = getHeight() / 2 + ((bottom - top) / 2 - bottom);
        //获取文本的宽度，和上面类似，但是是一个比较粗略的结果
        float measureText = mPaint.measureText(drawStr);
        LogUtils.e(TAG, "measureText=" + measureText);
        // X 轴居中，盒子/2-文字宽度的/2 坐标开始绘制 文本
        float newBaseLineX = getWidth() / 2 - measureText / 2;
        canvas.drawText(drawStr, newBaseLineX, newBaseLineY, mPaint);

    }
}
