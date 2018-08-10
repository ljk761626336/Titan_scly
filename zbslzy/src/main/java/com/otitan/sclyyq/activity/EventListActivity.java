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
import android.widget.RadioButton;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.otitan.sclyyq.BaseActivity;
import com.otitan.sclyyq.R;
import com.otitan.sclyyq.adapter.EventListAdapter;
import com.otitan.sclyyq.dialog.EventListDialog;
import com.otitan.sclyyq.entity.Audio;
import com.otitan.sclyyq.entity.Emergency;
import com.otitan.sclyyq.entity.EventList;
import com.otitan.sclyyq.entity.Image;
import com.otitan.sclyyq.entity.Video;
import com.otitan.sclyyq.greendao.GreenDaoManager;
import com.otitan.sclyyq.service.RetrofitHelper;
import com.otitan.sclyyq.util.BussUtil;
import com.otitan.sclyyq.util.DividerGridItemDecoration;
import com.otitan.sclyyq.util.ToastUtil;
import com.titan.baselibrary.util.ProgressDialogUtil;
import com.titan.baselibrary.util.Util;

import java.io.File;
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
    private List<EventList> ysbList = new ArrayList<>();
    private List<EventList> wsbList = new ArrayList<>();
    //从本地数据库中取的数据
    private List<Emergency> dataList = new ArrayList<>();
    private int pageIndex = 1;
    private int pageSize = 20;
    private Handler handler;
    private boolean hasMore = true;
    private boolean listStatu = true;
    //是否有新上报的事件 false否 true是
    private boolean hasUpload = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_eventlist);
        if (BaseActivity.greenDaoManager == null) {
            BaseActivity.greenDaoManager = GreenDaoManager.getInstance();
        }
        initHandler();
        initView();
    }

    private void initView() {
        getWsbEventList();
        ImageView closeImg = findViewById(R.id.sjlb_close);
        closeImg.setOnClickListener(this);

        RadioButton wsbRb = findViewById(R.id.wsb_rb);
        RadioButton ysbRb = findViewById(R.id.ysb_rb);
        wsbRb.setChecked(true);
        wsbRb.setOnClickListener(this);
        ysbRb.setOnClickListener(this);

        refreshLayout = findViewById(R.id.sjlb_srf);
        recyclerView = findViewById(R.id.sjlb_rv);

        final LinearLayoutManager manager = new LinearLayoutManager(this, OrientationHelper.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        DividerGridItemDecoration itemDecoration = new DividerGridItemDecoration(mContext);
        recyclerView.addItemDecoration(itemDecoration);

        adapter = new EventListAdapter(this, wsbList);

        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new EventListAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                EventListDialog dialog = new EventListDialog(mContext, R.style.Dialog,
                        listStatu ? wsbList.get(position) : ysbList.get(position));
                BussUtil.setDialogParams(mContext, dialog, 0.5, 0.5);
            }

            @Override
            public void upLoad(int position) {
                if (wsbList.size() > position) {
                    Emergency emergency = dataList.get(position);
                    emergency.setXJ_ZPDZ(getList(emergency.getXJ_ZPDZ()));
                    emergency.setXJ_YPDZ(getList(emergency.getXJ_YPDZ()));
                    emergency.setXJ_SPDZ(getList(emergency.getXJ_SPDZ()));
                    emergency.setXJ_LX("[]");
                    upLoadEvent(emergency);
                }
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
                                adapter.setItems(ysbList);
                            }
                            break;
                        case 2:
                            ToastUtil.setToast(mContext, "没有更多数据了");
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
                return;
            case R.id.wsb_rb:
                if (!((RadioButton) view).isChecked()) {
                    break;
                }
                listStatu = true;
                if (wsbList.isEmpty()) {
                    getWsbEventList();
                    if (adapter != null) {
                        adapter.setItems(wsbList);
                    }
                } else {
                    if (adapter != null) {
                        adapter.setItems(wsbList);
                    }
                }
                break;
            case R.id.ysb_rb:
                if (!((RadioButton) view).isChecked()) {
                    break;
                }
                listStatu = false;
                if (ysbList.isEmpty() || hasUpload) {
                    pageIndex = 1;
                    hasMore = true;
                    ysbList.clear();
                    getEventList(pageIndex, pageSize);
                } else {
                    if (adapter != null) {
                        adapter.setItems(ysbList);
                    }
                }
                break;
        }
    }

    /**
     * 获取未上报事件集合
     */
    private void getWsbEventList() {
        ProgressDialogUtil.startProgressDialog(this, "加载中...");
        wsbList.clear();
        dataList.clear();
        List<Emergency> list = BaseActivity.greenDaoManager.getmDaoSession().getEmergencyDao().loadAll();
        if (list.isEmpty()) {
            ProgressDialogUtil.stopProgressDialog(mContext);
            ToastUtil.setToast(mContext, "暂无数据");
            return;
        }
        dataList.addAll(list);
        for (Emergency item : list) {
            EventList event = new EventList();
            event.setID(item.getID());
            event.setREMARK(item.getREMARK());
            event.setXJ_SJMC(item.getXJ_SJMC());
            event.setXJ_MSXX(item.getXJ_MSXX());
            event.setXJ_JD(item.getXJ_JD());
            event.setXJ_WD(item.getXJ_WD());
            event.setXJ_XXDZ(item.getXJ_XXDZ());
            event.setXJ_SBBH(item.getXJ_SBBH());
            event.setXJ_LX("[]");
            event.setXC_ID(item.getXC_ID());
            wsbList.add(event);
        }
        ProgressDialogUtil.stopProgressDialog(mContext);
        if (refreshLayout != null && refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(false);
        }
    }

    /**
     * 事件上报
     */
    private void upLoadEvent(final Emergency emergency) {
        ProgressDialogUtil.startProgressDialog(this, "上报中...");
        List<Emergency> list = new ArrayList<>();
        list.add(emergency);
        String json = new Gson().toJson(list);
        Observable<String> observable = RetrofitHelper.getInstance(mContext)
                .getServer().upRequisitionInfo(json);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        ProgressDialogUtil.stopProgressDialog(mContext);
                        hasUpload = false;
                        ToastUtil.setToast(mContext, "网络连接错误 " + throwable.getMessage());
                    }

                    @Override
                    public void onNext(String s) {
                        ProgressDialogUtil.stopProgressDialog(mContext);
                        if (s.equals("true")) {
                            hasUpload = true;
                            com.titan.baselibrary.util.ToastUtil.setToast(mContext, "上报成功");
                            BaseActivity.greenDaoManager.getmDaoSession().getEmergencyDao().deleteByKey(emergency.getID());
                            getWsbEventList();
                            if (adapter != null) {
                                adapter.setItems(wsbList);
                            }
                        } else {
                            hasUpload = false;
                            com.titan.baselibrary.util.ToastUtil.setToast(mContext, "上报失败,检查网络");
                        }
                    }
                });
    }

    /**
     * 将文件地址转换为实体类信息集合
     *
     * @param filePath
     * @return
     */
    private String getList(String filePath) {
        if (filePath == null) {
            filePath = "";
        }
        File file = new File(filePath);
        List<Object> list = new ArrayList<>();
        if (file.exists()) {
            if (filePath.endsWith(".mp4")) {
                Video video = new Video();
                video.setName(file.getName());
                video.setBase(Util.fileToString(filePath));
                list.add(video);
            } else if (filePath.endsWith(".m4a")) {
                Audio audio = new Audio();
                audio.setName(file.getName());
                audio.setBase(Util.fileToString(filePath));
                list.add(audio);
            } else if (filePath.endsWith(".jpg") || filePath.endsWith(".png") || filePath.endsWith(".jpeg")) {
                Image image = new Image();
                image.setBase(Util.picToString(filePath));
                image.setName(file.getName());
                list.add(image);
            }
        }
        return new Gson().toJson(list);
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
                        hasUpload = true;
                        ToastUtil.setToast(mContext, "网络错误:" + e.getMessage());
                        Message message = new Message();
                        handler.sendMessage(message);
                    }

                    @Override
                    public void onNext(String s) {
                        hasUpload = false;
                        ProgressDialogUtil.stopProgressDialog(mContext);
                        Gson gson = new Gson();
                        Type type = new TypeToken<ArrayList<EventList>>() {
                        }.getType();
                        List<EventList> tempList = gson.fromJson(s, type);
                        if (!tempList.isEmpty()) {
                            ysbList.addAll(tempList);
                            EventListActivity.this.pageIndex += 1;
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
        ysbList.clear();
        if (listStatu) {
            getWsbEventList();
            if (adapter != null) {
                adapter.setItems(wsbList);
            }
        } else {
            getEventList(pageIndex, pageSize);
        }
    }
}
