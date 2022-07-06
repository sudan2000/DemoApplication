package com.susu.baselibrary.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.TransformationUtils;
import com.susu.baselibrary.utils.ui.DisplayUtils;

import java.security.MessageDigest;

/**
 * Author : sudan
 * Time : 2021/5/18
 * Description:
 */
public class HeadImageForUserTransformation extends BitmapTransformation {
    private static final int VERSION = 1;
    private static final String ID = "com.susu.glide.HeadTransformationForUser." + VERSION;

    private int borderSize;
    private int borderColor;
    private boolean whiteInline = false;

    public HeadImageForUserTransformation(Context context) {
        if (context != null) {
            this.borderSize = DisplayUtils.px2dip(context, 8);
        }
        this.borderColor = Color.WHITE;
    }

    @Override
    protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
        Bitmap bitmap = TransformationUtils.circleCrop(pool, toTransform, outWidth, outHeight);
        setCanvasBitmapDensity(toTransform, bitmap);
        Paint paint = new Paint();
        paint.setColor(borderColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(borderSize);
        paint.setAntiAlias(true);

        Canvas canvas = new Canvas(bitmap);
        canvas.drawCircle(
                outWidth / 2f,
                outHeight / 2f,
                Math.max(outWidth, outHeight) / 2f - borderSize / 2f,
                paint
        );

        if (whiteInline) {
            paint.setColor(Color.WHITE);
            canvas.drawCircle(
                    outWidth / 2f,
                    outHeight / 2f,
                    Math.max(outWidth, outHeight) / 2f - 3 * borderSize / 2,
                    paint
            );
        }
        return bitmap;
    }

    public HeadImageForUserTransformation(int borderSize, @ColorInt int borderColor) {
        this.borderSize = borderSize;
        this.borderColor = borderColor;
    }

    public HeadImageForUserTransformation(int borderSize, @ColorInt int borderColor, boolean whiteInline) {
        this.borderSize = borderSize;
        this.borderColor = borderColor;
        this.whiteInline = whiteInline;
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        messageDigest.update((ID + borderSize + borderColor).getBytes(CHARSET));
    }

    void setCanvasBitmapDensity(@NonNull Bitmap toTransform, @NonNull Bitmap canvasBitmap) {
        canvasBitmap.setDensity(toTransform.getDensity());
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof HeadImageForUserTransformation &&
                ((HeadImageForUserTransformation) o).borderSize == borderSize &&
                ((HeadImageForUserTransformation) o).borderColor == borderColor;
    }

    @Override
    public int hashCode() {
        return ID.hashCode() + borderSize * 100 + borderColor + 10;
    }
}
