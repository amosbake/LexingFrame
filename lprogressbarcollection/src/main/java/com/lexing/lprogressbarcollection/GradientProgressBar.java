package com.lexing.lprogressbarcollection;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Shader;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.view.View;

/**
 * Author: mopel
 * Date : 2016/11/26
 */
public class GradientProgressBar extends View implements Runnable {

    private PorterDuffXfermode mXfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP);
    private Paint gradientPaint;
    private Paint bgPaint;
    private Paint textPaint;
    @ColorInt
    private int startColor;
    @ColorInt
    private int endColor;
    @ColorInt
    private int textColor;
    private int progress;
    private int max ;
    private int viewWidth;
    private Thread thread;

    /**
     * 左右来回移动的滑块
     */
    private Bitmap flikerBitmap;

    /**
     * 滑块移动最左边位置，作用是控制移动
     */
    private float flickerLeft;


    public GradientProgressBar(Context context) {
        this(context, null);
    }

    public GradientProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GradientProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
         TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.GradientProgressBar);
        try {
            textColor = ta.getColor(R.styleable.GradientProgressBar_percentTextColor,Color.WHITE);
            startColor = ta.getColor(R.styleable.GradientProgressBar_startColor,Color.parseColor("#b1f0fd"));
            endColor = ta.getColor(R.styleable.GradientProgressBar_startColor,Color.parseColor("#5ce2ff"));
            progress = ta.getInt(R.styleable.GradientProgressBar_progress,0);
            max = ta.getInt(R.styleable.GradientProgressBar_max,100);
        }finally {
            ta.recycle();
        }
        gradientPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(textColor);
        textPaint.setStrokeWidth(3);
        bgPaint.setColor(Color.parseColor("#eeeeee"));
        flikerBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.flicker);
        flickerLeft = -flikerBitmap.getWidth();
        thread = new Thread(this);
        thread.start();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewWidth = w - getPaddingLeft() - getPaddingRight();
        textPaint.setTextSize(h * 2 / 3);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawBackground(canvas);
        drawProgress(canvas);
        if (progress * 100 / max > 15) {
            drawProgressText(canvas);
        }
    }

    private void drawProgressText(Canvas canvas) {
        final int progressRight = getPaddingLeft() + viewWidth * progress / max;
        final int textLength = viewWidth / 10;
        final int offsetLength = viewWidth / 40;
        final String progressText = progress * 100 / max + "%";
        Rect targetRect = new Rect(progressRight - textLength - offsetLength, getPaddingTop(), progressRight - offsetLength, getHeight() - getPaddingBottom());
        Paint.FontMetricsInt fontMetrics = textPaint.getFontMetricsInt();
        int baseline = (targetRect.bottom + targetRect.top - fontMetrics.bottom - fontMetrics.top) / 2;
        textPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(progressText, targetRect.centerX(), baseline, textPaint);
    }

    private void drawProgress(Canvas canvas) {
        LinearGradient _linearGradient = new LinearGradient(getPaddingLeft(), getHeight() / 2, getPaddingLeft() + viewWidth * progress / max, getHeight() / 2, startColor, endColor, Shader.TileMode.CLAMP);
        gradientPaint.setShader(_linearGradient);
        canvas.save(Canvas.CLIP_SAVE_FLAG);
        Path _path = new Path();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            _path.addRoundRect(getPaddingLeft(), getPaddingTop(), getPaddingLeft() + viewWidth * progress / max, getHeight() - getPaddingBottom(), getHeight() / 2, getHeight() / 2, Path.Direction.CW);
            canvas.drawRoundRect(getPaddingLeft(), getPaddingTop(), getPaddingLeft() + viewWidth * progress / max, getHeight() - getPaddingBottom(), getHeight() / 2, getHeight() / 2, gradientPaint);
        } else {
            _path.addRect(getPaddingLeft(), getPaddingTop(), getPaddingLeft() + viewWidth * progress / max, getHeight() - getPaddingBottom(), Path.Direction.CW);
            canvas.drawRect(getPaddingLeft(), getPaddingTop(), getPaddingLeft() + viewWidth * progress / max, getHeight() - getPaddingBottom(), bgPaint);
        }

        canvas.clipPath(_path);
        gradientPaint.setShader(null);
        gradientPaint.setXfermode(mXfermode);
        canvas.drawBitmap(flikerBitmap, flickerLeft, 0, gradientPaint);
        gradientPaint.setXfermode(null);
        canvas.restore();
    }

    private void drawBackground(Canvas canvas) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            canvas.drawRoundRect(getPaddingLeft(), getPaddingTop(), getWidth() - getPaddingRight(), getHeight() - getPaddingBottom(), getHeight() / 2, getHeight() / 2, bgPaint);
            canvas.drawRoundRect(getPaddingLeft(), getPaddingTop(), getPaddingLeft() + viewWidth * progress / max, getHeight() - getPaddingBottom(), getHeight() / 2, getHeight() / 2, gradientPaint);
        } else {
            canvas.drawRect(getPaddingLeft(), getPaddingTop(), getWidth() - getPaddingRight(), getHeight() - getPaddingBottom(), bgPaint);
        }
    }

    public void setProgress(int progress) {
        this.progress = progress;
        postInvalidate();
    }

    @Override
    public void run() {
        int width = flikerBitmap.getWidth();
        try {
            while (!thread.isInterrupted()) {
//                flickerLeft += DensityUtils.dip2px(getContext(), 5);
                float progressWidth = (1.0f * progress / max) * viewWidth;
                if (flickerLeft >= progressWidth) {
                    flickerLeft = -width;
                }
                postInvalidate();
                Thread.sleep(30);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
