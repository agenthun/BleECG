package com.agenthun.bleecg.view;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.widget.Checkable;

import com.agenthun.bleecg.R;

/**
 * @project ESeal
 * @authors agenthun
 * @date 16/3/10 上午1:23.
 */
public class CheckableFab extends FloatingActionButton implements Checkable {

    private static final int[] CHECKED = {android.R.attr.state_checked};

    private boolean mIsChecked = true;

    public CheckableFab(Context context) {
        this(context, null);
    }

    public CheckableFab(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CheckableFab(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setImageResource(R.drawable.setting_done_fab);
    }

    @Override
    public int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(++extraSpace);
        if (mIsChecked) {
            mergeDrawableStates(drawableState, CHECKED);
        }
        return drawableState;
    }

    @Override
    public void setChecked(boolean checked) {
        if (mIsChecked == checked) {
            return;
        }
        mIsChecked = checked;
        refreshDrawableState();
    }

    @Override
    public boolean isChecked() {
        return mIsChecked;
    }

    @Override
    public void toggle() {
        setChecked(!mIsChecked);
    }
}
