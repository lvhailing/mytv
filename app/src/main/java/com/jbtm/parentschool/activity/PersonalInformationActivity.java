package com.jbtm.parentschool.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.jbtm.parentschool.R;
import com.jbtm.parentschool.models.PayModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by junde on 2018/12/7.
 */

public class PersonalInformationActivity extends AppCompatActivity {
    private int from;   //0（默认值）从顶部flag来。1未登录，从登录来


    public static void startActivity(Context context, int from) {
        Intent intent = new Intent(context, PersonalInformationActivity.class);
        intent.putExtra("from", from);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_infomation);

        initView();
        initData();

    }


    private void initView() {
        getSupportActionBar().hide();
    }

    private void initData() {
        //0（默认值）从顶部flag来。1未登录，从登录来
        from = getIntent().getIntExtra("from", 0);

    }
}
