package com.otitan.sclyyq.model.modelview;

import android.content.Context;

import com.esri.core.geometry.Polyline;
import com.otitan.sclyyq.entity.Trajectory;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by otitan_li on 2018/7/2.
 * ITrajectoryModel
 * 轨迹记录上传接口
 */

public interface ITrajectoryModel extends Serializable,IBaseModel{

    void sendTrajectory(Context context, Trajectory trajectory);

    Date getStartDate();

    Date getEndDate();

    void setStartDate(Date date);

    void setEndDate(Date date);

    void setStartAddr(String addr);

    String getStartAddr();

    void setEndAddr(String addr);

    String getEndAddr();

    void setLine(Polyline line);

    Polyline getLine();

}
