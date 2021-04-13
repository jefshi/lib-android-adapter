package com.csp.sample.adapter.adapter;

import android.content.Context;

import com.csp.adapter.recyclerview.ItemViewHolder;
import com.csp.adapter.recyclerview.SingleAdapter;
import com.csp.sample.adapter.R;

/**
 * 单布局举例—标签布局
 *
 * @author csp
 */
public class TagAdapter extends SingleAdapter<String> {

    /**
     * 添加布局 ID
     */
    public TagAdapter(Context context) {
        super(context, R.layout.item_tag);
    }

    /**
     * 布局和数据绑定
     */
    @Override
    public void onBind(ItemViewHolder holder, String datum, final int position) {
        holder.setText(R.id.txt_tag, datum);

        holder.getConvertView().setOnClickListener(v -> {
            // 因为修改数据了，所以监听器放前头
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(position);
            }
            notifyItemRemoved(position);
        });
    }
}
