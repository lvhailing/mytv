package com.jbtm.parentschool.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.jbtm.parentschool.R;
import com.jbtm.parentschool.adapter.WatchHistoryAdapter;
import com.jbtm.parentschool.models.CommonWrapper;
import com.jbtm.parentschool.models.HomeWrapper;
import com.jbtm.parentschool.models.WatchHistoryModel;
import com.jbtm.parentschool.network.MyObserverAdapter;
import com.jbtm.parentschool.network.MyRemoteFactory;
import com.jbtm.parentschool.network.MyRequestProxy;
import com.jbtm.parentschool.network.model.ResultModel;
import com.jbtm.parentschool.utils.RequestUtil;
import com.jbtm.parentschool.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class WatchHistoryView extends LinearLayout {
    private RecyclerView recyclerView;
    private Context mContext;

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

        getHistory();
    }

    //获取播放记录
    private void getHistory() {
        Map<String, Object> map = RequestUtil.getBasicMapNoBusinessParams();

        MyRemoteFactory.getInstance().getProxy(MyRequestProxy.class)
                .getHistory(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyObserverAdapter<ResultModel<CommonWrapper>>() {
                    @Override
                    public void onMyError(Throwable e) {
                        ToastUtil.showCustom("调接口失败");
                    }

                    @Override
                    public void onMySuccess(ResultModel<CommonWrapper> result) {
                        if (result.result != null) {
                            List<WatchHistoryModel> courses = result.result.courses;
                            WatchHistoryAdapter adapter = new WatchHistoryAdapter(mContext, courses);
                            recyclerView.setAdapter(adapter);
                        }
                    }
                });
    }
}
