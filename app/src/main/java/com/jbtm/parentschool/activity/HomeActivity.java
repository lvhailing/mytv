package com.jbtm.parentschool.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v4.view.ViewPropertyAnimatorListenerAdapter;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jbtm.parentschool.R;
import com.jbtm.parentschool.adapter.HomeAdapter;
import com.jbtm.parentschool.dialog.ExitAppDialog;
import com.jbtm.parentschool.models.HomeColumnModel;
import com.jbtm.parentschool.models.HomeCourseModel;
import com.jbtm.parentschool.models.HomeWrapper;
import com.jbtm.parentschool.network.MyObserverAdapter;
import com.jbtm.parentschool.network.MyRemoteFactory;
import com.jbtm.parentschool.network.MyRequestProxy;
import com.jbtm.parentschool.network.model.ResultModel;
import com.jbtm.parentschool.utils.RequestUtil;
import com.jbtm.parentschool.utils.ToastUtil;
import com.jbtm.parentschool.utils.UIUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class HomeActivity extends BaseActivity implements View.OnClickListener {
    private long lastTime;
    private int scaleTime = 200;
    private LinearLayout ll_title_me; // 点击跳转至个人信息
    private LinearLayout ll_title_buy; // 点击跳转课程详情
    private LinearLayout ll_jx_2items;   //精选的两个item
    private RelativeLayout rl_jx_item_1;   //精选的两个item
    private RelativeLayout rl_jx_item_2;   //精选的两个item
    private TextView tv_menu_jx;
    private TextView tv_menu_1;
    private TextView tv_menu_2;
    private TextView tv_menu_3;
    private TextView tv_menu_4;
    private TextView tv_menu_5;
    private List<TextView> textViewList;
    private HomeAdapter adapter;
    private HomeWrapper homeWrapper;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, HomeActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initView();
        initData();
    }

    private void initView() {
        getSupportActionBar().hide();

        showProgressDialog(this);

        ll_title_me = findViewById(R.id.ll_title_me);
        ll_title_buy = findViewById(R.id.ll_title_buy);
        RecyclerView recyclerView = findViewById(R.id.rv_course);
        tv_menu_jx = findViewById(R.id.tv_menu_jx);
        tv_menu_1 = findViewById(R.id.tv_menu_1);
        tv_menu_2 = findViewById(R.id.tv_menu_2);
        tv_menu_3 = findViewById(R.id.tv_menu_3);
        tv_menu_4 = findViewById(R.id.tv_menu_4);
        tv_menu_5 = findViewById(R.id.tv_menu_5);

        ll_jx_2items = findViewById(R.id.ll_jx_2items);
        rl_jx_item_1 = findViewById(R.id.rl_jx_item_1);
        rl_jx_item_2 = findViewById(R.id.rl_jx_item_2);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setFocusable(false);
        adapter = new HomeAdapter(this, null);
        recyclerView.setAdapter(adapter);

        ll_title_me.setOnClickListener(this);
        ll_title_buy.setOnClickListener(this);
        rl_jx_item_1.setOnClickListener(this);
        rl_jx_item_2.setOnClickListener(this);

        listenTvFocus(tv_menu_jx);
        listenTvFocus(tv_menu_1);
        listenTvFocus(tv_menu_2);
        listenTvFocus(tv_menu_3);
        listenTvFocus(tv_menu_4);
        listenTvFocus(tv_menu_5);
        listenJXFocus(rl_jx_item_1);
        listenJXFocus(rl_jx_item_2);

        textViewList = new ArrayList<>();
        textViewList.add(tv_menu_1);
        textViewList.add(tv_menu_2);
        textViewList.add(tv_menu_3);
        textViewList.add(tv_menu_4);
        textViewList.add(tv_menu_5);
    }

    private void initData() {
        Map<String, Object> map = RequestUtil.getBasicMapNoBusinessParams();

        MyRemoteFactory.getInstance().getProxy(MyRequestProxy.class)
                .getHomeData(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyObserverAdapter<ResultModel<HomeWrapper>>() {
                    @Override
                    public void onMyError(Throwable e) {
                        closeProgressDialog();
                        ToastUtil.showCustom("调接口失败");
                    }

                    @Override
                    public void onMySuccess(ResultModel<HomeWrapper> result) {
                        closeProgressDialog();

                        if (result.result != null) {
                            homeWrapper = result.result;
                            //设置menu
                            setMenu();

                            //设置精选数据
                            setMenuJx();
                        }
                    }
                });
    }

    //设置精选数据
    private void setMenuJx() {
        List<HomeCourseModel> recommendList = homeWrapper.recommend_list;
        if (recommendList == null || recommendList.size() <= 0) {
            return;
        }

        //设置两张精选的图片
        ImageView iv1 = rl_jx_item_1.findViewById(R.id.iv);
        ImageView iv2 = rl_jx_item_2.findViewById(R.id.iv);
        if (recommendList.size() == 1) {
            iv2.setVisibility(View.GONE);
            setImageView(iv1, recommendList.get(0).photo);
            return;
        }
        setImageView(iv1, recommendList.get(0).photo);
        setImageView(iv2, recommendList.get(1).photo);

        if (recommendList.size() > 2) {
            //设置recyclerView
            adapter.setData(recommendList);
        }
    }

    //设置menu
    private void setMenu() {
        List<HomeColumnModel> columnList = homeWrapper.column_list;
        if (columnList == null) {
            return;
        }
        for (int i = 0; i < columnList.size(); i++) {
            textViewList.get(i).setText(columnList.get(i).title);
            textViewList.get(i).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_title_me:
                PersonalInformationActivity.startActivity(this, 0);
                break;
            case R.id.ll_title_buy:
                PayActivity.startActivity(this);
                break;
            case R.id.rl_jx_item_1: //精选课程，大图第1张
                CourseDetailActivity.startActivity(this, homeWrapper.recommend_list.get(0).course_id);
                break;
            case R.id.rl_jx_item_2: //精选课程，大图第2张
                CourseDetailActivity.startActivity(this, homeWrapper.recommend_list.get(1).course_id);
                break;
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //返回键
            long nowTime = System.currentTimeMillis();
            if (nowTime - lastTime < 1500) {
                //双击了返回键，退出应用
                showExitAppDialog();
            } else {
                lastTime = System.currentTimeMillis();
                ToastUtil.showCustom("再按退出应用");
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void showExitAppDialog() {
        final ExitAppDialog dialog = new ExitAppDialog(this);
        dialog.show();
        dialog.setOnMyClickListener(new ExitAppDialog.MyClickListener() {
            @Override
            public void moreTime() {
                dialog.dismiss();
            }

            @Override
            public void exit() {
                dialog.dismiss();
                finish();
            }
        });
    }

    private void listenTvFocus(TextView v) {
        v.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (view.getTag() != null && view.getTag().equals("jx") && b) {
                    //是精选tab 并且获得了焦点
                    ll_jx_2items.setVisibility(View.VISIBLE);
                    setMenuJx();
                    Log.i("aaa", "精选tab被开启");
                    return;
                }
                if (b) {
                    if (ll_jx_2items.getVisibility() == View.VISIBLE) {
                        ll_jx_2items.setVisibility(View.GONE);
                        Log.i("aaa", "精选tab被关闭");
                    }
                    if (view.getId() == R.id.tv_menu_1) {
                        adapter.setData(homeWrapper.column_list.get(0).items);  //第1个menu
                    } else if (view.getId() == R.id.tv_menu_2) {
                        adapter.setData(homeWrapper.column_list.get(1).items);  //第2个menu
                    } else if (view.getId() == R.id.tv_menu_3) {
                        adapter.setData(homeWrapper.column_list.get(2).items);  //第3个menu
                    } else if (view.getId() == R.id.tv_menu_4) {
                        adapter.setData(homeWrapper.column_list.get(3).items);  //第4个menu
                    } else if (view.getId() == R.id.tv_menu_5) {
                        adapter.setData(homeWrapper.column_list.get(4).items);  //第5个menu
                    }
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
                            .scaleX(1.05f)
                            .scaleY(1.05f)
                            .setDuration(scaleTime)
                            .setListener(new ViewPropertyAnimatorListenerAdapter() {
                                @Override
                                public void onAnimationStart(View view) {
                                    view.findViewById(R.id.v_bg).setVisibility(View.VISIBLE);
                                }
                            })
                            .start();
                } else {
                    ViewCompat.animate(v)
                            .scaleX(1)
                            .scaleY(1)
                            .setListener(new ViewPropertyAnimatorListenerAdapter() {
                                @Override
                                public void onAnimationStart(View view) {
                                    view.findViewById(R.id.v_bg).setVisibility(View.GONE);
                                }
                            })
                            .start();
                }
            }
        });
    }
}
