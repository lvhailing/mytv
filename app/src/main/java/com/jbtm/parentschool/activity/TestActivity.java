package com.jbtm.parentschool.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jbtm.parentschool.R;
import com.jbtm.parentschool.models.CommonModel;
import com.jbtm.parentschool.network.MyObserverAdapter;
import com.jbtm.parentschool.network.MyRemoteFactory;
import com.jbtm.parentschool.network.MyRequestProxy;
import com.jbtm.parentschool.network.model.DataModel;
import com.jbtm.parentschool.network.model.DataWrapper;
import com.jbtm.parentschool.network.model.ResultModel;
import com.jbtm.parentschool.utils.RequestUtil;
import com.jbtm.parentschool.utils.SPUtil;
import com.jbtm.parentschool.utils.ToastUtil;
import com.jbtm.parentschool.utils.ZXingUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class TestActivity extends AppCompatActivity {
    private TextView textView;
    private ImageView imageView;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        textView = findViewById(R.id.tv);
        imageView = findViewById(R.id.iv);
        editText = findViewById(R.id.et);

    }

    public void makeQrCode(View view) {
        Bitmap bitmap = ZXingUtil.createQRImage("https://qr.alipay.com/bax04979wslvcoe3of1z007a",
                imageView.getWidth(), imageView.getHeight());
        imageView.setImageBitmap(bitmap);
    }

    public void login(View view) {
        String num = editText.getText().toString();
        Map<String, Object> params = new HashMap<>();
        params.put("phone", "18001122787");
        params.put("captcha", num);
        RequestUtil.getBasicMap(params);

        MyRemoteFactory.getInstance().getProxy(MyRequestProxy.class)
                .login(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyObserverAdapter<ResultModel<CommonModel>>() {
                    @Override
                    public void onMyError(Throwable e) {
//                        ToastUtil.showCustom("调接口失败");
                    }

                    @Override
                    public void onMySuccess(ResultModel<CommonModel> result) {

                        SPUtil.setToken(result.result.access_token);

                        ToastUtil.showCustom(result.result.access_token);
                    }
                });
    }

    public void captcha(View view) {
        Map<String, Object> params = new HashMap<>();
        params.put("phone", "18001122787");
        RequestUtil.getBasicMap(params);

        MyRemoteFactory.getInstance().getProxy(MyRequestProxy.class)
                .sendCaptcha(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyObserverAdapter<ResultModel>() {
                    @Override
                    public void onMyError(Throwable e) {
//                        ToastUtil.showCustom("调接口失败");
                    }

                    @Override
                    public void onMySuccess(ResultModel result) {
                        ToastUtil.showCustom("验证码发送成功");
                    }
                });
    }

    private void getBooks(String id) {
        Map<String, Object> params = new HashMap<>();
        params.put("type", "caijing");
        params.put("key", "cf2e8c721799bbc8f3c9d639a4d0a9e6");

        MyRemoteFactory.getInstance().getProxy(MyRequestProxy.class)
                .getBookListByPost(params)
//                .getBookListByGet()   //get请求样例
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyObserverAdapter<ResultModel<DataWrapper<DataModel>>>() {
                    @Override
                    public void onMyError(Throwable e) {
                        //server取单据失败
//                        ToastUtil.showCustom("调接口失败");
                    }

                    @Override
                    public void onMySuccess(ResultModel<DataWrapper<DataModel>> result) {
                        //server取单据成功
                        if (result != null && result.result != null
                                && result.result.data != null && result.result.data.size() > 0) {
                            List<DataModel> list = result.result.data;

                            textView.setText(list.get(1).category);

                            Glide.with(TestActivity.this)
                                    .load(list.get(0).thumbnail_pic_s)
                                    .into(imageView);
                        }
                    }
                });
    }
}
