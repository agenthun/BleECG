package com.agenthun.bleecg.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.agenthun.bleecg.R;

/**
 * @project BleECG
 * @authors agenthun
 * @date 16/9/11 22:04.
 */
public class RoundHistogram extends View {
    private float value;
    private float MAX_VALUE = 20f;
    private float mRectHeight;
    private float mRectWidth;
    private float mRectHalfWidth;

    private float mWidth;
    private float mHeight;

    private Paint mPaint;
    private LinearGradient mLinearGradient;
    private Context mContext;

    public RoundHistogram(Context context) {
        this(context, null);
    }

    public RoundHistogram(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.textViewStyle);
    }

    public RoundHistogram(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        mContext = context;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        RectF rectF = new RectF(
                0,
                mHeight - mRectHeight,
                mRectWidth,
                mHeight);

        canvas.drawRoundRect(
                rectF,
                mRectHalfWidth,
                mRectHalfWidth,
                mPaint);
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;

        mRectWidth = mWidth;
        mRectHalfWidth = mRectWidth / 2;

        mLinearGradient = new LinearGradient(
                0,
                0,
                mWidth,
                mHeight,
                ContextCompat.getColor(mContext, R.color.colorAccent),
                ContextCompat.getColor(mContext, R.color.colorPrimary),
                Shader.TileMode.CLAMP);
        mPaint.setShader(mLinearGradient);
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
        mRectHeight = value / MAX_VALUE * 200;
    }
}
