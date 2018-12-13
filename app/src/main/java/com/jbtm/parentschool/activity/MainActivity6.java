package com.jbtm.parentschool.activity;

import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jbtm.parentschool.R;
import com.jbtm.parentschool.adapter.MyAdapter6;

public class MainActivity6 extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView recyclerView;
    private ImageView iv_title_me; // 点击跳转至个人信息
    private TextView tv_menu_jx;
    private TextView tv_menu_zmgj;
    private TextView tv_menu_hylx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main6);

        initView();
        initData();
    }

    private void initView() {
        getSupportActionBar().hide();

        iv_title_me = (ImageView) findViewById(R.id.iv_title_me);
        recyclerView = findViewById(R.id.rv_course);
        tv_menu_jx = findViewById(R.id.tv_menu_jx);
        tv_menu_zmgj = findViewById(R.id.tv_menu_zmgj);
        tv_menu_hylx = findViewById(R.id.tv_menu_hylx);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setFocusable(false);

        iv_title_me.setOnClickListener(this);
        iv_title_me.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    showOnFocusAnimation(view);
                } else {
                    showLoseFocusAnimation(view);
                }
            }
        });
        tv_menu_jx.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    showOnFocusAnimation(view);
                } else {
                    showLoseFocusAnimation(view);
                }
            }
        });
        tv_menu_zmgj.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    showOnFocusAnimation(view);
                } else {
                    showLoseFocusAnimation(view);
                }
            }
        });
        tv_menu_hylx.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    showOnFocusAnimation(view);
                } else {
                    showLoseFocusAnimation(view);
                }
            }
        });
    }

    private void initData() {
        String[] data = new String[30];
        for (int i = 0; i < data.length; i++) {
            data[i] = "item" + i;
        }
        MyAdapter6 adapter = new MyAdapter6(this, data);
        recyclerView.setAdapter(adapter);
        recyclerView.scrollToPosition(0);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_me:
                break;
        }
    }

    private void showOnFocusAnimation(View v) {
        ViewCompat.animate(v)
                .scaleX(1.1f)
                .scaleY(1.1f)
                .translationZ(1)
                .setDuration(200)
                .start();
    }

    private void showLoseFocusAnimation(View v) {
        ViewCompat.animate(v)
                .scaleX(1)
                .scaleY(1)
                .translationZ(1)
                .start();
    }
}
