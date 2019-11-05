package com.csp.adapter.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;
import java.util.List;

/**
 * HeadFootAdapter - 追加头尾布局
 * Created by csp on 2018/08/30.
 * Modified by csp on 2019/08/20.
 *
 * @version 1.1.0
 */
@SuppressWarnings("unused")
public class HeadFootAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_HEAD = 0xC0000000;
    private static final int VIEW_TYPE_FOOT = 0x80000000;

    private RecyclerView.Adapter mAdapter;
    private List<View> mHeadViews = new ArrayList<>();
    private List<View> mFootViews = new ArrayList<>();

    public RecyclerView.Adapter getAdapter() {
        return mAdapter;
    }

    public HeadFootAdapter(@NonNull RecyclerView.Adapter<? extends RecyclerView.ViewHolder> adapter) {
        mAdapter = adapter;
    }

    private boolean isHeadView(int viewType) {
        return (viewType & VIEW_TYPE_HEAD) == VIEW_TYPE_HEAD;
    }

    private boolean isFootView(int viewType) {
        return (viewType & VIEW_TYPE_FOOT) == VIEW_TYPE_FOOT;
    }

    private boolean isHeadOrFoot(int viewType) {
        return isHeadView(viewType) || isFootView(viewType);
    }

    private View getViewByViewType(int viewType) {
        if (isHeadView(viewType))
            return mHeadViews.get(viewType & ~VIEW_TYPE_HEAD);
        else
            return mFootViews.get(viewType & ~VIEW_TYPE_FOOT);
    }

    private View addView(@LayoutRes int layoutId, @NonNull ViewGroup parent, boolean isHead) {
        View view = inflate(parent.getContext(), layoutId, parent);
        addView(view, isHead);
        return view;
    }

    private void addView(View view, boolean isHead) {
        if (isHead)
            mHeadViews.add(view);
        else
            mFootViews.add(view);
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
        if (!mHeadViews.remove(view))
            mFootViews.remove(view);
    }

    public void clearView() {
        mHeadViews.clear();
        mFootViews.clear();
    }

    public void clearHeadView() {
        mHeadViews.clear();
    }

    public void clearFootView() {
        mFootViews.clear();
    }

    public boolean hadHeadView() {
        return mHeadViews.size() > 0;
    }

    public boolean hadFootView() {
        return mFootViews.size() > 0;
    }

    @Override
    public int getItemCount() {
        return mHeadViews.size() + mFootViews.size() + mAdapter.getItemCount();
    }

    @Override
    public int getItemViewType(int position) {
        int indexOfAdapter = position - mHeadViews.size();
        int indexOfFoot = indexOfAdapter - mAdapter.getItemCount();

        if (indexOfAdapter < 0)
            return VIEW_TYPE_HEAD + position;
        else if (indexOfFoot < 0)
            return mAdapter.getItemViewType(indexOfAdapter);
        else
            return VIEW_TYPE_FOOT + indexOfFoot;
    }

    @Override
    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (isHeadOrFoot(viewType)) {
            return new ItemViewHolder(getViewByViewType(viewType));
        } else
            return mAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (!isHeadOrFoot(holder.getItemViewType()))
            mAdapter.onBindViewHolder(holder, position - mHeadViews.size());
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
                            : superSpanSizeLookup.getSpanSize(position - mHeadViews.size());
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
    @SuppressWarnings("WeakerAccess")
    public View inflate(Context context, @LayoutRes int layoutId, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(layoutId, parent, false);
    }
}
