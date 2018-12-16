package com.jbtm.parentschool.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jbtm.parentschool.R;
import com.jbtm.parentschool.adapter.HomeAdapter;
import com.jbtm.parentschool.adapter.WatchHistoryAdapter;
import com.jbtm.parentschool.models.WatchHistoryModel;
import com.jbtm.parentschool.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;


public class WatchHistoryView extends LinearLayout {
    private RecyclerView recyclerView;
    private Context mContext;
    private List<WatchHistoryModel> list;

    public WatchHistoryView(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public WatchHistoryView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    private void init() {
        View view = View.inflate(mContext, R.layout.view_watch_history, this);

        recyclerView = view.findViewById(R.id.rv_history);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 4);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setFocusable(false);

        list = new ArrayList<>();
        list.add(new WatchHistoryModel(1, "美国课程", "aa", 30));
        list.add(new WatchHistoryModel(1, "美国课程", "aa", 40));
        list.add(new WatchHistoryModel(1, "美国课程", "aa", 50));
        list.add(new WatchHistoryModel(1, "美国课程", "aa", 60));
        list.add(new WatchHistoryModel(1, "美国课程", "aa", 70));
        list.add(new WatchHistoryModel(1, "美国课程", "aa", 80));
        WatchHistoryAdapter adapter = new WatchHistoryAdapter(mContext, list);
        recyclerView.setAdapter(adapter);
    }

    public void setData(List<WatchHistoryModel> list) {
        this.list = list;
    }
}
