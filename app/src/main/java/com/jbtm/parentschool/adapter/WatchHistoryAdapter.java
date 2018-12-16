package com.jbtm.parentschool.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jbtm.parentschool.R;
import com.jbtm.parentschool.activity.VideoActivity;
import com.jbtm.parentschool.models.WatchHistoryModel;
import com.jbtm.parentschool.utils.ToastUtil;

import java.util.List;

public class WatchHistoryAdapter extends RecyclerView.Adapter<WatchHistoryAdapter.ViewHolder> {
    private List<WatchHistoryModel> list;
    private Context mContext;
    private float scaleValue = 1.1f;
    private int scaleTime = 200;

    public WatchHistoryAdapter(Context context, List<WatchHistoryModel> list) {
        super();
        mContext = context;
        this.list = list;
    }

    public void setData(List<WatchHistoryModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_watch_history, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {
        viewHolder.tv_progress.setText("已观看" + list.get(position).progress);

        listenViewFocus(viewHolder.itemView, viewHolder.v_bg);
        listenViewClick(viewHolder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView iv;
        private ImageView v_bg;    //边框
        private TextView tv_progress;
        private RelativeLayout rl_pic;
        private View itemView;

        public ViewHolder(View itemView) {
            super(itemView);

            this.itemView = itemView;
            iv = itemView.findViewById(R.id.iv);
            v_bg = itemView.findViewById(R.id.v_bg);
            tv_progress = itemView.findViewById(R.id.tv_progress);
            rl_pic = itemView.findViewById(R.id.rl_pic);
        }
    }

    private void listenViewClick(View itemView, final int position) {
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showCustom(list.get(position).title);
                VideoActivity.startActivity(mContext);
            }
        });
    }

    private void listenViewFocus(View itemView, final ImageView v_bg) {
        itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View v, final boolean hasFocus) {
                //获取焦点时变化
                if (hasFocus) {
                    ViewCompat.animate(v)
                            .scaleX(scaleValue)
                            .scaleY(scaleValue)
                            .setDuration(scaleTime)
                            .setListener(new ViewPropertyAnimatorListener() {
                                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                                @Override
                                public void onAnimationStart(View view) {
                                    v_bg.setVisibility(View.VISIBLE);
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
                                    v_bg.setVisibility(View.GONE);
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
