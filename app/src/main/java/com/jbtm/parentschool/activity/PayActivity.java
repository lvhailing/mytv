package com.jbtm.parentschool.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.jbtm.parentschool.R;
import com.jbtm.parentschool.models.PayModel;
import com.jbtm.parentschool.utils.ToastUtil;
import com.jbtm.parentschool.widget.PayTypeView;

import java.util.ArrayList;
import java.util.List;

public class PayActivity extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout ll_title_me; // 点击跳转至个人信息
    private LinearLayout ll_title_buy; // 点击跳转课程详情
    private LinearLayout ll_dandian_arrow; // 单点的箭头，在没有单点是需隐藏
    private PayTypeView yearView;
    private PayTypeView monthView;
    private PayTypeView dandianView;
    private ImageView iv_year_arrow;
    private ImageView iv_month_arrow;
    private ImageView iv_dandian_arrow;
    private int from;   //0（默认值）从顶部flag来，则包年聚焦。1从单点购买来，则单点聚焦


    public static void startActivity(Context context, int from) {
        Intent intent = new Intent(context, PayActivity.class);
        intent.putExtra("from", from);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        initView();
        initData();
    }

    private void initView() {
        getSupportActionBar().hide();

        ll_title_me = findViewById(R.id.ll_title_me);
        ll_title_buy = findViewById(R.id.ll_title_buy);

        yearView = findViewById(R.id.v_year);
        monthView = findViewById(R.id.v_month);
        dandianView = findViewById(R.id.v_dandian);
        iv_year_arrow = findViewById(R.id.iv_year_arrow);
        iv_month_arrow = findViewById(R.id.iv_month_arrow);
        iv_dandian_arrow = findViewById(R.id.iv_dandian_arrow);
        ll_dandian_arrow = findViewById(R.id.ll_dandian_arrow);

        ll_title_me.setOnClickListener(this);
        ll_title_buy.setOnClickListener(this);

        listenFocus(yearView);
        listenFocus(monthView);
        listenFocus(dandianView);
    }

    private void initData() {
        //0（默认值）从顶部flag来，则包年聚焦。1从单点购买来，则单点聚焦
        from = getIntent().getIntExtra("from", 0);

        PayModel payModel1 = new PayModel("包年特供", "100", "150", 1);
        PayModel payModel2 = new PayModel("包月", "20", "30", 1);
        List<PayModel> list = new ArrayList<>();
        list.add(payModel1);
        list.add(payModel2);

        //设置年卡
        yearView.setPayType(1);
        yearView.setData(list.get(0));

        //设置月卡
        monthView.setPayType(2);
        monthView.setData(list.get(1));

        if (from == 0) {
            //从顶部flag来，则包年聚焦
            dandianView.setVisibility(View.GONE);
            //单点的箭头，在没有单点是需隐藏
            ll_dandian_arrow.setVisibility(View.GONE);
            yearView.requestFocus();
        } else {
            //从单点购买来，则单点聚焦
            dandianView.setVisibility(View.VISIBLE);
            dandianView.setPayType(3);
            dandianView.setData(new PayModel("单点", "10", "15", 0));
            dandianView.requestFocus();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_title_me:
                PersonalInformationActivity.startActivity(this, 0);
                break;
            case R.id.ll_title_buy:
                ToastUtil.showCustom("已经在本页");
                break;
        }
    }

    private void listenFocus(View itemView) {
        itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View v, final boolean hasFocus) {
                //获取焦点时变化
                PayTypeView payTypeView = (PayTypeView) v;
                payTypeView.setFocus(hasFocus);
                dealArrow(hasFocus, payTypeView.getPayType());
                dealFocus(v, hasFocus);
            }
        });
    }

    private void dealFocus(View v, boolean hasFocus) {
        if (hasFocus) {
            ViewCompat.animate(v)
                    .scaleX(1.05f)
//                    .scaleY(1.05f)
                    .setDuration(200)
                    .start();
        } else {
            ViewCompat.animate(v)
                    .scaleX(1)
//                    .scaleY(1)
                    .start();
        }
    }

    private void dealArrow(boolean hasFocus, int payType) {
        if (payType == 1) {
            //1包年，2包月，3单点
            if (hasFocus) {
                setVisible(iv_year_arrow);
            } else {
                setGone(iv_year_arrow);
            }
        } else if (payType == 2) {
            //1包年，2包月，3单点
            if (hasFocus) {
                setVisible(iv_month_arrow);
            } else {
                setGone(iv_month_arrow);
            }
        } else if (payType == 3) {
            //1包年，2包月，3单点
            if (hasFocus) {
                setVisible(iv_dandian_arrow);
            } else {
                setGone(iv_dandian_arrow);
            }
        }
    }

    private void setVisible(View view) {
        if (view.getVisibility() == View.GONE) {
            view.setVisibility(View.VISIBLE);
        }
    }

    private void setGone(View view) {
        if (view.getVisibility() == View.VISIBLE) {
            view.setVisibility(View.GONE);
        }
    }
}
