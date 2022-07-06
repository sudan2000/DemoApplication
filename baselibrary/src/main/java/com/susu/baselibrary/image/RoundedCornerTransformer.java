package com.susu.baselibrary.image;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.os.Build.VERSION;

import androidx.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.TransformationUtils;
import com.bumptech.glide.util.Util;

import java.nio.ByteBuffer;
import java.security.MessageDigest;


/**
 * glide中设置四个角的transformation
 */
public class RoundedCornerTransformer extends BitmapTransformation {
    private static final String ID = RoundedCornerTransformer.class.getName();
    private static final byte[] ID_BYTES;
    private boolean leftTopEnable;
    private boolean leftBottomEnable;
    private boolean rightTopEnable;
    private boolean rightBottomEnable;
    private final int roundingRadius;
    private ScaleMode scaleMode;

    public RoundedCornerTransformer(int radius, boolean leftTopEnable, boolean leftBottomEnable, boolean rightTopEnable, boolean rightBottomEnable) {
        this.roundingRadius = radius;
        this.leftTopEnable = leftTopEnable;
        this.leftBottomEnable = leftBottomEnable;
        this.rightTopEnable = rightTopEnable;
        this.rightBottomEnable = rightBottomEnable;
    }

    public RoundedCornerTransformer(int radius, boolean leftTopEnable, boolean leftBottomEnable, boolean rightTopEnable, boolean rightBottomEnable, ScaleMode scaleMode) {
        this.roundingRadius = radius;
        this.leftTopEnable = leftTopEnable;
        this.leftBottomEnable = leftBottomEnable;
        this.rightTopEnable = rightTopEnable;
        this.rightBottomEnable = rightBottomEnable;
        this.scaleMode = scaleMode;
    }

    public RoundedCornerTransformer(int radius) {
        this(radius, true, true, true, true);
    }

    private Bitmap settleScaleMode(BitmapPool pool, Bitmap inBitmap, int outWidth, int outHeight) {
        if (this.scaleMode == null) {
            return inBitmap;
        }

        if (scaleMode == ScaleMode.CENTER_CROP) {
            return TransformationUtils.centerCrop(pool, inBitmap, outWidth, outHeight);

        } else if (scaleMode == ScaleMode.FIT_CENTER) {
            return TransformationUtils.fitCenter(pool, inBitmap, outWidth, outHeight);
        }
        return inBitmap;
    }

    protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap inBitmap, int outWidth, int outHeight) {
        Bitmap bitmap = settleScaleMode(pool, inBitmap, outWidth, outHeight);
        Config safeConfig = getAlphaSafeConfig(bitmap);
        Bitmap toTransform = getAlphaSafeBitmap(pool, bitmap);
        Bitmap result = pool.get(toTransform.getWidth(), toTransform.getHeight(), safeConfig);
        result.setHasAlpha(true);
        BitmapShader shader = new BitmapShader(toTransform, TileMode.CLAMP, TileMode.CLAMP);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(shader);
        RectF rect = new RectF(0.0F, 0.0F, (float) result.getWidth(), (float) result.getHeight());
        Canvas canvas = new Canvas(result);
        canvas.drawColor(0, Mode.CLEAR);
        canvas.drawRoundRect(rect, (float) this.roundingRadius, (float) this.roundingRadius, paint);
        if (!this.leftTopEnable) {
            canvas.drawRect(0.0F, 0.0F, (float) this.roundingRadius, (float) this.roundingRadius, paint);
        }

        if (!this.leftBottomEnable) {
            canvas.drawRect(0.0F, (float) (result.getHeight() - this.roundingRadius), (float) this.roundingRadius, (float) result.getHeight(), paint);
        }

        if (!this.rightTopEnable) {
            canvas.drawRect((float) result.getWidth(), 0.0F, (float) result.getWidth(), (float) this.roundingRadius, paint);
        }

        if (!this.rightBottomEnable) {
            canvas.drawRect((float) (result.getWidth() - this.roundingRadius), (float) (result.getHeight() - this.roundingRadius), (float) result.getWidth(), (float) result.getHeight(), paint);
        }

        canvas.setBitmap(null);
        if (!toTransform.equals(bitmap)) {
            pool.put(toTransform);
        }

        return result;
    }


    private static Bitmap getAlphaSafeBitmap(@NonNull BitmapPool pool, @NonNull Bitmap maybeAlphaSafe) {
        Config safeConfig = getAlphaSafeConfig(maybeAlphaSafe);
        if (safeConfig.equals(maybeAlphaSafe.getConfig())) {
            return maybeAlphaSafe;
        } else {
            Bitmap argbBitmap = pool.get(maybeAlphaSafe.getWidth(), maybeAlphaSafe.getHeight(), safeConfig);
            (new Canvas(argbBitmap)).drawBitmap(maybeAlphaSafe, 0.0F, 0.0F, null);
            return argbBitmap;
        }
    }

    @NonNull
    private static Bitmap.Config getAlphaSafeConfig(@NonNull Bitmap inBitmap) {
        return VERSION.SDK_INT >= 26 && Config.RGBA_F16.equals(inBitmap.getConfig()) ? Config.RGBA_F16 : Config.ARGB_8888;
    }


    public int hashCode() {
        return Util.hashCode(ID.hashCode(), Util.hashCode(this.roundingRadius, Util.hashCode(this.leftTopEnable, Util.hashCode(this.leftBottomEnable, Util.hashCode(this.rightTopEnable, Util.hashCode(this.rightBottomEnable))))));
    }

    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        messageDigest.update(ID_BYTES);
        byte[] radiusData = ByteBuffer.allocate(20).putInt(this.roundingRadius).putInt(4, this.leftTopEnable ? 1 : 0).putInt(8, this.leftBottomEnable ? 1 : 0).putInt(12, this.rightTopEnable ? 1 : 0).putInt(16, this.rightBottomEnable ? 1 : 0).array();
        messageDigest.update(radiusData);
    }

    static {
        ID_BYTES = ID.getBytes(CHARSET);
    }

}


