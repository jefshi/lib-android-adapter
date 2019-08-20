package com.csp.sample.adapter.adapter;

import android.content.Context;

import com.csp.adapter.recyclerview.MultipleAdapter;
import com.csp.sample.adapter.dto.TopDto;

public class MixAdapter extends MultipleAdapter<TopDto> {

    public MixAdapter(Context context) {
        super(context);
    }

    @Override
    public void onDataChanged() {
        super.onDataChanged();

        mItemData.clear();
        mItemViews.clear();
        for (int i = 0; i < mData.size(); i++) {
            TopDto datum = mData.get(i);
            if (i % 2 == 0) {
                mItemData.add(datum.getChineseName());
                mItemViews.add(new TitleViewFill());
            }
            mItemData.add(datum);
            mItemViews.add(new TopViewFill());
        }
    }
}
