package com.otitan.sclyyq.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.otitan.sclyyq.BaseActivity;
import com.otitan.sclyyq.R;
import com.otitan.sclyyq.adapter.EventListAdapter;
import com.otitan.sclyyq.dialog.EventListDialog;
import com.otitan.sclyyq.entity.EventList;
import com.otitan.sclyyq.service.RetrofitHelper;
import com.otitan.sclyyq.util.BussUtil;
import com.otitan.sclyyq.util.DividerGridItemDecoration;
import com.otitan.sclyyq.util.ToastUtil;
import com.titan.baselibrary.util.ProgressDialogUtil;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 事件列表
 */
public class EventListActivity extends AppCompatActivity implements View.OnClickListener
        , SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;
    private Context mContext;
    private EventListAdapter adapter;
    private List<EventList> list = new ArrayList<>();
    private int pageIndex = 1;
    private int pageSize = 20;
    private Handler handler;
    private boolean hasMore = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_eventlist);

        initHandler();
        initView();
    }

    private void initView() {
        getEventList(pageIndex, pageSize);
        ImageView closeImg = findViewById(R.id.sjlb_close);
        closeImg.setOnClickListener(this);
        refreshLayout = findViewById(R.id.sjlb_srf);
        recyclerView = findViewById(R.id.sjlb_rv);
        final LinearLayoutManager manager = new LinearLayoutManager(this, OrientationHelper.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        DividerGridItemDecoration itemDecoration = new DividerGridItemDecoration(mContext);
        recyclerView.addItemDecoration(itemDecoration);
        adapter = new EventListAdapter(this, list);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new EventListAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                EventListDialog dialog = new EventListDialog(mContext, R.style.Dialog, list.get(position));
                BussUtil.setDialogParams(mContext, dialog, 0.5, 0.5);
            }
        });

        refreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary);
        refreshLayout.setOnRefreshListener(this);
        final int[] lastVisibleItem = new int[1];
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem[0] == adapter.getItemCount() - 1) {
                    if (!refreshLayout.isRefreshing() && hasMore) {
                        getEventList(pageIndex, pageSize);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem[0] = manager.findLastVisibleItemPosition();
            }
        });
    }


    private void initHandler() {
        if (handler == null) {
            handler = new Handler(Looper.getMainLooper()) {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    switch (msg.what) {
                        case 1:
                            if (adapter != null) {
                                adapter.setItems(list);
                            }
                            break;
                        case 2:
                            ToastUtil.setToast(mContext,"没有更多数据了");
                            break;
                    }
                    refreshLayout.setRefreshing(false);
                }
            };
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sjlb_close:
                finish();
                break;
        }
    }

    /**
     * 获取事件集合
     *
     * @param pageIndex 分页索引
     * @param pageSize  每页请求数量
     */
    private void getEventList(final int pageIndex, int pageSize) {
        ProgressDialogUtil.startProgressDialog(this, "加载中...");
        Observable<String> observable = RetrofitHelper.getInstance(this)
                .getServer().getUPPatrolEvent(pageIndex, pageSize);
        observable.subscribeOn(Schedulers.io()).subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        ProgressDialogUtil.stopProgressDialog(mContext);
                        ToastUtil.setToast(mContext, "网络错误:" + e.getMessage());
                        Message message = new Message();
                        handler.sendMessage(message);
                    }

                    @Override
                    public void onNext(String s) {
                        ProgressDialogUtil.stopProgressDialog(mContext);
                        Gson gson = new Gson();
                        Type type = new TypeToken<ArrayList<EventList>>() {
                        }.getType();
                        List<EventList> tempList = gson.fromJson(s, type);
                        if (!tempList.isEmpty()) {
                            list.addAll(tempList);
                            EventListActivity.this.pageIndex += pageIndex;
                            Message message = new Message();
                            message.what = 1;
                            handler.sendMessage(message);
                        } else {
                            hasMore = false;
                            Message message = new Message();
                            message.what = 2;
                            handler.sendMessage(message);
                        }
                    }
                });
    }

    @Override
    public void onRefresh() {
        pageIndex = 1;
        hasMore = true;
        list.clear();
        getEventList(pageIndex, pageSize);
    }

//    private void getLocEventList(){
//        list = BaseActivity.greenDaoManager.getmDaoSession().getEmergencyDao().loadAll();
//    }
}
