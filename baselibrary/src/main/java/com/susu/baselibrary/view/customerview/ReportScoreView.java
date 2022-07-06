package com.susu.baselibrary.view.customerview;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.susu.baselibrary.R;
import com.susu.baselibrary.utils.ui.ColorUtils;
import com.susu.baselibrary.utils.ui.DisplayUtils;

import static android.graphics.Paint.ANTI_ALIAS_FLAG;


public class ReportScoreView extends RelativeLayout {

    private TextView tvScore;
    private Context mContext;
    private final static int S_MAX_SCORE = 100;
    private int[] S_RING_COLORS;

    private final static int[] S_INDICATOR_COLORS = {
            R.color.color_8657ff,
            R.color.color_ff407e,
            R.color.color_ffba72,
            R.color.color_8cedf9,
            R.color.white};

    private int OFFSET;
    private final static float[] S_SHADER_POS = {0.0f, 0.25f, 0.5f, 0.75f, 1.0f};
    private int POINT_OUTER_W;
    private int POINT_INTER_W;
    private ValueAnimator mValueAnimator;
    private Paint mColorPaint;
    private int mScore = -1;
    private int mCurrentOffset;
    private int width;
    private int height;
    private float innerRadius;
    private RectF ringRectF = new RectF();

    public ReportScoreView(Context context) {
        this(context, null);
    }

    public ReportScoreView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ReportScoreView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
        initPaint();
        initView(context);
    }

    private void init() {
        S_RING_COLORS = new int[]{
                ColorUtils.getColor(mContext, R.color.color_ffdbb7),
                ColorUtils.getColor(mContext, R.color.color_ff4582),
                ColorUtils.getColor(mContext, R.color.color_714aff),
                ColorUtils.getColor(mContext, R.color.color_a0dbff),
                ColorUtils.getColor(mContext, R.color.color_ffdbb7)
        };
        OFFSET = DisplayUtils.dip2px(mContext, 6);
        POINT_OUTER_W = DisplayUtils.dip2px(mContext, 7);
        POINT_INTER_W = DisplayUtils.dip2px(mContext, 5);

    }

    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.report_score_layout, this, true);
        tvScore = view.findViewById(R.id.tvScore);
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        setWillNotDraw(false);
        setDrawingCacheEnabled(true);
    }

    private void initPaint() {
        if (mColorPaint == null) {
            mColorPaint = new Paint(ANTI_ALIAS_FLAG);
            mColorPaint.setStyle(Paint.Style.STROKE);
        }
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mValueAnimator != null) {
            mValueAnimator.cancel();
            mValueAnimator.removeAllUpdateListeners();
            mValueAnimator.removeAllListeners();
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (width != w || height != h) {
            width = w;
            height = h;
            innerRadius = width / 2 - OFFSET;
        }
    }

    /**
     * 得分倒计动画
     */
    private void startAnimation() {
        int scoreOffset = S_MAX_SCORE - mScore;
        int timeOffset = (scoreOffset / 25 + 1) * 500;
        mValueAnimator = ValueAnimator.ofInt(0, scoreOffset);
        mValueAnimator.setDuration(timeOffset);
        mValueAnimator.setInterpolator(new LinearInterpolator());
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mCurrentOffset = (int) valueAnimator.getAnimatedValue();
                tvScore.setText(String.valueOf(S_MAX_SCORE - mCurrentOffset));
            }
        });

        mValueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                checkFinalScore();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                checkFinalScore();
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mValueAnimator.start();
    }

    private void checkFinalScore() {
        if (tvScore == null) {
            return;
        }
        int score = S_MAX_SCORE - mCurrentOffset;
        if (score != mScore) {
            tvScore.setText(String.valueOf(mScore));
        }
    }

    public void setScore(int originScore) {
        setScore(originScore, true);
    }

    /**
     * 兼容负分数，限制最大分数100
     *
     * @param originScore
     * @param needAnimation 是否需要过渡动画
     */
    public void setScore(int originScore, boolean needAnimation) {
        int score = Math.abs(originScore);
        if (score > 100) {
            score = 100;
        }
        this.mScore = score;
        if (needAnimation) {
            startAnimation();
        } else {
            mCurrentOffset = S_MAX_SCORE - score;
            tvScore.setText(String.valueOf(score));
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (width == 0 || height == 0) {
            return;
        }
        drawOuterWhiteRing(canvas);
        drawIndicatorLine(canvas);
        drawIndicatorCircle(canvas);
    }

    /**
     * /**
     * 最外层白色圆环
     */
    private void drawOuterWhiteRing(Canvas canvas) {
        mColorPaint.reset();
        mColorPaint.setFlags(ANTI_ALIAS_FLAG);
        mColorPaint.setColor(ColorUtils.getColor(mContext, R.color.color_fcf8f9));
        mColorPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(getWidth() / 2, getWidth() / 2, getWidth() / 2, mColorPaint);
    }

    /**
     * 第二层彩色细线圆环
     */
    private void drawIndicatorLine(Canvas canvas) {
        ringRectF.right = getWidth() - OFFSET;
        ringRectF.bottom = getWidth() - OFFSET;
        ringRectF.left = OFFSET;
        ringRectF.top = OFFSET;

        float sweepAngle = 360 - mCurrentOffset * 3.6f;
        mColorPaint.reset();
        mColorPaint.setFlags(ANTI_ALIAS_FLAG);
        mColorPaint.setStyle(Paint.Style.STROKE);
        mColorPaint.setStrokeCap(Paint.Cap.ROUND);
        mColorPaint.setStrokeWidth(DisplayUtils.dip2px(getContext(), 6));
        mColorPaint.setShader(new SweepGradient(getWidth() / 2, getWidth() / 2, S_RING_COLORS, S_SHADER_POS));
        canvas.drawArc(ringRectF, -90f, sweepAngle, false, mColorPaint);
    }

    /**
     * 绘制圆点指示器
     */
    private void drawIndicatorCircle(Canvas canvas) {

        float arcValue = (float) (mCurrentOffset * 0.01f * 2 * Math.PI);
        float cx = (float) (getWidth() / 2 - Math.sin(arcValue) * innerRadius);
        float cy = (float) (getWidth() / 2 - Math.cos(arcValue) * innerRadius);

        mColorPaint.reset();
        mColorPaint.setFlags(ANTI_ALIAS_FLAG);
        mColorPaint.setColor(Color.WHITE);
        mColorPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(cx, cy, POINT_OUTER_W, mColorPaint);

        int indicatorColor = ColorUtils.getColor(getContext(), S_INDICATOR_COLORS[mCurrentOffset / 25]);
        mColorPaint.reset();
        mColorPaint.setFlags(ANTI_ALIAS_FLAG);
        mColorPaint.setColor(indicatorColor);
        mColorPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(cx, cy, POINT_INTER_W, mColorPaint);
    }


}
