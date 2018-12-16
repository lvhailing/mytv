package com.jbtm.parentschool.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.jbtm.parentschool.R;
import com.jbtm.parentschool.adapter.CourseDetailAdapter;
import com.jbtm.parentschool.models.MaterModel;
import com.jbtm.parentschool.widget.FullyGridLayoutManager;

import java.util.ArrayList;
import java.util.List;

public class CourseDetailActivity extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout ll_title_me; // 点击跳转至个人信息
    private LinearLayout ll_title_buy; // 点击跳转课程详情
    private RelativeLayout rl_play; //全屏播放
    private RelativeLayout rl_buy; //单点这一课

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, CourseDetailActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);

        initView();
        initData();
    }

    private void initView() {
        getSupportActionBar().hide();

        ll_title_me = findViewById(R.id.ll_title_me);
        ll_title_buy = findViewById(R.id.ll_title_buy);
        rl_play = findViewById(R.id.rl_play);
        rl_buy = findViewById(R.id.rl_buy);
        RecyclerView recyclerView = findViewById(R.id.rv_course);

        ll_title_me.setOnClickListener(this);
        ll_title_buy.setOnClickListener(this);
        rl_play.setOnClickListener(this);
        rl_buy.setOnClickListener(this);

        FullyGridLayoutManager gridLayoutManager = new FullyGridLayoutManager(this, 3);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(gridLayoutManager);

        List<MaterModel> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            list.add(new MaterModel("美国教育" + i, "12:30"));
        }
        CourseDetailAdapter adapter = new CourseDetailAdapter(this, list);
        recyclerView.setAdapter(adapter);
    }

    private void initData() {
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_title_me:
//                Intent intent = new Intent(this, PersonalInformationActivity.class);
//                startActivity(intent);
                break;
            case R.id.ll_title_buy:
                PayActivity.startActivity(this, 2);
                break;
            case R.id.rl_buy:  //单点这一课购买
                PayActivity.startActivity(this, 1);
                break;
            case R.id.rl_play:  //全屏播放
                VideoActivity.startActivity(this);
                break;
        }
    }


}
