package com.csp.sample.adapter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.csp.sample.adapter.adapter.MixAdapter;
import com.csp.sample.adapter.adapter.TagAdapter;
import com.csp.sample.adapter.dto.TopDto;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView rcvSingle = findViewById(R.id.rcv_single);
        RecyclerView rcvMultiple = findViewById(R.id.rcv_multiple);

        // 单布局
        TagAdapter tagAdapter = new TagAdapter(this);
        rcvSingle.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rcvSingle.setAdapter(tagAdapter);

        tagAdapter.setOnItemClickListener((parent, view, holder, position, id) -> {
            String item = tagAdapter.getItem(position);
            Toast.makeText(this, "删除该标签：" + item, Toast.LENGTH_SHORT).show();
        });

        // 多布局
        MixAdapter mixAdapter = new MixAdapter(this);
        rcvMultiple.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rcvMultiple.setAdapter(mixAdapter);

        addData(tagAdapter);
        addData(mixAdapter);
    }

    private void addData(TagAdapter adapter) {
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
        adapter.addData(list, false);
        adapter.notifyDataSetChanged();
    }

    private void addData(MixAdapter adapter) {
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

        adapter.addData(list, false);
        adapter.notifyDataSetChanged();
    }
}
