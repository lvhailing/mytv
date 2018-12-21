package com.jbtm.parentschool.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;


public class BaseActivity extends AppCompatActivity {
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    public void showProgressDialog(Context context) {
        showProgressDialog(context, true);
    }

    public void showProgressDialog(Context context, boolean cancelable) {
        if (isFinishing()) {
            return;
        }
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(context);
            mProgressDialog.setProgress(ProgressDialog.STYLE_SPINNER);//圆形
            mProgressDialog.setMessage("加载中，请稍后...");
            mProgressDialog.setCancelable(cancelable);
        }
        if (!isFinishing() && !mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }
    }

    public void closeProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing() && !isFinishing()) {
            mProgressDialog.dismiss();
        }
    }

    public void setImageView(ImageView imageView, String url) {
        Glide.with(this)
                .load(url)
                .into(imageView);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mProgressDialog != null) {
            closeProgressDialog();
            mProgressDialog = null;
        }
    }
}
