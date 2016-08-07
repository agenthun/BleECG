package com.agenthun.bleecg.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.agenthun.bleecg.R;

/**
 * @project BleECG
 * @authors agenthun
 * @date 16/8/7 01:43.
 */
public class HistogramView extends View {
    private static final String TAG = "HistogramView";

    private int mWidth;
    private int mRectSpace;
    private int mRectHeight;
    private int mRectWidth;
    private int mRectHalfWidth;
    private int mRectCount = 9;
    private int offset = 0;

    private int mDataLength = 20;
    float[] currentHeight;

    private Paint mPaint;

    private LinearGradient mLinearGradient;

    private Context mContext;

    public HistogramView(Context context) {
        super(context);
        initView(context);
    }

    public HistogramView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public HistogramView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        mContext = context;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    private void initData() {
        currentHeight = new float[mDataLength];
        for (int i = 0; i < mDataLength; i++) {
            currentHeight[i] = (i + 1.0f) / (mDataLength * 2) * mRectHeight;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        RectF rectF = null;

        for (int i = 0; i < mRectCount; i++) {
            float index = mRectSpace * i;

            rectF = new RectF(
                    index - mRectHalfWidth,
                    currentHeight[(i + offset) % mDataLength],
                    index + mRectHalfWidth,
                    mRectHeight * 0.9f);

            canvas.drawRoundRect(
                    rectF,
                    mRectHalfWidth,
                    mRectHalfWidth,
                    mPaint);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = getWidth();
        mRectHeight = getHeight();
        mRectSpace = mWidth / (mRectCount - 1);
        mRectWidth = (int) (mRectSpace * 0.4f);
        mRectHalfWidth = mRectWidth / 2;

        mLinearGradient = new LinearGradient(
                0,
                0,
                mRectWidth,
                mRectHeight,
                ContextCompat.getColor(mContext, R.color.colorAccent),
                ContextCompat.getColor(mContext, R.color.colorPrimary),
                Shader.TileMode.CLAMP);
        mPaint.setShader(mLinearGradient);

        initData();
    }

    private int mLastX;
    private int mStart;
    private int mEnd;

//    private float velocityX = 0;
//    private float velocityY = 0;
//    private float frictionX = 1;
//    private float frictionY = 1;
//    private float mFrictionForceX = 10;
//    private float mFrictionForceY = 10;
//
//    private float mLastX = Integer.MAX_VALUE;
//    private float mLastY = Integer.MAX_VALUE;
//    private float downX;
//    private float downY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
//        float y = event.getY();
/*        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastX = x;
                mLastY = y;
                downX = x;
                downY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                velocityX = (mLastX - x);
                velocityY = (mLastY - y);

                mLastX = x;
                mLastY = y;
                break;
            case MotionEvent.ACTION_UP:
                if (Math.abs(downX-x)<mTouchSlop && Math.abs(downY-y)<mTouchSlop)
                break;
        }*/
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastX = x;
                mStart = getScrollX();
                Log.d(TAG, "onTouchEvent() returned: ACTION_DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                int dx = mLastX - x;
                Log.d(TAG, "onTouchEvent() returned: getScrollX() = " + getScrollX());
                if (getScrollX() < 0) {
                    dx = mRectHalfWidth;
                }
                if (getScrollX() > mRectSpace * mRectCount) {
                    dx = -mRectSpace * mRectCount;
                }
                scrollBy(dx, 0);
                offset = getScrollX() / mRectSpace + 1;
                mLastX = x;
                Log.d(TAG, "onTouchEvent() returned: ACTION_MOVE dx= " + dx);
                break;
            case MotionEvent.ACTION_UP:
                mEnd = getScrollX();
                int dScrollX = mEnd - mStart;
                if (dScrollX > 0) {
                    if (dScrollX < mRectSpace / 2) {
                        Log.d(TAG, "onTouchEvent: 0 < dScrollX < mRectSpace/2, " + dScrollX);
                    } else {
                        Log.d(TAG, "onTouchEvent: dScrollX > mRectSpace/2, " + dScrollX);
                    }
                } else {
                    if (-dScrollX < mRectSpace / 2) {
                        Log.d(TAG, "onTouchEvent: -mRectSpace/2 < dScrollX < 0, " + dScrollX);
                    } else {
                        Log.d(TAG, "onTouchEvent: dScrollX < -mRectSpace/2, " + dScrollX);
                    }
                }
                Log.d(TAG, "onTouchEvent() returned: ACTION_UP");
                break;
        }
        postInvalidate();
        return true;
    }
}
