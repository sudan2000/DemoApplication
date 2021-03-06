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
//        //????????????ViewGroup????????????wrap_content?????????
//        int width = 0;//??????????????? ??????
//        int height = 0;//?????????????????????
//        //?????????????????????????????????
//        int lineWidth = 0;
//        int lineHeight = 0;
//
//        //?????????view?????????
//        int childCount = getChildCount();
//        for (int i = 0; i < childCount; i++) {
//            View child = getChildAt(i);
//            //?????????View????????????
//            measureChild(child, widthMeasureSpec, heightMeasureSpec);
//            //??????LayoutParams
//            MarginLayoutParams lp = (MarginLayoutParams) getLayoutParams();
//            //???View???????????????
//            int childWidth = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
//            //???View???????????????
//            int childHeight = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
//            if (lineWidth + childWidth > sizeWidth) {
//                //???????????????????????????
//                width = Math.max(width, lineWidth);
//                //??????lineWidth
//                lineWidth = childWidth;
//                //????????????
//                height += lineHeight;
//                lineHeight = childHeight;
//            } else {//???????????????
//                //????????????
//                lineWidth += childWidth;
//                //??????????????????
//                lineHeight = Math.max(lineHeight, childHeight);
//            }
//            //?????????????????????View?????????
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
//        //????????????ViewGroup?????????
//        int width = getWidth();
//
//        int lineWidth = 0;
//        int lineHeight = 0;
//        //??????????????????view
//        List<View> lineViews = new ArrayList<View>();
//        int childCount = getChildCount();
//        for (int i = 0; i < childCount; i++) {
//            View child = getChildAt(i);
//            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
//            int childWidth = child.getMeasuredWidth();
//            int childHeight = child.getMeasuredHeight();
//
//            //??????????????????
//            if (childWidth + lineWidth + lp.leftMargin + lp.rightMargin > width) {
//                //??????LineHeight
//                mLineHeight.add(lineHeight);
//                //??????????????????Views
//                mAllViews.add(lineViews);
//                //??????????????????
//                lineWidth = 0;
//                lineHeight = childHeight + lp.topMargin + lp.bottomMargin;
//                lineViews = new ArrayList();
//            }
//            lineWidth += childWidth + lp.leftMargin + lp.rightMargin;
//            lineHeight = Math.max(lineHeight, childHeight + lp.topMargin + lp.bottomMargin);
//            lineViews.add(child);
//        }
//        //??????????????????
//        mLineHeight.add(lineHeight);
//        mAllViews.add(lineViews);
//
//        //?????????View?????????
//        int left = 0;
//        int top = 0;
//        //????????????
//        int lineCount = mAllViews.size();
//        for (int i = 0; i < lineCount; i++) {
//            //????????????views?????????
//            lineViews = mAllViews.get(i);
//            lineHeight = mLineHeight.get(i);
//            for (int j = 0; j < lineViews.size(); j++) {
//                View child = lineViews.get(j);
//                //??????????????????
//                if (child.getVisibility() == View.GONE) {
//                    continue;
//                }
//                MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
//                int cLeft = left + lp.leftMargin;
//                int cTop = top + lp.topMargin;
//                int cRight = cLeft + child.getMeasuredWidth();
//                int cBottom = cTop + child.getMeasuredHeight();
//                //?????????View????????????
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