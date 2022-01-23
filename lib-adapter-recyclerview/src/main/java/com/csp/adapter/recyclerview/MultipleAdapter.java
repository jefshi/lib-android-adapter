package com.csp.adapter.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

/**
 * RecyclerView.Adapter - 多布局
 * Created by csp on 2018/06/19.
 * Modified by csp on 2019/08/20.
 *
 * @param <T> 数据对象
 * @author csp
 * @version 1.1.0
 * @see SingleAdapter
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public abstract class MultipleAdapter<T> extends RecyclerView.Adapter<ItemViewHolder> {
    private LayoutInflater mInflater;

    /**
     * 原始数据集合，外围可通过 Adapter 影响该集合，但该集合与 Item 不一一对应
     */
    protected List<T> mData;
    /**
     * 数据集合，Adapter 内部数据，与 Item 一一对应
     */
    protected List<Object> mItemData;
    /**
     * 布局集合，Adapter 内部数据，与 Item 一一对应
     */
    protected List<IItemView> mItemViews;

    protected OnItemClickListener mOnItemClickListener;
    protected OnItemLongClickListener mOnItemLongClickListener;
    protected OnDataChangedListener mOnDataChangedListener;

    public List<T> getData() {
        return new ArrayList<>(mData);
    }

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
        mOnItemLongClickListener = listener;
    }

    public void setOnDataChangedListener(OnDataChangedListener onDataChangedListener) {
        mOnDataChangedListener = onDataChangedListener;
    }

    public MultipleAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        mData = new ArrayList<>();

        mItemData = new ArrayList<>();
        mItemViews = new ArrayList<>();
    }

    public MultipleAdapter(Context context, Collection<? extends T> data) {
        this(context);

        addData(data, false);
    }

    /**
     * @see #addData(int, Collection, boolean)
     */
    public void addData(Collection<? extends T> data, boolean append) {
        addData(-1, data, append);
    }

    /**
     * @see #addData(int, Collection, boolean)
     */
    public <U extends T> void addData(U[] data, boolean append) {
        List<U> dataList = data == null ? null : Arrays.asList(data);
        addData(-1, dataList, append);
    }

    /**
     * @see #addData(int, Collection, boolean)
     */
    public <U extends T> void addData(U datum, boolean append) {
        addData(-1, datum, append);
    }

    /**
     * 追加数据源
     *
     * @param position 添加位置, -1: 添加在末尾，该值只于 {@link #mData} 有关
     * @param data     数据
     * @param append   是否追加到列表末尾。false: 重置数据
     */
    public void addData(int position, Collection<? extends T> data, boolean append) {
        if (!append) {
            mData.clear();
        }
        if (data != null && !data.isEmpty()) {
            if (position < 0) {
                mData.addAll(data);
            } else {
                mData.addAll(position, data);
            }
        }
        onDataChanged();
    }

    /**
     * @see #addData(int, Collection, boolean)
     */
    public <U extends T> void addData(int position, U datum, boolean append) {
        if (!append) {
            mData.clear();
        }
        if (datum != null) {
            if (position < 0) {
                mData.add(datum);
            } else {
                mData.add(position, datum);
            }
        }
        onDataChanged();
    }

    /**
     * 重置数据。重置的数据索引无效，则为添加数据
     *
     * @param position 要重置的数据索引，该值只于 {@link #mData} 有关
     * @param datum    重置后数据
     * @see #addData(int, Collection, boolean)
     */
    public void resetData(int position, T datum) {
        // 索引无效添加在末尾
        if (position < 0 || position >= mData.size()) {
            addData(-1, datum, false);
            return;
        }

        mData.remove(position);
        mData.add(position, datum);
        onDataChanged();
    }

    /**
     * @see Collection#remove(Object)
     */
    public <U extends T> void removeData(U datum) {
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
        if (mOnDataChangedListener != null) {
            mOnDataChangedListener.onDataChanged();
        }
    }

    /**
     * @return 获取布局
     */
    protected IItemView getItemView(int position) {
        return mItemViews.get(position);
    }

    /**
     * @see android.widget.Adapter#getItem(int)
     */
    public Object getItem(int position) {
        return mItemData.get(position);
    }

    @Override
    public int getItemCount() {
        return mItemViews.size();
    }

    /**
     * @return 布局ID 作为 ItemViewType
     */
    @Override
    public int getItemViewType(int position) {
        return mItemViews.get(position).getLayoutId();
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // ItemViewType 为布局ID
        ItemViewHolder holder = ItemViewHolder.createViewHolder(mInflater, parent, viewType);
        onCreateViewHolder(parent, holder);
        setOnClickListener(holder);
        return holder;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        getItemView(position).onBind(holder, getItem(position), position);
    }

    protected void onCreateViewHolder(@NonNull ViewGroup parent, ItemViewHolder holder) {
    }

    protected void setOnClickListener(final ItemViewHolder holder) {
        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(position);
                }
            }
        });

        holder.getConvertView().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                int position = holder.getAdapterPosition();
                return mOnItemLongClickListener != null
                        && mOnItemLongClickListener.onItemLongClick(position);
            }
        });
    }

    /**
     * 解析 XML，如果 ViewGroup 是 RecyclerView，那么保证 RecyclerView 已经执行过 setAdapter()
     */
    public View inflate(@LayoutRes int layoutId, ViewGroup parent) {
        return mInflater.inflate(layoutId, parent, false);
    }

    /**
     * ItemViewHolder 数据填充（规则）
     *
     * @param <E> 数据对象
     */
    public interface IItemView<E> {

        /**
         * 见 return 描述
         *
         * @return ItemViewHolder 对应布局
         */
        @LayoutRes
        int getLayoutId();

        /**
         * ItemViewHolder 数据绑定
         *
         * @param holder   ItemViewHolder
         * @param datum    对应数据
         * @param position 位置
         */
        void onBind(ItemViewHolder holder, E datum, int position);
    }

    /**
     * 数据变化监听器
     */
    public interface OnDataChangedListener {

        /**
         * 数据变化事件
         */
        void onDataChanged();
    }

    /**
     * @see AdapterView.OnItemClickListener
     */
    public interface OnItemClickListener {

        /**
         * Item 的点击事件
         *
         * @param position 被点击的 View 所属 Item 的 position
         * @see RecyclerView.ViewHolder#getAdapterPosition()
         */
        void onItemClick(int position);
    }

    /**
     * @see AdapterView.OnItemLongClickListener
     */
    public interface OnItemLongClickListener {

        /**
         * Item 的长按事件
         *
         * @param position 被点击的 View 所属 Item 的 position
         * @return true：长按事件消费
         * @see RecyclerView.ViewHolder#getAdapterPosition()
         */
        boolean onItemLongClick(int position);
    }

    /**
     * @see RadioGroup.OnCheckedChangeListener
     */
    public interface OnCheckedChangeListener {

        /**
         * 勾选事件
         *
         * @param checked  true：表示事件发生前，View 已勾选
         * @param position View 所属 Item 的 position
         * @see RecyclerView.ViewHolder#getAdapterPosition()
         */
        void onCheckedChanged(boolean checked, int position);
    }

    /**
     * 其他事件监听
     */
    public interface OnOtherListener {

        /**
         * 其他事件
         *
         * @param position View 所属 Item 的 position
         * @see RecyclerView.ViewHolder#getAdapterPosition()
         */
        void onOther(int position);
    }

    /**
     * 自定义事件监听
     * 1) 可用于传递的数据与 position 无关的场景
     * 2) 与 position 有关的场景，建议使用：{@link OnOtherListener}
     */
    public interface OnCustomListener<E> {

        /**
         * 其他事件
         *
         * @param datum    需要传递的数据，不一定是 T
         * @param position View 所属 Item 的 position
         * @see RecyclerView.ViewHolder#getAdapterPosition()
         */
        void onOther(E datum, int position);
    }
}
