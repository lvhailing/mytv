package com.jbtm.parentschool.adapter;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jbtm.parentschool.R;

public class MyAdapter6 extends RecyclerView.Adapter<MyAdapter6.ViewHolder> {
    private String[] mData;
    private Context mContext;

    public MyAdapter6(Context context, String[] data) {
        super();
        mContext = context;
        mData = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.mTextView.setText(mData[i]);
    }

    @Override
    public int getItemCount() {
        return mData.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.textView);

            itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(final View v, boolean hasFocus) {
                    //获取焦点时变化
                    if (hasFocus) {
                        ViewCompat.animate(v)
                                .scaleX(1.2f)
                                .scaleY(1.2f)
                                .translationZ(1)
                                .setDuration(200)
                                .setListener(new ViewPropertyAnimatorListener() {
                                    @Override
                                    public void onAnimationStart(View view) {

                                    }

                                    @Override
                                    public void onAnimationEnd(View view) {

                                        v.bringToFront();
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
                                .translationZ(1)
                                .start();
                    }
                }
            });
        }
    }

    public void setData(String[] data) {
        this.mData = data;
    }

}
