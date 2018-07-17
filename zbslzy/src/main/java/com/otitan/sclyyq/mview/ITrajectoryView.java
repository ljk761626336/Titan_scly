package com.otitan.sclyyq.mview;

/**
 * Created by otitan_li on 2018/7/2.
 * ITrajectoryView
 */

public interface ITrajectoryView extends IBaseView{

    /*隐藏开始记录按钮 显示暂停 标注 结束 按钮*/
    void showRecord();

    /*隐藏 暂停 标注 结束 按钮 显示开始记录按钮*/
    void hideRecord();

    /*暂停按钮*/
    void suspendRecord();


}
