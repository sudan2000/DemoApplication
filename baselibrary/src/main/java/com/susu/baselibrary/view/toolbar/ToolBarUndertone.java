package com.susu.baselibrary.view.toolbar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.susu.baselibrary.R;
import com.susu.baselibrary.utils.base.LogUtils;

/**
 * Description: Toolbar 自定义可缩放底色控件
 *
 */
public class ToolBarUndertone extends View {

    private static final String TAG = ToolBarUndertone.class.getSimpleName();

    private Paint mPaint;

    private int mMinHeight;

    private int mParentWidth;

    private int mParentHeight;

    public ToolBarUndertone(Context context) {
        this(context, null);
    }

    public ToolBarUndertone(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        mPaint = new Paint();
        mPaint.setColor(context.getResources().getColor(R.color.bg_toolbar_color));

        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(1f);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true); //防止抖动

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        ViewGroup viewGroup = (ViewGroup) getParent();
        setMeasuredDimension(viewGroup.getMeasuredWidth(), viewGroup.getMeasuredHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mRectF.set(0,0,mParentWidth,mMinHeight);
        //画上半部分矩形
        canvas.drawRect(mRectF, mPaint);

        //画下班部分圆弧，并填充
        canvas.drawCircle(mPoint.pointX, mPoint.pointY, mPoint.radius, mPaint);

    }

    //圆实体类
    private CirclePoint mPoint = new CirclePoint();

    private RectF mRectF = new RectF();


    //根据偏移量，计算需要画圆弧的圆的半径，和需要定位的圆中心坐标
    public void pullToTop(float percent, int parentWidth, int parentHeight, int minHeight) {

        if (mParentWidth == 0) {
            mParentWidth = parentWidth;

        }
        if (mParentHeight == 0) {
            mParentHeight = parentHeight;

        }
        if (mMinHeight == 0) {
            mMinHeight = minHeight;
            LogUtils.d(TAG, " parent min height : " + mMinHeight);
        }

        int abos = (int)((mParentHeight - mMinHeight) * percent);


        int tmp = mParentHeight - mMinHeight - abos;
        //半径大小
        if (tmp > 5) {
            mPoint.radius = (mParentWidth * mParentWidth - 4 * tmp * tmp) / (8 * tmp) + tmp;
        }

        //圆心X坐标
        mPoint.pointX = mParentWidth / 2;

        //圆心Y坐标
        mPoint.pointY = (mParentHeight - abos) - mPoint.radius;

        LogUtils.d(TAG, "abos : " + abos + " ; tmp : " + tmp + " ;radius :" + mPoint.radius + " ; pointX : " + mPoint.pointX
                + " ; pointY : " + mPoint.pointY);

        //刷新
        postInvalidate();
    }


    class CirclePoint {
        //X坐标
        public float pointX;

        //Y坐标
        public float pointY;

        //半径
        public float radius;
    }


}
