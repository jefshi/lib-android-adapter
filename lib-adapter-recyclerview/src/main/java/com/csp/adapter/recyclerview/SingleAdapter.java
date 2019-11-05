package com.csp.adapter.recyclerview;

import android.content.Context;

import androidx.annotation.LayoutRes;

import java.util.Collection;

/**
 * RecyclerView.Adapter - 单布局
 * Created by csp on 2018/06/19.
 * Modified by csp on 2019/08/20.
 *
 * @version 1.1.0
 */
@SuppressWarnings({"unused"})
public abstract class SingleAdapter<T> extends MultipleAdapter<T> implements MultipleAdapter.IItemView<T> {

    @LayoutRes
    private int mLayoutId;

    public SingleAdapter(Context context, @LayoutRes int layoutId) {
        super(context);

        mLayoutId = layoutId;
    }

    public SingleAdapter(Context context, @LayoutRes int layoutId, Collection<T> data) {
        super(context);

        mLayoutId = layoutId;
        addData(data, false);
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
    }

    @Override
    @SuppressWarnings("unchecked")
    public T getItem(int position) {
        return (T) super.getItem(position);
    }
}
