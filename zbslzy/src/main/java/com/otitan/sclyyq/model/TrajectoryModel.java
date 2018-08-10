package com.otitan.sclyyq.model;

import android.content.Context;
import android.util.Log;

import com.esri.core.geometry.Polyline;
import com.google.gson.Gson;
import com.otitan.sclyyq.entity.Trajectory;
import com.otitan.sclyyq.model.modelview.ITrajectoryModel;
import com.otitan.sclyyq.service.RetrofitHelper;
import com.titan.baselibrary.util.ToastUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by otitan_li on 2018/7/2.
 * TrajectoryModel
 */

public class TrajectoryModel extends BaseModel implements ITrajectoryModel {

    public String getStartAddress() {
        return startAddress;
    }

    public void setStartAddress(String startAddress) {
        this.startAddress = startAddress;
    }

    public String getEndAddress() {
        return endAddress;
    }

    public void setEndAddress(String endAddress) {
        this.endAddress = endAddress;
    }

    private String startAddress;
    private String endAddress;

    @Override
    public void setStartAddr(String addr) {
        setStartAddress(addr);
    }

    @Override
    public String getStartAddr() {
        return getStartAddress();
    }

    @Override
    public void setEndAddr(String addr) {
        setEndAddress(addr);
    }

    @Override
    public String getEndAddr() {
        return getEndAddress();
    }

    @Override
    public void setLine(Polyline line) {

    }

    @Override
    public Polyline getLine() {
        return null;
    }

    private static final long serialVersionUID = 1974378498838922229L;

    public Polyline getPolyline() {
        return polyline;
    }

    public void setPolyline(Polyline polyline) {
        this.polyline = polyline;
    }

    private Polyline polyline;

    private Date startTime;

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    private Date endTime;


    @Override
    public void sendTrajectory(final Context context, final Trajectory trajectory) {
        List<Trajectory> list = new ArrayList<>();
        list.add(trajectory);
        Gson gson = new Gson();
        String json = gson.toJson(list);
        rx.Observable<String> oberver = RetrofitHelper.getInstance(context).getServer().uPPatrolLine(json);
        oberver.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        ToastUtil.setToast(context, "轨迹记录上传异常" + throwable.getMessage());
                        sendTrajectory(context, trajectory);
                        Log.e("tag","轨迹上报错误");
                    }

                    @Override
                    public void onNext(String s) {
                        if (s.equals("true") && trajectory.getXC_ENDTIME().equals("")) {
                            String id = trajectory.getXC_ID();
                            setXCID(id);
                        } else if (s.equals("true") && !trajectory.getXC_ENDTIME().equals("")) {
                            ToastUtil.setToast(context, "轨迹记录上传成功");
                        } else if (s.equals("false") && !trajectory.getXC_ENDTIME().equals("")) {
                            ToastUtil.setToast(context, "轨迹记录上传失败");
                        }
                    }
                });
    }

    @Override
    public Date getStartDate() {
        return getStartTime();
    }

    @Override
    public Date getEndDate() {
        return getEndTime();
    }

    @Override
    public void setStartDate(Date date) {
        setStartTime(date);
    }

    @Override
    public void setEndDate(Date date) {
        setEndTime(date);
    }


}
