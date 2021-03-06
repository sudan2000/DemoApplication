package com.susu.baselibrary.location.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

import com.susu.baselibrary.R;
import com.susu.baselibrary.location.LocationConstants;
import com.susu.baselibrary.utils.ui.DialogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class ViewUtil {


    /**
     * 显示提示信息
     */
    public static void showPrivacyDialog(Activity activity) {
        DialogUtils.showCommonDialogCancelable(
                activity,
                "提示: ",
                activity.getResources().getString(R.string.privacy_permission_desc),
                "同意",
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        SharedPreferenceUtil
                                .putBoolean(LocationConstants.PERMISSIONS_DESC_KEY, true);
                    }
                }, "不同意",
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SharedPreferenceUtil
                                .putBoolean(LocationConstants.PERMISSIONS_DESC_KEY, false);
                    }
                }, false);
    }

    public static void setMargins(View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            view.requestLayout();
        }
    }

    public static void initSystemBar(Activity activity, int res) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(activity, true);
        }
        SystemBarTintManager tintManager = new SystemBarTintManager(activity);
        tintManager.setStatusBarTintEnabled(true);

        // 使用颜色资源
        tintManager.setStatusBarTintResource(res);

    }

//    public static void startActivityForResult(Activity fromActivity, Class<?> toClass, int requestCode) {
//        Intent intent = new Intent(fromActivity, toClass);
//        fromActivity.startActivityForResult(intent, requestCode);
//        fromActivity.overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
//    }
//
//    public static void startActivity(Activity fromActivity, Class<?> toClass) {
//        Intent intent = new Intent(fromActivity, toClass);
//        fromActivity.startActivity(intent);
//        fromActivity.overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
//    }

    /**
     * 设置屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public static void setBackgroundAlpha(Activity context, float bgAlpha) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        context.getWindow().setAttributes(lp);
    }

    @TargetApi(19)
    private static void setTranslucentStatus(Activity activity, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();

        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 调整Picker布局
     *
     * @param frameLayout
     */
    public static void resizePicker(FrameLayout frameLayout) {
        List<NumberPicker> numberPickers = findNumberPicker(frameLayout);
        for (NumberPicker numberPicker : numberPickers) {
            resizeNumberPicker(numberPicker);
        }
    }

    /**
     * 获取ViewGroup中的NumberPicker组件
     *
     * @param viewGroup
     * @return
     */
    private static List<NumberPicker> findNumberPicker(ViewGroup viewGroup) {
        List<NumberPicker> numberPickers = new ArrayList<>();
        View child;
        if (null != viewGroup) {
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                child = viewGroup.getChildAt(i);
                if (child instanceof NumberPicker) {
                    numberPickers.add((NumberPicker) child);
                } else if (child instanceof LinearLayout) {
                    List<NumberPicker> result = findNumberPicker((ViewGroup) child);
                    if (result.size() > 0) {
                        return result;
                    }
                }
            }
        }
        return numberPickers;
    }

    /**
     * 调整NumberPicker大小
     *
     * @param numberPicker
     */
    private static void resizeNumberPicker(NumberPicker numberPicker) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(150, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(15, 0, 15, 0);
        numberPicker.setLayoutParams(params);
    }

}
