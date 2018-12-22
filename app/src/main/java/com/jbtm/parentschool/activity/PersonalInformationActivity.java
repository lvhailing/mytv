package com.jbtm.parentschool.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jbtm.parentschool.R;
import com.jbtm.parentschool.models.OrderWrapper;
import com.jbtm.parentschool.models.PayModel;
import com.jbtm.parentschool.models.WatchHistoryModel;
import com.jbtm.parentschool.network.MyObserverAdapter;
import com.jbtm.parentschool.network.MyRemoteFactory;
import com.jbtm.parentschool.network.MyRequestProxy;
import com.jbtm.parentschool.network.model.ResultModel;
import com.jbtm.parentschool.utils.RequestUtil;
import com.jbtm.parentschool.utils.SPUtil;
import com.jbtm.parentschool.utils.ToastUtil;
import com.jbtm.parentschool.widget.BuyKaAndDandianView;
import com.jbtm.parentschool.widget.BuyKaView;
import com.jbtm.parentschool.widget.BuyNothingView;
import com.jbtm.parentschool.widget.PersonalLoginNoView;
import com.jbtm.parentschool.widget.PersonalLoginYesView;
import com.jbtm.parentschool.widget.WatchHistoryView;

import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by junde on 2018/12/7.
 */

public class PersonalInformationActivity extends BaseActivity implements View.OnClickListener {
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

        ll_title_me.setOnClickListener(this);
        ll_title_buy.setOnClickListener(this);

        //0（默认值）从顶部flag来。1未登录，从登录来
        from = getIntent().getIntExtra("from", 0);

        if (from == 1) {
            //未登录，则禁用focus
            tv_menu_personal.setFocusable(false);
            tv_menu_buy.setFocusable(false);
            tv_menu_watch_history.setFocusable(false);
            ll_title_me.setFocusable(false);
            ll_title_buy.setFocusable(false);

            //未登录界面呈现
            setVisible(v_personal_login_no);
            v_personal_login_no.setFrom(from);
        } else {
            //已登录，则默认选中个人信息
            tv_menu_personal.requestFocus();
        }
    }

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
                            if (TextUtils.isEmpty(SPUtil.getToken())) {
                                //个人信息：未登录显示
                                setVisible(v_personal_login_no);
                            } else {
                                //个人信息：已登录显示
                                setVisible(v_personal_login_yes);
                            }
                            break;
                        case R.id.tv_menu_buy: //订购信息
//                            setMenuBuy();
                            setMenuBuyPresent();
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

    //获取订购信息的数据，呈现在界面上
    private void setMenuBuyPresent() {
        Map<String, Object> params = RequestUtil.getBasicMapNoBusinessParams();

        MyRemoteFactory.getInstance().getProxy(MyRequestProxy.class)
                .getMyOrders(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyObserverAdapter<ResultModel<OrderWrapper>>() {
                    @Override
                    public void onMyError(Throwable e) {
                        ToastUtil.showCustom("调接口失败");
                    }

                    @Override
                    public void onMySuccess(ResultModel<OrderWrapper> result) {
                        //套餐包业务
                        List<PayModel> myPackages = result.result.my_packages;
                        //单点业务
                        List<WatchHistoryModel> myCourses = result.result.my_courses;

                        boolean hasKa = myPackages != null && myPackages.size() > 0;
                        boolean hasDandian = myCourses != null && myCourses.size() > 0;

                        if (hasKa && hasDandian) {
                            //订购信息：订了卡和单点
                            setVisible(v_buy_ka_and_dandian);
                            v_buy_ka_and_dandian.setKaInfo(myPackages.get(0));
                            v_buy_ka_and_dandian.setDandianInfo(myCourses);
                        } else if (hasDandian) {
                            //订购信息：仅订了单点
                            setVisible(v_buy_ka_and_dandian);
                            v_buy_ka_and_dandian.setKaInfo(null);
                            v_buy_ka_and_dandian.setDandianInfo(myCourses);
                        } else if (hasKa) {
                            //订购信息：仅订了卡
                            setVisible(v_buy_ka);
                            v_buy_ka.setKaInfo(myPackages.get(0));
                        } else {
                            //订购信息：都没订
                            setVisible(v_buy_nothing);
                        }
                    }
                });
    }

    //将本地写死的数据，呈现在界面上
    private void setMenuBuy() {
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
        //退出登录，应关闭之前所有界面，重新打开登录页
        SPUtil.putToken("");
        PersonalInformationActivity.startActivity(this, 1);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_title_me:
                ToastUtil.showCustom("已经在本页");
                break;
            case R.id.ll_title_buy:
                PayActivity.startActivity(this);
                break;
        }
    }
}
