package com.agenthun.bleecg.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * @project BleECG
 * @authors agenthun
 * @date 16/8/4 21:01.
 */
public class CheckEmptyRecyclerView extends RecyclerView {
    private View emptyView;

    public CheckEmptyRecyclerView(Context context) {
        super(context);
    }

    public CheckEmptyRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CheckEmptyRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private AdapterDataObserver observer = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            checkIsEmpty();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            super.onItemRangeInserted(positionStart, itemCount);
            checkIsEmpty();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            super.onItemRangeRemoved(positionStart, itemCount);
            checkIsEmpty();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            super.onItemRangeChanged(positionStart, itemCount);
            checkIsEmpty();
        }
    };

    private void checkIsEmpty() {
        if (emptyView != null && getAdapter() != null) {
            boolean emptyViewIsAvailable = getAdapter().getItemCount() == 0;
            emptyView.setVisibility(emptyViewIsAvailable ? VISIBLE : GONE);
            setVisibility(emptyViewIsAvailable ? GONE : VISIBLE);
        }
    }

    @Override
    public void setAdapter(Adapter adapter) {
        Adapter old = getAdapter();
        if (old != null) {
            old.unregisterAdapterDataObserver(observer);
        }
        super.setAdapter(adapter);
        if (adapter != null) {
            adapter.registerAdapterDataObserver(observer);
        }
        checkIsEmpty();
    }

    public void setEmptyView(View emptyView) {
        this.emptyView = emptyView;
        checkIsEmpty();
    }
}
