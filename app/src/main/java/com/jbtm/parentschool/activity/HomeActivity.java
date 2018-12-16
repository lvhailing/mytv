package com.jbtm.parentschool.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jbtm.parentschool.R;
import com.jbtm.parentschool.adapter.HomeAdapter;
import com.jbtm.parentschool.dialog.ExitDialog;
import com.jbtm.parentschool.utils.ToastUtil;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    private long lastTime;
    private int scaleTime = 200;
    private RecyclerView recyclerView;
    private LinearLayout ll_title_me; // 点击跳转至个人信息
    private LinearLayout ll_title_buy; // 点击跳转课程详情
    private RelativeLayout rl_jx_2items;   //精选的两个item
    private RelativeLayout rl_item_1;   //精选的两个item
    private RelativeLayout rl_item_2;   //精选的两个item
    private TextView tv_menu_jx;
    private TextView tv_menu_zmgj;
    private TextView tv_menu_hylx;
    private TextView tv_menu_zyl;
    private TextView tv_menu_gdjy;
    private HomeAdapter adapter;
    private String[] rvData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initView();
        initData();
    }

    private void initView() {
        getSupportActionBar().hide();

        ll_title_me = findViewById(R.id.ll_title_me);
        ll_title_buy = findViewById(R.id.ll_title_buy);
        recyclerView = findViewById(R.id.rv_course);
        tv_menu_jx = findViewById(R.id.tv_menu_jx);
        tv_menu_zmgj = findViewById(R.id.tv_menu_zmgj);
        tv_menu_hylx = findViewById(R.id.tv_menu_hylx);
        tv_menu_zyl = findViewById(R.id.tv_menu_zyl);
        tv_menu_gdjy = findViewById(R.id.tv_menu_gdjy);

        rl_jx_2items = findViewById(R.id.rl_jx_2items);
        rl_item_1 = findViewById(R.id.rl_jx_item_1);
        rl_item_2 = findViewById(R.id.rl_jx_item_2);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setFocusable(false);

        ll_title_me.setOnClickListener(this);
        ll_title_buy.setOnClickListener(this);

        listenTvFocus(tv_menu_jx);
        listenTvFocus(tv_menu_zmgj);
        listenTvFocus(tv_menu_hylx);
        listenTvFocus(tv_menu_zyl);
        listenTvFocus(tv_menu_gdjy);
        listenJXFocus(rl_item_1);
        listenJXFocus(rl_item_2);
    }

    private void initData() {
        rvData = new String[5];
        for (int i = 0; i < rvData.length; i++) {
            rvData[i] = "item" + i;
        }
        adapter = new HomeAdapter(this, rvData);
        recyclerView.setAdapter(adapter);
        recyclerView.scrollToPosition(0);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_title_me:
                Intent intent1 = new Intent(this, PersonalInformationActivity.class);
                startActivity(intent1);
                break;
            case R.id.ll_title_buy:
                Intent intent2 = new Intent(this, CourseDetailActivity.class);
                startActivity(intent2);
                break;
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //返回键
            long nowTime = System.currentTimeMillis();
            if (nowTime - lastTime < 1500) {
                //双击了返回键，退出应用
                showExitDialog();
            } else {
                lastTime = System.currentTimeMillis();
                ToastUtil.showCustom("再按退出应用");
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void showExitDialog() {
        final ExitDialog dialog = new ExitDialog(this);
        dialog.show();
        dialog.setOnMyClickListener(new ExitDialog.MyClickListener() {
            @Override
            public void MoreTime() {
                dialog.dismiss();
                finish();
            }

            @Override
            public void Exit() {
                dialog.dismiss();
            }
        });
    }

    private void listenTvFocus(TextView v) {
        v.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (view.getTag() != null && view.getTag().equals("jx") && b) {
                    //是精选tab 并且获得了焦点
                    rl_jx_2items.setVisibility(View.VISIBLE);
                    adapter.setData(rvData);
                    Log.i("aaa", "精选tab被开启");
                    return;
                }
                if (b) {
                    if (rl_jx_2items.getVisibility() == View.VISIBLE) {
                        rl_jx_2items.setVisibility(View.GONE);
                        Log.i("aaa", "精选tab被关闭");
                    }
                    String[] data = new String[15];
                    for (int i = 0; i < data.length; i++) {
                        data[i] = "item" + i;
                    }
                    adapter.setData(data);
                    ((TextView) view).setTextSize(20);
                } else {
                    ((TextView) view).setTextSize(19);
                }
            }
        });
    }

    private void listenJXFocus(final RelativeLayout itemView) {
        itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View v, final boolean hasFocus) {
                //获取焦点时变化
                if (hasFocus) {
                    ViewCompat.animate(v)
                            .scaleX(1.1f)
                            .scaleY(1.1f)
                            .setDuration(scaleTime)
                            .setListener(new ViewPropertyAnimatorListener() {
                                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                                @Override
                                public void onAnimationStart(View view) {
                                    view.findViewById(R.id.v_bg).setVisibility(View.VISIBLE);
                                    view.bringToFront();
                                    view.setElevation(100f);   //防止被其他view z轴方向覆盖
                                }

                                @Override
                                public void onAnimationEnd(View view) {
                                }

                                @Override
                                public void onAnimationCancel(View view) {
                                }
                            })
                            .start();
                } else {
                    ViewCompat.animate(v)
                            .scaleX(1)
                            .scaleY(1)
                            .setListener(new ViewPropertyAnimatorListener() {
                                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                                @Override
                                public void onAnimationStart(View view) {
                                    view.findViewById(R.id.v_bg).setVisibility(View.GONE);
                                    view.setElevation(0f); //防止z轴方向，覆盖其他view
                                }

                                @Override
                                public void onAnimationEnd(View view) {
                                }

                                @Override
                                public void onAnimationCancel(View view) {
                                }
                            })
                            .start();
                }
            }
        });
    }
}
