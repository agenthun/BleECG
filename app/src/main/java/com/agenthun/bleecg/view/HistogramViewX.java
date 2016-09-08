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
import com.agenthun.bleecg.utils.DataLogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @project BleECG
 * @authors agenthun
 * @date 16/8/7 01:43.
 */
public class HistogramViewX extends View {
    private static final String TAG = "HistogramView";

    private int mWidth;
    private int mRectSpace;
    private int mRectHeight;
    private int mRectWidth;
    private int mRectHalfWidth;
    private int mRectCount = 9;
    private int offset = 0;

    private int mDataLength = 20;
    List<RoundHistogram> dataSet = new ArrayList<>();
    String[] dataStr = null;

    private Paint mPaint;

    private LinearGradient mLinearGradient;

    private Context mContext;

    private Scroller scroller;

    private int mMaxDataValue = Integer.MIN_VALUE;
    private int[] value = {
            93,
            90,
            89,
            86,
            91,
            70,
            88,
            91,
            90,
            89,
            87
    };

    public HistogramViewX(Context context) {
        super(context);
        initView(context);
    }

    public HistogramViewX(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public HistogramViewX(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        mContext = context;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        scroller = new Scroller(context);

        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();

//        byte[] buffer = DataLogUtils.FileToBytes();
//        dataStr = new String(buffer).split("\n");

        float avg = 0;
        for (int i = 0; i < value.length; i++) {
            avg += value[i];
            if (value[i] > mMaxDataValue) {
                mMaxDataValue = value[i];
            }
        }
        avg /= value.length;
    }

    private void initData() {

        for (int i = 0; i < value.length; i++) {
            RoundHistogram roundHistogram = new RoundHistogram();
            PointF pointF = roundHistogram.getStart();
            pointF.x = mRectSpace * i;
            roundHistogram.setStart(pointF);
            roundHistogram.setSpace(mRectSpace);
            roundHistogram.setWidth(mRectWidth);
            roundHistogram.setHeight((1 - value[i] * 1.0f / mMaxDataValue) * mRectHeight);
//            roundHistogram.setHeight((i + 1.0f) / (mDataLength * 2) * mRectHeight);
            dataSet.add(roundHistogram);
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

        for (int i = 0; i < dataSet.size(); i++) {
            RoundHistogram roundHistogram = dataSet.get(i);

            rectF = new RectF(
                    roundHistogram.getStart().x,
                    roundHistogram.getHeight(),
                    roundHistogram.getStart().x + roundHistogram.getWidth(),
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
                            if (dataSet.get(0).getStart().x + mSliding > dataSet.get(0).getSpace() ||
                                    dataSet.get(dataSet.size() - 1).getStart().x + mSliding + dataSet.get(dataSet.size() - 1).getSpace() + dataSet.get(dataSet.size() - 1).getWidth() < mWidth) {
                                return true;
                            }
                            for (int i = 0; i < dataSet.size(); i++) {
                                PointF start = dataSet.get(i).getStart();
                                start.x += mSliding;
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
                    break;
            }
        }
        return true;
    }

    private int clickWhere(PointF pointF) {
        for (int i = 0; i < dataSet.size(); i++) {
            RoundHistogram roundHistogram = dataSet.get(i);
            PointF start = roundHistogram.getStart();
            if (start.x > pointF.x) {
                return -1;
            } else if (start.x <= pointF.x) {
                if (start.x + roundHistogram.getWidth() > pointF.x &&
                        (start.y > pointF.y && start.y - roundHistogram.getHeight() < pointF.y)) {
                    return i;
                }
            }
        }
        return -1;
    }

    class RoundHistogram {
        private float mWidth;
        private float mHeight;
        private float mSpace;
        private PointF mStart = new PointF(); //起始位置

        public RoundHistogram() {
            mStart.y = 10f;
        }

        public float getWidth() {
            return mWidth;
        }

        public void setWidth(float mWidth) {
            this.mWidth = mWidth;
        }

        public float getHeight() {
            return mHeight;
        }

        public void setHeight(float mHeight) {
            this.mHeight = mHeight;
        }

        public float getSpace() {
            return mSpace;
        }

        public void setSpace(float mSpace) {
            this.mSpace = mSpace;
        }

        public PointF getStart() {
            return mStart;
        }

        public void setStart(PointF mStart) {
            this.mStart = mStart;
        }
    }
}
