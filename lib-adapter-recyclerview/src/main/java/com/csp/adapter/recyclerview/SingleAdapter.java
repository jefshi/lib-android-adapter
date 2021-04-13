package com.csp.adapter.recyclerview;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;

import java.util.Collection;

/**
 * RecyclerView.Adapter - 单布局
 * Created by csp on 2018/06/19.
 * Modified by csp on 2019/08/20.
 *
 * @author csp
 * @version 1.1.0
 */
@SuppressWarnings({"unused"})
public abstract class SingleAdapter<T> extends MultipleAdapter<T> implements MultipleAdapter.IItemView<T> {

    @LayoutRes
    private int mLayoutId;

    public SingleAdapter(Context context, @LayoutRes int layoutId) {
        super(context);

        mLayoutId = layoutId;
        registerAdapterDataObserver();
    }

    public SingleAdapter(Context context, @LayoutRes int layoutId, Collection<T> data) {
        super(context);

        mLayoutId = layoutId;
        addData(data, false);
        registerAdapterDataObserver();
    }

    public SingleAdapter(Context context, @LayoutRes int layoutId, T[] data) {
        super(context);

        mLayoutId = layoutId;
        addData(data, false);
    }

    @Override
    public int getLayoutId() {
        return mLayoutId;
    }

    @Override
    public void onDataChanged() {
        mItemData.clear();
        mItemViews.clear();

        mItemData.addAll(mData);
        for (int i = 0; i < mItemData.size(); i++) {
            mItemViews.add(this);
        }
        super.onDataChanged();
    }

    @Override
    @SuppressWarnings("unchecked")
    public T getItem(int position) {
        return (T) super.getItem(position);
    }

    public void registerAdapterDataObserver() {
        // TODO 补全监听
        registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                mData.remove(positionStart);
                mItemData.remove(positionStart);
                mItemViews.remove(positionStart);
                notifyItemRangeChanged(0, getItemCount());
            }
        });
    }
}
