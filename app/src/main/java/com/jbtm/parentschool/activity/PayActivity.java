package com.jbtm.parentschool.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

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
import com.jbtm.parentschool.utils.UIUtil;
import com.jbtm.parentschool.utils.ZXingUtil;
import com.jbtm.parentschool.widget.PayTypeView;

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
    private TextView tv_pay_result;
    private View v_pay_bg;
    private ProgressBar pb;
    private int from;   //0（默认值）从顶部flag来，则包年聚焦。1从单点购买来，则单点聚焦
    private List<PayModel> payModelList;
    private int courseId;   //支付时 课程ID（点播方式必传）
    private int mKaType = 1;    //1包年，2包月，3单点。默认包年套餐
    private int mPayType = 2;    //1微信，2支付宝。默认支付宝
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
        tv_pay_result = findViewById(R.id.tv_pay_result);
        iv_zfb = findViewById(R.id.iv_zfb);
        iv_wx = findViewById(R.id.iv_wx);
        iv_qrcode = findViewById(R.id.iv_qrcode);
        iv_pay_success = findViewById(R.id.iv_pay_success);
        v_pay_bg = findViewById(R.id.v_pay_bg);
        pb = findViewById(R.id.pb);

        ll_title_me.setOnClickListener(this);
        ll_title_buy.setOnClickListener(this);
//        iv_zfb.setOnClickListener(this);
//        iv_wx.setOnClickListener(this);

        listenFocus(yearView);
        listenFocus(monthView);
        listenFocus(dandianView);
        listenFocus(iv_zfb);
        listenFocus(iv_wx);
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

                            //二维码支付结果文案处理，默认年卡价格
                            refreshPayText(payModelList.get(0).price);
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
            refreshPayText(coursePrice);
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
//            case R.id.iv_zfb:   //支付宝支付
//                getPayUrl(2);
//                break;
//            case R.id.iv_wx:   //微信支付
//                getPayUrl(1);
//                break;
        }
    }

    //先获取支付的url，然后生成二维码
    private void getPayUrl() {
        Map<String, Object> map = new HashMap<>();
        map.put("pay_type", mPayType);      //支付方式（1微信 2支付宝）
        map.put("order_type", mKaType);  //订单类型（1包年 2包月 3点播）
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
                            refreshQrCode(true);

                            //开启轮询，获取支付结果
                            startCountDown(result.result.order_id);
                        }
                    }
                });
    }

    //开启轮询，获取支付结果
    private void startCountDown(final int orderId) {
        if (timer == null) {
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
                        if (timer != null) {
                            timer.cancel();
                            timer = null;
                        }
                        showPaySuccess();
                    }
                });
    }

    //套餐焦点变换时，刷新付款金额
    public void refreshPayText(String price) {
        UIUtil.setPayResultStyle(tv_pay_result, "扫码支付" + price + "元");
    }

    //套餐焦点变换时，刷新二维码和加载条
    public void refreshQrCode(boolean isShowQrCode) {
        if (isShowQrCode) {
            //显示二维码
            setVisible(iv_qrcode);
            setInvisible(pb);
            setInvisible(v_pay_bg);
            setInvisible(iv_pay_success);
        } else {
            //显示加载框
            setVisible(pb);
            setInvisible(iv_qrcode);
            setInvisible(v_pay_bg);
            setInvisible(iv_pay_success);
        }
    }

    //付款成功后刷新二维码
    private void showPaySuccess() {
        tv_pay_result.setText("付款成功！");
        setVisible(v_pay_bg);
        setVisible(iv_pay_success);
        setInvisible(pb);
    }

    private void listenFocus(View itemView) {
        itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View v, final boolean hasFocus) {
                //焦点变化时：获取二维码地址并展示
                dealPayUrl(v, hasFocus);

                if (v instanceof PayTypeView) {
                    //焦点变化时：处理各子view背景字体
                    PayTypeView viewModel = (PayTypeView) v;
                    viewModel.setFocus(hasFocus);

                    //焦点变化时：处理箭头
                    dealArrow(hasFocus, viewModel.getPayType());

                    //焦点变化时：处理各子view动画
                    dealFocusAnim(v, hasFocus);
                }
            }
        });
    }

    //焦点变化时：获取二维码地址并展示
    private void dealPayUrl(View v, boolean hasFocus) {
        if (hasFocus) {
            refreshQrCode(false);
            if (v.getId() == R.id.v_year) {
                //包月：1包年，2包月，3单点
                mKaType = 1;
            } else if (v.getId() == R.id.v_month) {
                //包月：1包年，2包月，3单点
                mKaType = 2;
            } else if (v.getId() == R.id.v_dandian) {
                //包月：1包年，2包月，3单点
                mKaType = 3;
            } else if (v.getId() == R.id.iv_wx) {
                //微信支付：支付方式（1微信 2支付宝）
                mPayType = 1;
            } else if (v.getId() == R.id.iv_zfb) {
                //支付宝支付：支付方式（1微信 2支付宝）
                mPayType = 2;
            }
            getPayUrl();
        }
    }

    //处理焦点时动画
    private void dealFocusAnim(View v, boolean hasFocus) {
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

    //处理焦点时箭头
    private void dealArrow(boolean hasFocus, int payType) {
        //1包年，2包月，3单点
        if (payType == 1) {
            if (hasFocus) {
                setVisible(iv_year_arrow);
            } else {
                setGone(iv_year_arrow);
            }
        } else if (payType == 2) {
            if (hasFocus) {
                setVisible(iv_month_arrow);
            } else {
                setGone(iv_month_arrow);
            }
        } else if (payType == 3) {
            if (hasFocus) {
                setVisible(iv_dandian_arrow);
            } else {
                setGone(iv_dandian_arrow);
            }
        }
    }

    private void setVisible(View view) {
        if (view.getVisibility() != View.VISIBLE) {
            view.setVisibility(View.VISIBLE);
        }
    }

    private void setInvisible(View view) {
        if (view.getVisibility() != View.INVISIBLE) {
            view.setVisibility(View.INVISIBLE);
        }
    }

    private void setGone(View view) {
        if (view.getVisibility() != View.GONE) {
            view.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
}
