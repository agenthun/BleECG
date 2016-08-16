package com.agenthun.bleecg.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Scroller;
import android.widget.TextView;

import com.agenthun.bleecg.R;

/**
 * @project BleECG
 * @authors agenthun
 * @date 16/8/9 00:00.
 */
public class HeartRateTextView extends TextView {
    private int rate;
    private static final int HEART_DEFAULT_RATE = 60;

    private int radius;
    private int MAX_RADIUS;
    private int wavelet;
    private int mWidth;
    private int mHeight;

    private Paint mPaint;
    private RadialGradient mRadialGradient;

    private Context mContext;


    public HeartRateTextView(Context context) {
        this(context, null);
    }

    public HeartRateTextView(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.textViewStyle);
    }

    public HeartRateTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        mContext = context;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(mWidth / 2, mHeight / 2, radius + wavelet, mPaint);

        if (++wavelet > MAX_RADIUS) {
            wavelet = -radius;
        }

        postInvalidateDelayed(5);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = getWidth();
        mHeight = getHeight();
        radius = (int) (Math.min(mWidth, mHeight) * 0.6 / 2);
        MAX_RADIUS = (int) (radius * 0.3);
        wavelet = -radius;

        mRadialGradient = new RadialGradient(
                mWidth / 2,
                mHeight / 2,
                (Math.min(mWidth, mHeight) * 0.6f / 2),
                ContextCompat.getColor(mContext, R.color.gray_a_300),
                ContextCompat.getColor(mContext, R.color.gray_a_50),
                Shader.TileMode.REPEAT);
        mPaint.setShader(mRadialGradient);
    }
}
