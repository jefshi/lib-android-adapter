package com.csp.sample.adapter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.csp.adapter.recyclerview.HeadFootAdapter;
import com.csp.sample.adapter.adapter.MixAdapter;
import com.csp.sample.adapter.adapter.TagAdapter;
import com.csp.sample.adapter.dto.TopDto;

import java.util.ArrayList;
import java.util.List;

/**
 * 主页
 *
 * @author csp
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView rcvSingle = findViewById(R.id.rcv_single);
        RecyclerView rcvMultiple = findViewById(R.id.rcv_multiple);

        // 单布局
        TagAdapter tagAdapter = new TagAdapter(this);
        rcvSingle.setLayoutManager(new GridLayoutManager(this, 4));
        rcvSingle.setAdapter(tagAdapter);

        tagAdapter.setOnItemClickListener((position) -> {
            String item = tagAdapter.getItem(position);
            Toast.makeText(this, "删除该标签：" + item, Toast.LENGTH_SHORT).show();
        });

        List<String> tags = getTags();
        tagAdapter.addData(tags, false);
        tagAdapter.notifyDataSetChanged();

        // 多布局
        MixAdapter mixAdapter = new MixAdapter(this);
        List<TopDto> tops = getTops();
        mixAdapter.addData(tops, false);
        mixAdapter.notifyDataSetChanged();

        // 多布局 + 头尾布局
        HeadFootAdapter adapter = new HeadFootAdapter(mixAdapter);
        rcvMultiple.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rcvMultiple.setAdapter(adapter);

        View view = adapter.addHeaderView(R.layout.item_tag, rcvMultiple);
        ((TextView) view.findViewById(R.id.txt_tag)).setText("头布局");

        view = adapter.addHeaderView(R.layout.item_tag, rcvMultiple);
        ((TextView) view.findViewById(R.id.txt_tag)).setText("头布局二");

        view = adapter.addFootView(R.layout.item_tag, rcvMultiple);
        ((TextView) view.findViewById(R.id.txt_tag)).setText("尾布局");

        view = adapter.addFootView(R.layout.item_tag, rcvMultiple);
        ((TextView) view.findViewById(R.id.txt_tag)).setText("尾布局二");
    }

    private List<String> getTags() {
        List<String> list = new ArrayList<>();
        list.add("角色扮演");
        list.add("动作");
        list.add("换装");
        list.add("动漫");
        list.add("放置");
        list.add("经营");
        list.add("竞速");
        list.add("卡片");
        list.add("恋爱");
        list.add("美少女");
        list.add("女性向");
        list.add("消除");
        list.add("音乐节奏");
        list.add("育成");
        return list;
    }

    private List<TopDto> getTops() {
        List<TopDto> list = new ArrayList<>();

        TopDto top = new TopDto();
        top.setChineseName("公主连接");
        top.setGameRecommendText("公主连接");
        top.setGameGrade("S");
        top.setGameService("日服");
        list.add(top);

        top = new TopDto();
        top.setChineseName("小小军姬");
        top.setGameRecommendText("小小军姬");
        top.setGameGrade("A");
        top.setGameService("日服");
        list.add(top);

        top = new TopDto();
        top.setChineseName("偶像大师");
        top.setGameRecommendText("偶像大师");
        top.setGameGrade("B");
        top.setGameService("日服");
        list.add(top);

        top = new TopDto();
        top.setChineseName("公主连接");
        top.setGameRecommendText("公主连接");
        top.setGameGrade("S");
        top.setGameService("日服");
        list.add(top);

        top = new TopDto();
        top.setChineseName("小小军姬");
        top.setGameRecommendText("小小军姬");
        top.setGameGrade("A");
        top.setGameService("日服");
        list.add(top);

        top = new TopDto();
        top.setChineseName("偶像大师");
        top.setGameRecommendText("偶像大师");
        top.setGameGrade("B");
        top.setGameService("日服");
        list.add(top);
        return list;
    }
}
