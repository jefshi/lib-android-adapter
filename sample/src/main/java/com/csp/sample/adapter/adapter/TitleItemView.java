package com.csp.sample.adapter.adapter;

import com.csp.adapter.recyclerview.ItemViewHolder;
import com.csp.adapter.recyclerview.MultipleAdapter;
import com.csp.sample.adapter.R;


class TitleItemView implements MultipleAdapter.IItemView<String> {

    /**
     * 布局 ID
     */
    @Override
    public int getLayoutId() {
        return R.layout.item_title;
    }

    /**
     * 布局和数据绑定
     */
    @Override
    public void onBind(ItemViewHolder holder, String title, int position) {
        holder.setText(R.id.txt_title, title);
    }
}
