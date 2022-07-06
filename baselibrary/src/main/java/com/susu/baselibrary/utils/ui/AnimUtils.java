package com.susu.baselibrary.utils.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * Author : sudan
 * Time : 2021/4/30
 * Description:
 */
public class AnimUtils {

    public static void animViewToViewOnWindow(final Activity activity, final View animView,
                                              View tagView, View toView,
                                              int duration, final AnimatorListenerAdapter listener) {


        Rect rect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);

        int[] finalPoint = new int[2];
        toView.getLocationOnScreen(finalPoint);

        int[] startPoint = new int[2];
        tagView.getLocationOnScreen(startPoint);

        int top = finalPoint[1] - rect.top + toView.getHeight() / 2 - DisplayUtils.getNavigationBarHeight(activity) - 20;
        ((FrameLayout.LayoutParams) animView.getLayoutParams()).setMargins(finalPoint[0], top, 0, 0);
        ((FrameLayout) activity.findViewById(android.R.id.content)).addView(animView);

        PropertyValuesHolder translationX = PropertyValuesHolder.ofFloat("translationX", startPoint[0] - finalPoint[0], 0);
        PropertyValuesHolder translationY = PropertyValuesHolder.ofFloat("translationY", startPoint[1] - finalPoint[1], 0);
        ObjectAnimator oa = ObjectAnimator.ofPropertyValuesHolder(animView, translationX, translationY).setDuration(duration);

        oa.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                ((ViewGroup) activity.findViewById(android.R.id.content)).removeView(animView);
                if (listener != null) {
                    listener.onAnimationEnd(animation);
                }
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                if (listener != null) {
                    listener.onAnimationStart(animation);
                }
            }
        });
        oa.start();
    }


    private static Bitmap getViewBitmap(View view) {
        view.destroyDrawingCache();
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.setDrawingCacheEnabled(true);
        Bitmap bitmap = view.getDrawingCache(true);
        return bitmap;
    }


}
