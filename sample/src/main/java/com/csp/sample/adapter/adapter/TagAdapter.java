package com.csp.sample.adapter.adapter;

import android.content.Context;

import com.csp.adapter.recyclerview.SingleAdapter;
import com.csp.adapter.recyclerview.ViewHolder;
import com.csp.sample.adapter.R;

public class TagAdapter extends SingleAdapter<String> {

    public TagAdapter(Context context) {
        super(context, R.layout.item_tag);
    }

    @Override
    protected void onBind(ViewHolder holder, String datum, final int position) {
        holder.setText(R.id.txt_tag, datum);

        holder.getConvertView().setOnClickListener(v -> {
            mData.remove(datum);
            notifyDataSetChanged();

            if (mOnItemClickListener != null)
                mOnItemClickListener.onItemClick(null, null, holder, position, -1);
        });
    }
}
