package com.susu.baselibrary.view.customerview.textview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.Gravity;

import androidx.appcompat.widget.AppCompatTextView;

import com.susu.baselibrary.R;
import com.susu.baselibrary.utils.ui.DisplayUtils;

/**
 * Author : sudan
 * Time : 2021/2/24
 * Description:
 */
public class StrokeTextView extends AppCompatTextView {

    private TextPaint strokePaint;


    public StrokeTextView(Context context) {
        this(context, null, 0);
    }

    public StrokeTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StrokeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        strokePaint = new TextPaint();
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setColor(getResources().getColor(R.color.black));
        int strokeWidth = DisplayUtils.dip2px(getContext(), 3);
        strokePaint.setStrokeWidth(strokeWidth);
        // 复制原来TextView画笔中的一些参数
        TextPaint paint = getPaint();
        strokePaint.setTextSize(paint.getTextSize());
        strokePaint.setTypeface(paint.getTypeface());
        strokePaint.setFlags(paint.getFlags());
        strokePaint.setAlpha(paint.getAlpha());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 自定义描边效果
        String text = getText().toString();
        int gravity = getGravity();
        if ((gravity & Gravity.LEFT) == Gravity.LEFT) {
            canvas.drawText(text, 0f, getBaseline(), strokePaint);
        } else if ((gravity & Gravity.RIGHT) == Gravity.RIGHT) {
            canvas.drawText(text, getWidth() - strokePaint.measureText(text), getBaseline(), strokePaint);
        } else {
            canvas.drawText(text, (getWidth() - strokePaint.measureText(text)) / 2, getBaseline(), strokePaint);
        }


        super.onDraw(canvas);
    }
}
