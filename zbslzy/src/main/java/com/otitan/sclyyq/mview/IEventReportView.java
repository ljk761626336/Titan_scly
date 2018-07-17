package com.otitan.sclyyq.mview;

import android.app.Dialog;

import java.io.Serializable;


/**
 * Created by otitan_li on 2018/6/14.
 * IEventReportView
 */

public interface IEventReportView extends IBaseView,Serializable {

    void setReportDialog(Dialog dialog);

    Dialog getReportDialog();

}
