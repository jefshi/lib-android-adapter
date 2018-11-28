package com.csp.sample.adapter.adapter;

import com.csp.adapter.recyclerview.MultipleAdapter;
import com.csp.adapter.recyclerview.ViewHolder;
import com.csp.sample.adapter.R;


/**
 * 分类布局
 * Created by chenshp on 2018/4/27.
 */
class TitleViewFill implements MultipleAdapter.IViewFill<String> {

    @Override
    public int getLayoutId() {
        return R.layout.item_title;
    }

    @Override
    public void onBind(final ViewHolder holder, String title, final int position) {
        holder.setText(R.id.txt_title, title);
    }
}
