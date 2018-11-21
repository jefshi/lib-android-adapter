package com.csp.adapter.recyclerview;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * HeadFootAdapter - 追加头尾布局
 * Created by csp on 2018/08/30.
 * Modified by csp on 2018/09/12.
 *
 * @version 1.0.1
 */
@SuppressWarnings("unused")
public class HeadFootAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private RecyclerView.Adapter mAdapter;

    private static final int VIEW_TYPE_HEAD = 0x40000000;
    private static final int VIEW_TYPE_FOOT = 0x80000000;

    private int mHeadSize = 0;
    private SparseArrayCompat<View> mExtraViews = new SparseArrayCompat<>();

    public RecyclerView.Adapter getAdapter() {
        return mAdapter;
    }

    public HeadFootAdapter(@NonNull RecyclerView.Adapter<? extends RecyclerView.ViewHolder> adapter) {
        mAdapter = adapter;
    }

    private boolean isHeadView(int key) {
        return (key & VIEW_TYPE_HEAD) == VIEW_TYPE_HEAD;
    }

    private boolean isFootView(int key) {
        return (key & VIEW_TYPE_FOOT) == VIEW_TYPE_FOOT;
    }

    private boolean isHeadOrFoot(int key) {
        return isHeadView(key) || isFootView(key);
    }

    private View addView(@LayoutRes int layoutId, @NonNull ViewGroup parent, boolean isHead) {
        View view = inflate(parent.getContext(), layoutId, parent);
        int index = isHead ? layoutId : layoutId + VIEW_TYPE_FOOT; // layout = 0x7f......
        mExtraViews.put(index, view);
        if (isHead)
            ++mHeadSize;
        return view;
    }

    private void addView(View view, boolean isHead) {
        int index = mExtraViews.size() + (isHead ? VIEW_TYPE_HEAD : VIEW_TYPE_FOOT);
        mExtraViews.put(index, view);
        if (isHead)
            ++mHeadSize;
    }

    public View addHeaderView(@LayoutRes int layoutId, @NonNull ViewGroup parent) {
        return addView(layoutId, parent, true);
    }

    public View addFootView(@LayoutRes int layoutId, @NonNull ViewGroup parent) {
        return addView(layoutId, parent, false);
    }

    public void addHeaderView(View view) {
        addView(view, true);
    }

    public void addFootView(View view) {
        addView(view, false);
    }

    private void removeView(View view) {
        int index = mExtraViews.indexOfValue(view);
        if (index > -1) {
            mExtraViews.removeAt(index);
            if (isHeadView(mExtraViews.keyAt(index)))
                --mHeadSize;
        }
    }

    public void clearView() {
        mExtraViews.clear();
        mHeadSize = 0;
    }

    public void clearHeadView() {
        for (int i = (mExtraViews == null ? -1 : mExtraViews.size() - 1); i > 0; --i) {
            if (isHeadView(mExtraViews.keyAt(i)))
                mExtraViews.removeAt(i);
        }
        mHeadSize = 0;
    }

    public void clearFootView() {
        for (int i = (mExtraViews == null ? -1 : mExtraViews.size() - 1); i > 0; --i) {
            if (!isHeadView(mExtraViews.keyAt(i)))
                mExtraViews.removeAt(i);
        }
    }

    public boolean hadHeadView() {
        return mHeadSize > 0;
    }

    public boolean hadFootView() {
        return mExtraViews.size() - mHeadSize > 0;
    }

    @Override
    public int getItemCount() {
        return mExtraViews.size() + mAdapter.getItemCount();
    }

    @Override
    public int getItemViewType(int position) {
        int index = position - mHeadSize - mAdapter.getItemCount();
        if (position < mHeadSize)
            index = position;

        if (index > -1)
            return mExtraViews.keyAt(index);
        else
            return mAdapter.getItemViewType(position - mHeadSize);
    }

    @Override
    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (isHeadOrFoot(viewType)) {
            View view = mExtraViews.get(viewType);
            return new ViewHolder(view);
        } else
            return mAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (!isHeadOrFoot(holder.getItemViewType()))
            mAdapter.onBindViewHolder(holder, position - mHeadSize);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, List<Object> payloads) {
        if (payloads.isEmpty())
            onBindViewHolder(holder, position);
        else if (!isHeadOrFoot(holder.getItemViewType()))
            mAdapter.onBindViewHolder(holder, position - mHeadSize, payloads);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        mAdapter.onAttachedToRecyclerView(recyclerView);

        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = (GridLayoutManager) manager;
            final GridLayoutManager.SpanSizeLookup superSpanSizeLookup = gridManager.getSpanSizeLookup();

            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return isHeadOrFoot(getItemViewType(position))
                            ? gridManager.getSpanCount()
                            : superSpanSizeLookup.getSpanSize(position - mHeadSize);
                }
            });
        }
    }

    @Override
    public void onViewAttachedToWindow(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);

        if (isHeadOrFoot(holder.getItemViewType())) {
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
            if (lp instanceof StaggeredGridLayoutManager.LayoutParams) {
                StaggeredGridLayoutManager.LayoutParams p
                        = (StaggeredGridLayoutManager.LayoutParams) lp;
                p.setFullSpan(true);
            }
        }
    }

    /**
     * 解析 XML，如果 ViewGroup 是 RecyclerView，那么保证 RecyclerView 已经执行过 setAdapter()
     */
    public View inflate(Context context, @LayoutRes int layoutId, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(layoutId, parent, false);
    }
}
