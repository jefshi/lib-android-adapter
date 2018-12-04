package com.csp.sample.adapter.adapter;

import com.csp.adapter.recyclerview.MultipleAdapter;
import com.csp.adapter.recyclerview.ViewHolder;
import com.csp.sample.adapter.R;


class TitleViewFill implements MultipleAdapter.IViewFill<String> {

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
    public void onBind(final ViewHolder holder, String title, final int position) {
        holder.setText(R.id.txt_title, title);
    }
}
