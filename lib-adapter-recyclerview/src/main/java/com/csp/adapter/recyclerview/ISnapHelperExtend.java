package com.csp.adapter.recyclerview;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

/**
 * RecyclerView + SnapHelper 时，实现类似 ViewPager.OnPageChangeListener 的监听
 * Created by pangli on 2018/12/13.
 * Modified by csp on 2018/11/28.
 *
 * @author csp
 * @version 1.0.3
 */
@SuppressWarnings("unused")
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
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            if (mOnPageChangeListener != null) {
                mOnPageChangeListener.onScrolled(recyclerView, dx, dy);
            }
        }

        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            if (mOnPageChangeListener == null) {
                return;
            }
            mOnPageChangeListener.onScrollStateChanged(recyclerView, newState);

            if (newState != RecyclerView.SCROLL_STATE_IDLE) {
                return;
            }
            RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
            View view = manager != null ? mSnapHelper.findSnapView(manager) : null;
            int position = view != null ? manager.getPosition(view) : 0; // 当前 View 的 position
            if (mOldPosition != position) {
                mOldPosition = position;
                mOnPageChangeListener.onPageSelected(position);
            }
        }
    }

    /**
     * 参考：ViewPager.OnPageChangeListener
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