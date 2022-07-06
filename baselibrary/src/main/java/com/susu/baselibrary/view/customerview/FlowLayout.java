package com.susu.baselibrary.view.customerview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Author : sudan
 * Time : 2020/12/10
 * Description:
 */
public class FlowLayout extends ViewGroup {

    private List<List<View>> mAllViews = new ArrayList<>();

    private List<Integer> mLineHeight = new ArrayList<>();

    public FlowLayout(Context context) {
        this(context, null, 0);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        android.util.Log.d("susu", "measure******" );

        int width = 0;
        int height = 0;

        int lineWidth = 0;
        int lineHeight = 0;

        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);

            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();


            int childWidth = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            int childHeight = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;

            if (lineWidth + childWidth > widthSize) {
                width = Math.max(width, lineWidth);
                lineWidth = childWidth;
                height += lineHeight;
                lineHeight = childHeight;

            } else {
                lineWidth += childWidth;
                lineHeight = Math.max(lineHeight, childHeight);
            }

            if (i == childCount - 1) {
                width = Math.max(width, lineHeight);
                height += lineHeight;
            }

            setMeasuredDimension(widthMode == MeasureSpec.EXACTLY ? widthSize : width,
                    heightMode == MeasureSpec.EXACTLY ? heightSize : height);

        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        android.util.Log.d("susu", "layout******" );


        mAllViews.clear();
        mLineHeight.clear();

        int width = getWidth();
        int lineWidth = 0;
        int lineHeight = 0;

        List<View> lineViews = new ArrayList<>();
        int childCount = getChildCount();

        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            MarginLayoutParams lp2 = (MarginLayoutParams) getLayoutParams();
            int childWidth = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            int childHeight = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;

            if (childWidth + lineWidth > width) {
                mLineHeight.add(lineHeight);
                mAllViews.add(lineViews);
                lineHeight = childHeight;
                lineWidth = childWidth;
                lineViews = new ArrayList<>();

            } else {
                lineWidth += childWidth;
                lineHeight = Math.max(lineHeight, childHeight);
            }
            lineViews.add(child);
        }
        mLineHeight.add(lineHeight);
        mAllViews.add(lineViews);

        int left = 0;
        int top = 0;
        int lineCount = mAllViews.size();
        for (int i = 0; i < lineCount; i++) {
            lineViews = mAllViews.get(i);
            lineHeight = mLineHeight.get(i);

            for (int j = 0; j < lineViews.size(); j++) {
                View child = lineViews.get(j);
                if (child.getVisibility() == View.GONE) {
                    continue;
                }
                MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
                int cLeft = left + lp.leftMargin;
                int cTop = top + lp.topMargin;
                int cRight = cLeft + child.getMeasuredWidth();
                int cBottom = cTop + child.getMeasuredHeight();

                child.layout(cLeft, cTop, cRight, cBottom);
                left += child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            }
            left = 0;
            top += lineHeight;
        }
    }

//    @Override
//    public LayoutParams generateLayoutParams(AttributeSet attr) {
//        return new MarginLayoutParams(getContext(), attr);
//    }


//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//
//        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
//        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
//        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
//        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);
//
//        //如果当前ViewGroup的宽高为wrap_content的情况
//        int width = 0;//自己测量的 宽度
//        int height = 0;//自己测量的高度
//        //记录每一行的宽度和高度
//        int lineWidth = 0;
//        int lineHeight = 0;
//
//        //获取子view的个数
//        int childCount = getChildCount();
//        for (int i = 0; i < childCount; i++) {
//            View child = getChildAt(i);
//            //测量子View的宽和高
//            measureChild(child, widthMeasureSpec, heightMeasureSpec);
//            //得到LayoutParams
//            MarginLayoutParams lp = (MarginLayoutParams) getLayoutParams();
//            //子View占据的宽度
//            int childWidth = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
//            //子View占据的高度
//            int childHeight = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
//            if (lineWidth + childWidth > sizeWidth) {
//                //对比得到最大的宽度
//                width = Math.max(width, lineWidth);
//                //重置lineWidth
//                lineWidth = childWidth;
//                //记录行高
//                height += lineHeight;
//                lineHeight = childHeight;
//            } else {//不换行情况
//                //叠加行宽
//                lineWidth += childWidth;
//                //得到最大行高
//                lineHeight = Math.max(lineHeight, childHeight);
//            }
//            //处理最后一个子View的情况
//            if (i == childCount - 1) {
//                width = Math.max(width, lineWidth);
//                height += lineHeight;
//            }
//        }
//        //wrap_content
//        setMeasuredDimension(modeWidth == MeasureSpec.EXACTLY ? sizeWidth : width,
//                modeHeight == MeasureSpec.EXACTLY ? sizeHeight : height);
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//
//    }
//
//
//    @Override
//    protected void onLayout(boolean changed, int l, int t, int r, int b) {
//        // TODO Auto-generated method stub
//        mAllViews.clear();
//        mLineHeight.clear();
//        //获取当前ViewGroup的宽度
//        int width = getWidth();
//
//        int lineWidth = 0;
//        int lineHeight = 0;
//        //记录当前行的view
//        List<View> lineViews = new ArrayList<View>();
//        int childCount = getChildCount();
//        for (int i = 0; i < childCount; i++) {
//            View child = getChildAt(i);
//            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
//            int childWidth = child.getMeasuredWidth();
//            int childHeight = child.getMeasuredHeight();
//
//            //如果需要换行
//            if (childWidth + lineWidth + lp.leftMargin + lp.rightMargin > width) {
//                //记录LineHeight
//                mLineHeight.add(lineHeight);
//                //记录当前行的Views
//                mAllViews.add(lineViews);
//                //重置行的宽高
//                lineWidth = 0;
//                lineHeight = childHeight + lp.topMargin + lp.bottomMargin;
//                lineViews = new ArrayList();
//            }
//            lineWidth += childWidth + lp.leftMargin + lp.rightMargin;
//            lineHeight = Math.max(lineHeight, childHeight + lp.topMargin + lp.bottomMargin);
//            lineViews.add(child);
//        }
//        //处理最后一行
//        mLineHeight.add(lineHeight);
//        mAllViews.add(lineViews);
//
//        //设置子View的位置
//        int left = 0;
//        int top = 0;
//        //获取行数
//        int lineCount = mAllViews.size();
//        for (int i = 0; i < lineCount; i++) {
//            //当前行的views和高度
//            lineViews = mAllViews.get(i);
//            lineHeight = mLineHeight.get(i);
//            for (int j = 0; j < lineViews.size(); j++) {
//                View child = lineViews.get(j);
//                //判断是否显示
//                if (child.getVisibility() == View.GONE) {
//                    continue;
//                }
//                MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
//                int cLeft = left + lp.leftMargin;
//                int cTop = top + lp.topMargin;
//                int cRight = cLeft + child.getMeasuredWidth();
//                int cBottom = cTop + child.getMeasuredHeight();
//                //进行子View进行布局
//                child.layout(cLeft, cTop, cRight, cBottom);
//                left += child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
//            }
//            left = 0;
//            top += lineHeight;
//        }
//
//
//    }


}