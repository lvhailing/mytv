package com.jbtm.parentschool;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jbtm.parentschool.border.MyBorderView;

public class MainActivity extends AppCompatActivity {
    private MyBorderView border;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initData();
    }

    private void initView() {
        getSupportActionBar().hide();

        border = new MyBorderView(this);
        border.setBackgroundResource(R.drawable.border_highlight);

        recyclerView = findViewById(R.id.rv_course);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setFocusable(false);
        border.attachTo(recyclerView);

    }

    private void initData() {
        String[] data = new String[20];
        for (int i = 0; i < data.length; i++) {
            data[i] = "item" + i;
        }
        MyAdapter adapter = new MyAdapter(this, data);
        recyclerView.setAdapter(adapter);
        recyclerView.scrollToPosition(0);
    }
}
