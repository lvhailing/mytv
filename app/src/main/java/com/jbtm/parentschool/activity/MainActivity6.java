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
    private ImageView iv_title_buy; // 点击跳转至个人信息
    private TextView tv_menu_jx;
    private TextView tv_menu_zmgj;
    private TextView tv_menu_hylx;
    private TextView tv_menu_zyl;
    private TextView tv_menu_gdjy;
    private TextView tv_menu_hbjd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main6);

        initView();
        initData();
    }

    private void initView() {
        getSupportActionBar().hide();

        iv_title_me = findViewById(R.id.iv_title_me);
        iv_title_buy = findViewById(R.id.iv_title_buy);
        recyclerView = findViewById(R.id.rv_course);
        tv_menu_jx = findViewById(R.id.tv_menu_jx);
        tv_menu_zmgj = findViewById(R.id.tv_menu_zmgj);
        tv_menu_hylx = findViewById(R.id.tv_menu_hylx);
        tv_menu_zyl = findViewById(R.id.tv_menu_zyl);
        tv_menu_gdjy = findViewById(R.id.tv_menu_gdjy);
        tv_menu_hbjd = findViewById(R.id.tv_menu_hbjd);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setFocusable(false);

        iv_title_me.setOnClickListener(this);
        listenFocus(iv_title_me);
        listenFocus(iv_title_buy);
        listenFocus(tv_menu_jx);
        listenFocus(tv_menu_zmgj);
        listenFocus(tv_menu_hylx);
        listenFocus(tv_menu_zyl);
        listenFocus(tv_menu_gdjy);
        listenFocus(tv_menu_hbjd);
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

    private void listenFocus(View v) {
        v.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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

    private void showLoseFocusAnimation(View v) {
        ViewCompat.animate(v)
                .scaleX(1)
                .scaleY(1)
                .translationZ(1)
                .start();
    }
}
