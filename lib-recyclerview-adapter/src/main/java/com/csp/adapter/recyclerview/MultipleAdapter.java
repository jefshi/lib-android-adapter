package com.csp.adapter.recyclerview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.csp.utillib.EmptyUtil;

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
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public abstract class MultipleAdapter<T> extends RecyclerView.Adapter<ViewHolder> {
    protected Context mContext;
    protected LayoutInflater mInflater;
    protected List<T> mData;

    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;

    private SparseArray<IViewFill> mViewFillManager;

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
        mContext = context;
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

        if (EmptyUtil.isEmpty(data))
            return;

        if (position < 0)
            mData.addAll(data);
        else
            mData.addAll(position, data);
    }

    /**
     * @see #addData(int, Collection, boolean)
     */
    public void addData(int position, T datum, boolean append) {
        if (!append)
            mData.clear();

        if (datum == null)
            return;

        if (position < 0)
            mData.add(datum);
        else
            mData.add(position, datum);
    }

    /**
     * @see Collection#remove(Object)
     */
    public void removeData(T datum) {
        mData.remove(datum);
    }

    /**
     * 追加布局
     *
     * @see #onBindViewHolder(ViewHolder, int)
     */
    protected MultipleAdapter addViewFill(int viewType, IViewFill viewHolder) {
        mViewFillManager.put(viewType, viewHolder);
        return this;
    }

    /**
     * @return 获取布局
     * @see #onBindViewHolder(ViewHolder, int)
     */
    protected IViewFill getViewFill(int viewType) {
        return mViewFillManager.get(viewType);
    }

    /**
     * @return 获取布局
     * @see #getItemViewType(int)
     * @see #getViewFill(int)
     */
    protected IViewFill getViewFillByPosition(int position) {
        return getViewFill(getItemViewType(position));
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutId = mViewFillManager.get(viewType).getLayoutId();
        View view = mInflater.inflate(layoutId, parent, false);
        ViewHolder holder = ViewHolder.createViewHolder(mContext, view);
        onCreateViewHolder(holder);
        setOnClickListener(parent, holder);
        return holder;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        IViewFill viewFill = getViewFillByPosition(position);
        viewFill.convert(holder, mData.get(position), position);
    }

    protected void onCreateViewHolder(ViewHolder holder) {
    }

    protected void setOnClickListener(final ViewGroup parent, final ViewHolder viewHolder) {
        final int position = viewHolder.getAdapterPosition();

        viewHolder.getConvertView().setOnClickListener((view) -> {
            if (mOnItemClickListener != null)
                mOnItemClickListener.onItemClick(parent, view, viewHolder, position, -1);
        });

        viewHolder.getConvertView().setOnClickListener((view) -> {
            if (mOnItemLongClickListener != null)
                mOnItemLongClickListener.onItemLongClick(parent, view, viewHolder, position, -1);
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
         * ViewHolder 数据填充
         *
         * @param holder ViewHolder
         * @param datum  对应数据
         * @param offset 数据偏移量
         */
        void convert(ViewHolder holder, E datum, int offset);
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

        void onItemLongClick(ViewGroup parent, View view, RecyclerView.ViewHolder viewHolder, int position, long id);
    }

    /**
     * 添加布局
     *
     * @see #addViewFill(int, IViewFill)
     */
    protected abstract void addMultiViewFills();
}
