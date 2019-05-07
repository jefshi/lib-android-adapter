package com.csp.adapter.recyclerview;

import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.View;


/**
 * RecyclerView + SnapHelper 时，实现类似 {@link ViewPager.OnPageChangeListener} 的监听
 * Created by pangli on 2018/12/13.
 * Modified by csp on 2018/11/28.
 *
 * @version 1.0.3
 */
public interface ISnapHelperExtend {

    class OnScrollListener extends RecyclerView.OnScrollListener {
        private SnapHelper mSnapHelper;
        private OnPageChangeListener mOnPageChangeListener;
        private int mOldPosition = -1;

        public void setOnPageChangeListener(OnPageChangeListener onPageChangeListener) {
            mOnPageChangeListener = onPageChangeListener;
        }

        public OnScrollListener(SnapHelper snapHelper) {
            mSnapHelper = snapHelper;
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            if (mOnPageChangeListener != null)
                mOnPageChangeListener.onScrolled(recyclerView, dx, dy);
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            if (mOnPageChangeListener == null)
                return;

            mOnPageChangeListener.onScrollStateChanged(recyclerView, newState);

            if (newState != RecyclerView.SCROLL_STATE_IDLE)
                return;

            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            View view = mSnapHelper.findSnapView(layoutManager);
            int position = view != null ? layoutManager.getPosition(view) : 0; // 当前 View 的 position
            if (mOldPosition != position) {
                mOldPosition = position;
                mOnPageChangeListener.onPageSelected(position);
            }
        }


    }

    /**
     * @see ViewPager.OnPageChangeListener
     */
    abstract class OnPageChangeListener {

        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        }

        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        }

        public void onPageSelected(int position) {
        }
    }
}