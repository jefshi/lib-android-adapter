package com.csp.adapter.recyclerview;

import android.content.Context;

import java.util.Collection;

/**
 * RecyclerView.Adapter - 单布局
 * Created by csp on 2018/06/19.
 * Modified by csp on 2018/06/19.
 *
 * @version 1.0.0
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public abstract class SingleAdapter<T> extends MultipleAdapter<T> {
    private int mLayoutId;

    public SingleAdapter(Context context, int layoutId) {
        super(context);

        mLayoutId = layoutId;
    }

    public SingleAdapter(Context context, int layoutId, Collection<T> data) {
        super(context);

        mLayoutId = layoutId;
        addData(data, false);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    protected void addMultiViewFills() {
        addViewFill(0, new IViewFill<T>() {
            @Override
            public int getLayoutId() {
                return mLayoutId;
            }

            @Override
            public void convert(ViewHolder holder, T datum, int offset) {
                SingleAdapter.this.convert(holder, datum, offset);
            }
        });
    }

    /**
     * @see android.widget.Adapter#getItem(int)
     */
    public T getItem(int position) {
        return mData.get(position);
    }

    /**
     * {@link IViewFill#convert(ViewHolder, Object, int)}
     *
     * @param position 对应数据在列表中的位置
     */
    protected abstract void convert(ViewHolder holder, T datum, int position);
}
