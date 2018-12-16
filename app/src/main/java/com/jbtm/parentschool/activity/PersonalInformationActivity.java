package com.jbtm.parentschool.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jbtm.parentschool.R;
import com.jbtm.parentschool.models.PayModel;
import com.jbtm.parentschool.utils.ToastUtil;
import com.jbtm.parentschool.widget.BuyKaAndDandianView;
import com.jbtm.parentschool.widget.BuyKaView;
import com.jbtm.parentschool.widget.BuyNothingView;
import com.jbtm.parentschool.widget.PersonalLoginNoView;
import com.jbtm.parentschool.widget.PersonalLoginYesView;
import com.jbtm.parentschool.widget.WatchHistoryView;

/**
 * Created by junde on 2018/12/7.
 */

public class PersonalInformationActivity extends AppCompatActivity implements View.OnClickListener {
    private PersonalLoginYesView v_personal_login_yes;
    private PersonalLoginNoView v_personal_login_no;
    private BuyKaAndDandianView v_buy_ka_and_dandian;
    private BuyKaView v_buy_ka;
    private BuyNothingView v_buy_nothing;
    private WatchHistoryView v_watch_history;

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

        v_personal_login_yes = findViewById(R.id.v_personal_login_yes);
        v_personal_login_no = findViewById(R.id.v_personal_login_no);
        v_buy_ka_and_dandian = findViewById(R.id.v_buy_ka_and_dandian);
        v_buy_ka = findViewById(R.id.v_buy_ka);
        v_buy_nothing = findViewById(R.id.v_buy_nothing);
        v_watch_history = findViewById(R.id.v_watch_history);

        LinearLayout ll_title_me = findViewById(R.id.ll_title_me);
        LinearLayout ll_title_buy = findViewById(R.id.ll_title_buy);
        TextView tv_menu_personal = findViewById(R.id.tv_menu_personal);
        TextView tv_menu_buy = findViewById(R.id.tv_menu_buy);
        TextView tv_menu_watch_history = findViewById(R.id.tv_menu_watch_history);

        listenTvFocus(tv_menu_personal);
        listenTvFocus(tv_menu_buy);
        listenTvFocus(tv_menu_watch_history);

        tv_menu_personal.requestFocus();

        ll_title_me.setOnClickListener(this);
        ll_title_buy.setOnClickListener(this);
    }

    private void initData() {
        //0（默认值）从顶部flag来。1未登录，从登录来
        from = getIntent().getIntExtra("from", 0);

    }

    private boolean personalFlag;
    private int buyIndex = 0;

    private void listenTvFocus(TextView v) {
        v.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                //设置焦点文字大小
                setTextSize((TextView) view, b);

                //获取到焦点时刷新界面
                if (b) {
                    switch (view.getId()) {
                        case R.id.tv_menu_personal: //个人信息
                            personalFlag = !personalFlag;
                            if (personalFlag) {
                                //个人信息：未登录显示
                                setVisible(v_personal_login_no);
                            } else {
                                //个人信息：已登录显示
                                setVisible(v_personal_login_yes);
                            }
                            break;
                        case R.id.tv_menu_buy: //订购信息
                            buyIndex++;
                            if (buyIndex == 1) {
                                //订购信息：订了卡和单点
                                setVisible(v_buy_ka_and_dandian);
                                v_buy_ka_and_dandian.setKaInfo(new PayModel("包全年套餐", "100天"));
                            } else if (buyIndex == 2) {
                                //订购信息：仅订了单点
                                setVisible(v_buy_ka_and_dandian);
                                v_buy_ka_and_dandian.setKaInfo(null);
                            } else if (buyIndex == 3) {
                                //订购信息：仅订了卡
                                setVisible(v_buy_ka);
                                v_buy_ka.setKaInfo(new PayModel("包全年套餐", "100天"));
                            } else if (buyIndex == 4) {
                                //订购信息：都没订
                                setVisible(v_buy_nothing);
                                buyIndex = 0;
                            }
                            break;
                        case R.id.tv_menu_watch_history: //观看记录
                            //观看记录
                            setVisible(v_watch_history);
                            break;
                    }
                }
            }
        });
    }

    private void setTextSize(TextView view, boolean b) {
        if (b) {
            view.setTextSize(20);
        } else {
            view.setTextSize(19);
        }
    }

    private void setVisible(View view) {
        setGone(v_personal_login_yes);
        setGone(v_personal_login_no);
        setGone(v_buy_ka_and_dandian);
        setGone(v_buy_ka);
        setGone(v_buy_nothing);
        setGone(v_watch_history);

        if (view.getVisibility() == View.GONE) {
            view.setVisibility(View.VISIBLE);
        }
    }

    private void setGone(View view) {
        if (view.getVisibility() == View.VISIBLE) {
            view.setVisibility(View.GONE);
        }
    }

    public void gotoLogin() {
        //个人信息：未登录显示
        setVisible(v_personal_login_no);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_title_me:
                ToastUtil.showCustom("已经在本页");
                break;
            case R.id.ll_title_buy:
                PayActivity.startActivity(this, 0);
                break;
        }
    }
}
