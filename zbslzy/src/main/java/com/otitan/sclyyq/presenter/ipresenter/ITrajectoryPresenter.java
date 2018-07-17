package com.otitan.sclyyq.presenter.ipresenter;


import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by otitan_li on 2018/7/2.
 * ITrajectoryPresenter
 */

public interface ITrajectoryPresenter {

    /*开始记录轨迹*/
    void startReport();

    /*暂停记录轨迹*/
    void suspendReport();

    /*添加轨迹标注*/
    void lableReport();

    /*结束记录轨迹*/
    void endReport();

    /*记录 轨迹点并标会出来*/
    void recordLine();

    void initMyTrajectorySearch(View view);

    void initSelectTimePopuwindow(TextView view, boolean flag);

    void initSelectTimePopuwindow(Button button, boolean flag);

}
