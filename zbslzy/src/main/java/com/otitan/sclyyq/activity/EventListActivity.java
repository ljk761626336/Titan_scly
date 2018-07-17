package com.otitan.sclyyq.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.otitan.sclyyq.R;
import com.otitan.sclyyq.adapter.EventListAdapter;
import com.otitan.sclyyq.entity.EventList;
import com.otitan.sclyyq.service.RetrofitHelper;
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
public class EventListActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    private Context mContext;
    private EventListAdapter adapter;
    private List<EventList> list = new ArrayList<>();
    private int pageIndex =1;
    private int pageSize = 20;
    private Handler handler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_eventlist);

        initHandler();
        initView();
    }

    private void initView() {
        getEventList(pageIndex,pageSize);
        ImageView closeImg = findViewById(R.id.sjlb_close);
        closeImg.setOnClickListener(this);
        recyclerView = findViewById(R.id.sjlb_rv);
        LinearLayoutManager manager = new LinearLayoutManager(this, OrientationHelper.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        DividerGridItemDecoration itemDecoration = new DividerGridItemDecoration(mContext);
        recyclerView.addItemDecoration(itemDecoration);
        adapter = new EventListAdapter(this, list);
        recyclerView.setAdapter(adapter);
    }


    private void initHandler(){
        if (handler==null){
            handler = new Handler(Looper.getMainLooper()){
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    if (msg.what==1){
                        if (adapter != null) {
                            adapter.setItems(list);
                        }
                    }
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
    private void getEventList(int pageIndex, int pageSize) {
        ProgressDialogUtil.startProgressDialog(this,"加载中...");
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
                    }

                    @Override
                    public void onNext(String s) {
                        ProgressDialogUtil.stopProgressDialog(mContext);
                        Gson gson = new Gson();
                        Type type = new TypeToken<ArrayList<EventList>>() {
                        }.getType();
                        list = gson.fromJson(s, type);
                        Message message = new Message();
                        message.what = 1;
                        handler.sendMessage(message);
                    }
                });
    }
}
