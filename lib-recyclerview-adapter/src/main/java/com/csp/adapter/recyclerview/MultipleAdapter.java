package com.csp.adapter.recyclerview;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * RecyclerView.Adapter - 多布局
 * Created by csp on 2018/06/19.
 * Modified by csp on 2018/06/19.
 *
 * @param <T> 数据对象
 * @version 1.0.0
 * @see SingleAdapter
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public abstract class MultipleAdapter<T> extends RecyclerView.Adapter<ViewHolder> {
    protected LayoutInflater mInflater;
    protected List<T> mData;

    private SparseArray<IViewFill> mViewFillManager;

    protected OnItemClickListener mOnItemClickListener;
    protected OnItemLongClickListener mOnItemLongClickListener;

    /**
     * @see AdapterView#setOnItemClickListener(AdapterView.OnItemClickListener)
     */
    public void setOnItemClickListener(@Nullable OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    /**
     * @see AdapterView#setOnItemLongClickListener(AdapterView.OnItemLongClickListener)
     */
    public void setOnItemLongClickListener(@Nullable OnItemLongClickListener listener) {
        // ViewGroup parent = null;
        // if (!parent.isLongClickable()) {
        //     parent.setLongClickable(true);
        // }
        mOnItemLongClickListener = listener;
    }

    public MultipleAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        mData = new ArrayList<>();

        mViewFillManager = new SparseArray<>();
        addMultiViewFills();
    }

    public MultipleAdapter(Context context, Collection<T> data) {
        this(context);

        addData(data, false);
    }

    /**
     * @see #addData(int, Collection, boolean)
     */
    public void addData(Collection<T> data, boolean append) {
        addData(-1, data, append);
    }

    /**
     * @see #addData(int, Collection, boolean)
     */
    public void addData(T[] data, boolean append) {
        List<T> dataList = Arrays.asList(data);
        addData(-1, dataList, append);
    }

    /**
     * @see #addData(int, Collection, boolean)
     */
    public void addData(T datum, boolean append) {
        addData(-1, datum, append);
    }

    /**
     * 追加数据源
     *
     * @param position 添加位置, -1: 添加在末尾
     * @param data     数据
     * @param append   false: 重置数据
     */
    public void addData(int position, Collection<T> data, boolean append) {
        if (!append)
            mData.clear();

        if (data != null && !data.isEmpty()) {
            if (position < 0)
                mData.addAll(data);
            else
                mData.addAll(position, data);
        }
        onDataChanged();
    }

    /**
     * @see #addData(int, Collection, boolean)
     */
    public void addData(int position, T datum, boolean append) {
        if (!append)
            mData.clear();

        if (datum != null) {
            if (position < 0)
                mData.add(datum);
            else
                mData.add(position, datum);
        }
        onDataChanged();
    }

    /**
     * @see Collection#remove(Object)
     */
    public void removeData(T datum) {
        mData.remove(datum);
        onDataChanged();
    }

    /**
     * @see Collection#clear()
     */
    public void clearData() {
        mData.clear();
        onDataChanged();
    }

    /**
     * 数据变化时回调
     */
    public void onDataChanged() {
    }

    /**
     * 追加布局
     *
     * @see SingleAdapter#onBindViewHolder(ViewHolder, int)
     */
    protected MultipleAdapter addViewFill(int viewType, IViewFill viewHolder) {
        mViewFillManager.put(viewType, viewHolder);
        return this;
    }

    /**
     * @return 获取布局
     * @see #getViewFillByPosition(int)
     */
    protected IViewFill getViewFill(int viewType) {
        return mViewFillManager.get(viewType);
    }

    /**
     * @return 获取布局
     * @see #getItemViewType(int)
     * @see #getViewFill(int)
     * @see SingleAdapter#onBindViewHolder(ViewHolder, int)
     */
    protected IViewFill getViewFillByPosition(int position) {
        return getViewFill(getItemViewType(position));
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutId = mViewFillManager.get(viewType).getLayoutId();
        View view = mInflater.inflate(layoutId, parent, false);
        ViewHolder holder = new ViewHolder(view);
        onCreateViewHolder(holder);
        setOnClickListener(parent, holder);
        return holder;
    }

    protected void onCreateViewHolder(ViewHolder holder) {
    }

    protected void setOnClickListener(final ViewGroup parent, final ViewHolder viewHolder) {
        viewHolder.getConvertView().setOnClickListener(view -> {
            int position = viewHolder.getAdapterPosition();
            if (mOnItemClickListener != null)
                mOnItemClickListener.onItemClick(parent, view, viewHolder, position, -1);
        });

        viewHolder.getConvertView().setOnLongClickListener(view -> {
            int position = viewHolder.getAdapterPosition();
            return mOnItemLongClickListener != null
                    && mOnItemLongClickListener.onItemLongClick(parent, view, viewHolder, position, -1);
        });
    }

    /**
     * ViewHolder 数据填充（规则）
     *
     * @param <E> 数据对象
     */
    public interface IViewFill<E> {

        /**
         * @return ViewHolder 对应布局
         */
        int getLayoutId();

        /**
         * ViewHolder 数据绑定
         *
         * @param holder ViewHolder
         * @param datum  对应数据
         * @param offset 数据偏移量
         */
        void onBind(ViewHolder holder, E datum, int offset);
    }

    /**
     * @see AdapterView.OnItemClickListener
     */
    public interface OnItemClickListener {

        void onItemClick(ViewGroup parent, View view, RecyclerView.ViewHolder viewHolder, int position, long id);
    }

    /**
     * @see AdapterView.OnItemLongClickListener
     */
    public interface OnItemLongClickListener {

        boolean onItemLongClick(ViewGroup parent, View view, RecyclerView.ViewHolder viewHolder, int position, long id);
    }

    /**
     * 添加布局，配合 onBindViewHolder() 使用，具体参考见 @see
     *
     * @see #addViewFill(int, IViewFill)
     * @see SingleAdapter#addMultiViewFills()
     * @see SingleAdapter#onBindViewHolder(ViewHolder, int)
     */
    protected abstract void addMultiViewFills();

    /**
     * 解析 XML，如果 ViewGroup 是 RecyclerView，那么保证 RecyclerView 已经执行过 setAdapter()
     */
    @SuppressWarnings("unchecked")
    public View inflate(@LayoutRes int layoutId, ViewGroup parent) {
        return mInflater.inflate(layoutId, parent, false);
    }
}
