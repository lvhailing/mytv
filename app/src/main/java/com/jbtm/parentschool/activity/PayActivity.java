package com.jbtm.parentschool.activity;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jbtm.parentschool.R;
import com.jbtm.parentschool.models.CommonModel;
import com.jbtm.parentschool.models.HomeWrapper;
import com.jbtm.parentschool.models.PayModel;
import com.jbtm.parentschool.network.MyObserverAdapter;
import com.jbtm.parentschool.network.MyRemoteFactory;
import com.jbtm.parentschool.network.MyRequestProxy;
import com.jbtm.parentschool.network.model.ResultModel;
import com.jbtm.parentschool.utils.RequestUtil;
import com.jbtm.parentschool.utils.ToastUtil;
import com.jbtm.parentschool.utils.ZXingUtil;
import com.jbtm.parentschool.widget.PayTypeView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class PayActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout ll_title_me; // 点击跳转至个人信息
    private LinearLayout ll_title_buy; // 点击跳转课程详情
    private LinearLayout ll_dandian_arrow; // 单点的箭头，在没有单点是需隐藏
    private PayTypeView yearView;
    private PayTypeView monthView;
    private PayTypeView dandianView;
    private ImageView iv_year_arrow;
    private ImageView iv_month_arrow;
    private ImageView iv_dandian_arrow;
    private ImageView iv_zfb;
    private ImageView iv_wx;
    private ImageView iv_qrcode;
    private ImageView iv_pay_success;
    private View v_pay_bg;
    private int from;   //0（默认值）从顶部flag来，则包年聚焦。1从单点购买来，则单点聚焦
    private List<PayModel> payModelList;
    private int courseId;   //支付时 课程ID（点播方式必传）
    private int mPayType;    //1包年，2包月，3单点
    private CountDownTimer timer;   //轮询器，每10秒轮询一次支付结果


    public static void startActivity(Context context) {
        Intent intent = new Intent(context, PayActivity.class);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, int from, int courseId, String coursePrice) {
        Intent intent = new Intent(context, PayActivity.class);
        intent.putExtra("from", from);
        intent.putExtra("courseId", courseId);
        intent.putExtra("coursePrice", coursePrice);
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

        showProgressDialog(this);

        ll_title_me = findViewById(R.id.ll_title_me);
        ll_title_buy = findViewById(R.id.ll_title_buy);

        yearView = findViewById(R.id.v_year);
        monthView = findViewById(R.id.v_month);
        dandianView = findViewById(R.id.v_dandian);
        iv_year_arrow = findViewById(R.id.iv_year_arrow);
        iv_month_arrow = findViewById(R.id.iv_month_arrow);
        iv_dandian_arrow = findViewById(R.id.iv_dandian_arrow);
        ll_dandian_arrow = findViewById(R.id.ll_dandian_arrow);
        iv_zfb = findViewById(R.id.iv_zfb);
        iv_wx = findViewById(R.id.iv_wx);
        iv_qrcode = findViewById(R.id.iv_qrcode);
        iv_pay_success = findViewById(R.id.iv_pay_success);
        v_pay_bg = findViewById(R.id.v_pay_bg);

        ll_title_me.setOnClickListener(this);
        ll_title_buy.setOnClickListener(this);
        iv_zfb.setOnClickListener(this);
        iv_wx.setOnClickListener(this);

        listenFocus(yearView);
        listenFocus(monthView);
        listenFocus(dandianView);
    }

    private void initData() {
        //0（默认值）从顶部flag来，则包年聚焦。1从单点购买来，则单点聚焦
        from = getIntent().getIntExtra("from", 0);

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
                            payModelList = result.result.package_list;

                            //设置年卡
                            yearView.setPayType(1);
                            yearView.setData(payModelList.get(0));

                            //设置月卡
                            monthView.setPayType(2);
                            monthView.setData(payModelList.get(1));
                        }
                    }
                });

        if (from == 1) {
            //从单点购买来，则单点聚焦
            //设置单点
            //支付时 课程ID（点播方式必传）
            courseId = getIntent().getIntExtra("courseId", 0);
            String coursePrice = getIntent().getStringExtra("coursePrice");
            dandianView.setVisibility(View.VISIBLE);
            dandianView.setPayType(3);
            dandianView.setData(new PayModel("单点", coursePrice, null, 0));
            dandianView.requestFocus();
            return;
        }

        //从顶部flag来，则包年聚焦
        dandianView.setVisibility(View.GONE);
        //单点的箭头，在没有单点是需隐藏
        ll_dandian_arrow.setVisibility(View.GONE);
        yearView.requestFocus();
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
            case R.id.iv_zfb:   //支付宝支付
                getPayUrl(2);
                break;
            case R.id.iv_wx:   //微信支付
                getPayUrl(1);
                break;
        }
    }

    //先获取支付的url，然后生成二维码
    private void getPayUrl(int type) {
        Map<String, Object> map = new HashMap<>();
        map.put("pay_type", type);      //支付方式（1微信 2支付宝）
        map.put("order_type", mPayType);  //订单类型（1包年 2包月 3点播）
        if (courseId != 0) {
            //非点播不传
            map.put("content_id", courseId);      //课程ID（点播方式必传）
        }
        RequestUtil.getBasicMap(map);

        MyRemoteFactory.getInstance().getProxy(MyRequestProxy.class)
                .makeOrder(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyObserverAdapter<ResultModel<CommonModel>>() {
                    @Override
                    public void onMyError(Throwable e) {
                        closeProgressDialog();
                        ToastUtil.showCustom("调接口失败");
                    }

                    @Override
                    public void onMySuccess(ResultModel<CommonModel> result) {
                        closeProgressDialog();

                        if (result.result != null) {
                            //订单的付款二维码地址
                            String qrCodeUrl = result.result.qrcode_url;

                            //设置二维码图片
                            Bitmap bitmap = ZXingUtil.createQRImage(qrCodeUrl, iv_qrcode.getWidth(), iv_qrcode.getHeight());
                            iv_qrcode.setImageBitmap(bitmap);

                            //开启轮询，获取支付结果
                            startCountDown(result.result.order_id);
                        }
                    }
                });
    }

    //开启轮询，获取支付结果
    private void startCountDown(final int orderId) {
        timer = new CountDownTimer(30 * 60_000, 10_000) {
            @Override
            public void onTick(long millisUntilFinished) {
                //轮询支付接口
                getPayResult(orderId);
            }

            @Override
            public void onFinish() {
            }
        };
        timer.start();
    }

    //轮询支付接口
    private void getPayResult(int orderId) {
        Map<String, Object> map = new HashMap<>();
        map.put("order_id", orderId);
        RequestUtil.getBasicMap(map);

        MyRemoteFactory.getInstance().getProxy(MyRequestProxy.class)
                .getPayResult(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyObserverAdapter<ResultModel>() {
                    @Override
                    public void onMyError(Throwable e) {
                        closeProgressDialog();
                        ToastUtil.showCustom("调接口失败");
                    }

                    @Override
                    public void onMySuccess(ResultModel result) {
                        closeProgressDialog();
                        ToastUtil.showCustom(result.msg);
                        //成获取到支付结果
                        timer.cancel();
                        showPaySuccess();
                    }
                });
    }

    private void showPaySuccess() {
        v_pay_bg.setVisibility(View.VISIBLE);
        iv_pay_success.setVisibility(View.VISIBLE);
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
                mPayType = 1;
            } else {
                setGone(iv_year_arrow);
            }
        } else if (payType == 2) {
            //1包年，2包月，3单点
            if (hasFocus) {
                setVisible(iv_month_arrow);
                mPayType = 2;
            } else {
                setGone(iv_month_arrow);
            }
        } else if (payType == 3) {
            //1包年，2包月，3单点
            if (hasFocus) {
                setVisible(iv_dandian_arrow);
                mPayType = 3;
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }
}
