package com.otitan.sclyyq.model.modelview;


import android.app.Dialog;

import com.otitan.sclyyq.activity.EventReportActivity;

/**
 * Created by otitan_li on 2018/6/14.
 * IEventReportModel
 */

public interface IEventReportModel extends IBaseModel{

    void senInofToServer(String json, String id, EventReportActivity activity);

    void senInofToServer(String json, String id, Dialog dialog);

}
