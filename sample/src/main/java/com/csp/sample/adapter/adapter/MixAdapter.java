package com.csp.sample.adapter.adapter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.csp.adapter.recyclerview.MultipleAdapter;
import com.csp.adapter.recyclerview.ViewHolder;
import com.csp.sample.adapter.dto.TopDto;

import java.util.ArrayList;
import java.util.List;

public class MixAdapter extends MultipleAdapter<TopDto> {
    private final int TOP_LAYOUT = 0;
    private final int TITLE_LAYOUT = 1;

    private final List<Integer> mItemViewType = new ArrayList<>();
    private List<Object> mNewData = new ArrayList<>();

    public MixAdapter(Context context) {
        super(context);
    }

    /**
     * 添加布局（布局数据类型可以不同）
     */
    @Override
    protected void addMultiViewFills() {
        addViewFill(TOP_LAYOUT, new TopViewFill());
        addViewFill(TITLE_LAYOUT, new TitleViewFill());
    }

    @Override
    public void onDataChanged() {
        super.onDataChanged();

        mNewData.clear();
        mItemViewType.clear();
        for (int i = 0; i < mData.size(); i++) {
            TopDto datum = mData.get(i);
            if (i % 2 == 0) {
                mNewData.add(datum.getChineseName());
                mItemViewType.add(TITLE_LAYOUT);
            }
            mItemViewType.add(TOP_LAYOUT);
            mNewData.add(datum);
        }
    }

    @Override
    public int getItemCount() {
        return mNewData.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mItemViewType.get(position);
    }

    /**
     * 只负责将数据分配给对应的布局
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        IViewFill viewFill = getViewFill(viewType);
        if (viewType == TOP_LAYOUT) {
            ((TopViewFill) viewFill).onBind(holder, (TopDto) mNewData.get(position), position);
            return;
        }

        if (viewType == TITLE_LAYOUT) {
            ((TitleViewFill) viewFill).onBind(holder, (String) mNewData.get(position), position);
        }
    }
}
