package com.jbtm.parentschool.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import com.jbtm.parentschool.adapter.MyAdapter;
import com.jbtm.parentschool.R;
import com.jbtm.parentschool.border.MyBorderView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private MyBorderView border;
    private RecyclerView recyclerView;
    private ImageView iv_title_me; // 点击跳转至个人信息

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

        iv_title_me = (ImageView) findViewById(R.id.iv_title_me);
        recyclerView = findViewById(R.id.rv_course);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setFocusable(false);
        border.attachTo(recyclerView);

        iv_title_me.setOnClickListener(this);
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

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_ENTER:
                Intent intent = new Intent(this, PersonalInformationActivity.class);
                startActivity(intent);
                //按下ok键
                break;
            case KeyEvent.KEYCODE_DPAD_CENTER:
                //按下中间键
                break;

            case KeyEvent.KEYCODE_DPAD_DOWN:
                //按下下方向键
                break;

            case KeyEvent.KEYCODE_DPAD_LEFT:
                //按下左方向键
                break;

            case KeyEvent.KEYCODE_DPAD_RIGHT:
                //按下右方向键
                break;

            case KeyEvent.KEYCODE_DPAD_UP:
                //按下上方向键
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_me:
                break;
        }
    }
}
