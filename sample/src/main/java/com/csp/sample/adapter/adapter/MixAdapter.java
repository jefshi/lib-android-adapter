package com.csp.sample.adapter.adapter;

import android.content.Context;

import com.csp.adapter.recyclerview.MultipleAdapter;
import com.csp.sample.adapter.dto.TopDto;

public class MixAdapter extends MultipleAdapter<TopDto> {

    public MixAdapter(Context context) {
        super(context);
    }

    /**
     * 列表的原始数据变化监听：
     * 1. 用于更新列表每一项所匹配的数据和布局类型
     * 2. 一般不建议在这直接刷新列表，即调用 notifyDataSetChanged 等
     */
    @Override
    public void onDataChanged() {
        super.onDataChanged();

        mItemData.clear();  // 数据集合，Adapter 内部数据，与 Item 一一对应
        mItemViews.clear(); // 布局集合，Adapter 内部数据，与 Item 一一对应
        for (int i = 0; i < mData.size(); i++) {  // 原始数据集合，外围通过 Adapter 影响，但与 Item 不一一对应
            TopDto datum = mData.get(i);
            if (i % 2 == 0) {
                mItemData.add(datum.getChineseName());
                mItemViews.add(new TitleItemView()); // 标题布局
            }
            mItemData.add(datum);
            mItemViews.add(new TopItemView()); // 内容布局
        }
    }
}
