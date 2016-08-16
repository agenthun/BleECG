package com.agenthun.bleecg.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Scroller;

import com.agenthun.bleecg.R;

import java.util.ArrayList;
import java.util.List;

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
    List<Float> dataSet = new ArrayList<>();

    private Paint mPaint;

    private LinearGradient mLinearGradient;

    private Context mContext;

    private Scroller scroller;

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
        scroller = new Scroller(context);

        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    private void initData() {
        for (int i = 0; i < mDataLength; i++) {
            dataSet.add((i + 1.0f) / (mDataLength * 2) * mRectHeight);
        }
    }

//    @Override
//    public void computeScroll() {
//        super.computeScroll();
//        if (scroller.computeScrollOffset()) {
//            ((View) getParent()).scrollTo(scroller.getCurrX(), scroller.getCurrY());
//            invalidate();
//        }
//    }

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
                    dataSet.get((i + offset) % mDataLength),
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

    private float mLastX;
    private boolean isMoving;
    private int mTouchSlop;
    private float mSliding = 0;
    private int mSelected = -1;

    private float ratio = 1;
    private long animateTime = 1600;
    private ValueAnimator mAnimator;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (dataSet.size() > 0) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mLastX = event.getX();
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (ratio == 1) {
                        float moveX = event.getX();
                        mSliding = moveX - mLastX;
                        if (Math.abs(mSliding) > mTouchSlop) {
                            isMoving = true;
                            mLastX = moveX;

                            for (int i = 0; i < dataSet.size(); i++) {

                            }
                            invalidate();
                        }
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    if (!isMoving) {
                        PointF pointF = new PointF(event.getX(), event.getY());
                        mSelected = clickWhere(pointF);
                        invalidate();
                    }
                    isMoving = false;
                    mSliding = 0;
                    Log.d(TAG, "onTouchEvent() returned: ACTION_UP");
                    break;
            }
        }
        return true;
    }

    private int clickWhere(PointF pointF) {
        for (int i = 0; i < dataSet.size(); i++) {

        }
        return 0;
    }

    class RoundHistogram {
        private float mWidth;
        private float mHeight;
        private PointF mStart = new PointF(); //起始位置
    }
}
