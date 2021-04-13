package com.csp.sample.adapter.adapter;

import com.csp.adapter.recyclerview.ItemViewHolder;
import com.csp.adapter.recyclerview.MultipleAdapter;
import com.csp.sample.adapter.R;
import com.csp.sample.adapter.dto.TopDto;


/**
 * 内容布局
 *
 * @author csp
 */
class TopItemView implements MultipleAdapter.IItemView<TopDto> {

    private final static int[] IMG_GRADE_RES = new int[]{
            R.drawable.ic_grade_s,
            R.drawable.ic_grade_a,
            R.drawable.ic_grade_b,
    };

    @Override
    public int getLayoutId() {
        return R.layout.item_top;
    }

    @Override
    public void onBind(ItemViewHolder holder, TopDto datum, int position) {
        int grade;
        switch (datum.getGameGrade()) {
            case "S":
                grade = 0;
                break;
            case "A":
                grade = 1;
                break;
            default:
            case "B":
                grade = 2;
                break;
        }
        holder.setText(R.id.txt_label, datum.getChineseName())
                .setText(R.id.txt_booster, datum.getGameService())
                .setText(R.id.txt_describe, datum.getGameRecommendText())
                .setImageResource(R.id.img_grade, IMG_GRADE_RES[grade]);
    }
}
