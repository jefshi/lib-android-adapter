package com.csp.adapter.recyclerview;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RadioGroup;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * RecyclerView.Adapter - 多布局
 * Created by csp on 2018/06/19.
 * Modified by csp on 2019/04/10.
 *
 * @param <T> 数据对象
 * @version 1.0.1
 * @see SingleAdapter
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public abstract class MultipleAdapter<T> extends RecyclerView.Adapter<ViewHolder> {
    private LayoutInflater mInflater;
    protected List<T> mData; // 数据集，但与 Item 不一一对应
    protected List<Object> mItemData; // 数据集，与 Item 一一对应
    protected List<IItemView> mItemViews; // 布局集合，与 Item 一一对应

    protected OnItemClickListener mOnItemClickListener;
    protected OnItemLongClickListener mOnItemLongClickListener;
    protected OnDataChangedListener mOnDataChangedListener;

    @Deprecated
    public List<T> getData() {
        return mData;
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
        // ViewGroup parent = null;
        // if (!parent.isLongClickable()) {
        //     parent.setLongClickable(true);
        // }
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
     * @param append   是否追加到列表末尾。false: 重置数据
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
        if (mOnDataChangedListener != null)
            mOnDataChangedListener.onDataChanged();
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
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(viewType, parent, false); // ItemViewType 为布局ID
        ViewHolder holder = new ViewHolder(view);
        onCreateViewHolder(parent, holder);
        setOnClickListener(parent, holder);
        return holder;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        IItemView itemView = getItemView(position);
        itemView.onBind(holder, getItem(position), position);
    }

    protected void onCreateViewHolder(@NonNull ViewGroup parent, ViewHolder holder) {
    }

    protected void setOnClickListener(final ViewGroup parent, final ViewHolder viewHolder) {
        viewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = viewHolder.getAdapterPosition();
                if (mOnItemClickListener != null)
                    mOnItemClickListener.onItemClick(parent, view, viewHolder, position);
            }
        });

        viewHolder.getConvertView().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                int position = viewHolder.getAdapterPosition();
                return mOnItemLongClickListener != null
                        && mOnItemLongClickListener.onItemLongClick(parent, view, viewHolder, position);
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
     * ViewHolder 数据填充（规则）
     *
     * @param <E> 数据对象
     */
    public interface IItemView<E> {

        /**
         * @return ViewHolder 对应布局
         */
        @LayoutRes
        int getLayoutId();

        /**
         * ViewHolder 数据绑定
         *
         * @param holder   ViewHolder
         * @param datum    对应数据
         * @param position 位置
         */
        void onBind(ViewHolder holder, E datum, int position);
    }

    /**
     * 数据变化监听器
     */
    public interface OnDataChangedListener {

        void onDataChanged();
    }

    /**
     * @see AdapterView.OnItemClickListener
     */
    public interface OnItemClickListener {

        /**
         * Item 的点击事件
         *
         * @param parent     即 Item 所在的 RecyclerView 对象
         * @param view       被点击的 View
         * @param viewHolder 被点击的 View 所属 Item 的 ViewHolder
         * @param position   被点击的 View 所属 Item 的 position
         */
        void onItemClick(ViewGroup parent, View view, ViewHolder viewHolder, int position);
    }

    /**
     * @see AdapterView.OnItemLongClickListener
     */
    public interface OnItemLongClickListener {

        /**
         * Item 的长按事件
         *
         * @param parent     即 Item 所在的 RecyclerView 对象
         * @param view       被点击的 View
         * @param viewHolder 被点击的 View 所属 Item 的 ViewHolder
         * @param position   被点击的 View 所属 Item 的 position
         */
        boolean onItemLongClick(ViewGroup parent, View view, ViewHolder viewHolder, int position);
    }

    /**
     * @see RadioGroup.OnCheckedChangeListener
     */
    public interface OnCheckedChangeListener {

        /**
         * 勾选事件
         *
         * @param view       勾选监听的 View
         * @param checked    true：表示事件发生前，View 已勾选
         * @param viewHolder View 所属 Item 的 ViewHolder
         * @param position   View 所属 Item 的 position
         */
        void onCheckedChanged(View view, boolean checked, ViewHolder viewHolder, int position);
    }

    /**
     * 其他事件监听
     */
    public interface OnOtherListener {

        /**
         * 其他事件
         *
         * @param view       勾选监听的 View
         * @param viewHolder View 所属 Item 的 ViewHolder
         * @param position   View 所属 Item 的 position
         */
        void onOther(View view, ViewHolder viewHolder, int position);
    }

    /**
     * 自定义事件监听
     */
    public interface OnCustomListener<E> {

        /**
         * 其他事件
         *
         * @param view       勾选监听的 View
         * @param viewHolder View 所属 Item 的 ViewHolder
         * @param datum      需要传递的数据
         * @param position   View 所属 Item 的 position
         */
        void onOther(View view, ViewHolder viewHolder, E datum, int position);
    }
}
