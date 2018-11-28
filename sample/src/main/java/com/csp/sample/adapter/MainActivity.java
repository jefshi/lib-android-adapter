package com.csp.sample.adapter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView rcvSingle = findViewById(R.id.rcv_single);
        RecyclerView rcvMultiple = findViewById(R.id.rcv_multiple);


    }
}
