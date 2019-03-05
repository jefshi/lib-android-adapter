package com.csp.adapter.recyclerview;

import android.content.Context;
import android.support.annotation.NonNull;

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
    private static final int DEFAULT_LAYOUT = 0;

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

    public SingleAdapter(Context context, int layoutId, T[] data) {
        super(context);

        mLayoutId = layoutId;
        addData(data, false);
    }

    @Override
    protected void addMultiViewFills() {
        addViewFill(DEFAULT_LAYOUT, new IViewFill<T>() {
            @Override
            public int getLayoutId() {
                return mLayoutId;
            }

            @Override
            public void onBind(ViewHolder holder, T datum, Object extra, int position) {
                SingleAdapter.this.onBind(holder, datum, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        return DEFAULT_LAYOUT;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        IViewFill viewFill = getViewFill(viewType);
        viewFill.onBind(holder, mData.get(position), null, position);
    }

    /**
     * @see android.widget.Adapter#getItem(int)
     */
    public T getItem(int position) {
        return mData.get(position);
    }

    /**
     * {@link IViewFill#onBind(ViewHolder, Object, Object, int)}
     *
     * @param position 对应数据在列表中的位置
     */
    protected abstract void onBind(ViewHolder holder, T datum, int position);
}
